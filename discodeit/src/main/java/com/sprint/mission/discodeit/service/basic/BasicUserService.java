package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.dto.ProfileImageRequest;
import com.sprint.mission.discodeit.service.dto.UserCreateRequest;
import com.sprint.mission.discodeit.service.dto.UserUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@RequiredArgsConstructor
public class BasicUserService implements UserService {

    // Service는 "무엇을 저장/조회할지"만 알고, "어떻게 저장되는지(File/JCF)"는 Repository가 담당한다.
    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;
    private final BinaryContentRepository binaryContentRepository;

    @Override
    public User create(UserCreateRequest request) {
        // 1) DTO에서 값 꺼내기 (DTO를 쓰는 이유: 파라미터가 늘어나도 메서드 시그니처가 깔끔함)
        String username = request.getUsername();
        String email = request.getEmail();
        String password = request.getPassword();

        // 2) 중복 검사 (현재 Repository에 findByUsername/findByEmail이 없으니, findAll로 간단 구현)
        boolean usernameExists = userRepository.findAll().stream()
                .anyMatch(u -> u.getUsername().equals(username));
        if (usernameExists) {
            throw new IllegalArgumentException("이미 존재하는 username 입니다.");
        }

        boolean emailExists = userRepository.findAll().stream()
                .anyMatch(u -> u.getEmail().equals(email));
        if (emailExists) {
            throw new IllegalArgumentException("이미 존재하는 email 입니다.");
        }

        // 3) User 생성 + 저장
        User user = new User(username, email, password);
        userRepository.save(user);

        // 4) UserStatus 생성 + 저장 (요구사항: 유저 생성 시 상태도 같이 생성)
        UserStatus status = new UserStatus(user.getId());
        userStatusRepository.save(status);

        // 5) (선택) 프로필 이미지 저장
        // 현재 User 엔티티에 profileImageId를 아직 추가하지 않았을 수 있으므로
        // "일단 저장만" 하고 연결은 다음 스텝에서 진행하도록 둔다.
        ProfileImageRequest profile = request.getProfileImageRequest();
        if (profile != null) {
            BinaryContent binaryContent = new BinaryContent(
                    profile.getData(),
                    profile.getFilename(),
                    profile.getContentType(),
                    user.getId(),
                    null
            );
            binaryContentRepository.save(binaryContent);

            user.changeProfileImage(binaryContent.getId());
            userRepository.save(user);
        }

        return user;
    }

    @Override
    public User find(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User with id " + userId + " not found"));
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User update(UserUpdateRequest request) {
        UUID userId = request.getUserId();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User with id " + userId + " not found"));

        String newUsername = request.getNewUsername();
        String newEmail = request.getNewEmail();
        String newPassword = request.getNewPassword();

        if (newUsername != null) {
            boolean usernameExists = userRepository.findAll().stream()
                    .anyMatch(u -> !u.getId().equals(userId) && u.getUsername().equals(newUsername));
            if (usernameExists) throw new IllegalArgumentException("이미 존재하는 username 입니다.");
        }

        if (newEmail != null) {
            boolean emailExists = userRepository.findAll().stream()
                    .anyMatch(u -> !u.getId().equals(userId) && u.getEmail().equals(newEmail));
            if (emailExists) throw new IllegalArgumentException("이미 존재하는 email 입니다.");
        }

        // 1) 기본 정보 업데이트
        user.update(newUsername, newEmail, newPassword);

        // 2) 프로필 이미지 교체(있으면)
        ProfileImageRequest profile = request.getProfileImageRequest();
        if (profile != null) {
            UUID oldProfileImageId = user.getProfileImageId();

            BinaryContent newContent = new BinaryContent(
                    profile.getData(),
                    profile.getFilename(),
                    profile.getContentType(),
                    user.getId(),
                    null
            );
            binaryContentRepository.save(newContent);

            user.changeProfileImage(newContent.getId());

            if (oldProfileImageId != null) {
                binaryContentRepository.deleteById(oldProfileImageId);
            }
        }

        // 3) 마지막에 한 번만 저장 + 저장 결과 반환
        return userRepository.save(user);
    }

    @Override
    public void delete(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User with id " + userId + " not found"));

        UUID profileImageId = user.getProfileImageId();
        if (profileImageId != null) {
            binaryContentRepository.deleteById(profileImageId);
        }

        userStatusRepository.findByUserId(userId)
                .ifPresent(status -> userStatusRepository.deleteById(status.getId()));

        userRepository.deleteById(userId);
    }
}

        // (요구사항) 관련 도메인도 같이 삭제:
        // - BinaryContent(프로필)
        // - UserStatus
        // 연결 필드(User.profileImageId)가 아직 없으니, 이것도 다음 스텝에서 같이 마무리하자.

