package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;

import java.io.*;
import java.nio.file.*;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class FileUserStatusRepository implements UserStatusRepository {

    private final Path DIRECTORY;
    private final String EXTENSION = ".ser";

    public FileUserStatusRepository(String baseDirectory) {
        this.DIRECTORY = Paths.get(System.getProperty("user.dir"), baseDirectory, UserStatus.class.getSimpleName());
        if (Files.notExists(DIRECTORY)) {
            try { Files.createDirectories(DIRECTORY); }
            catch (IOException e) { throw new RuntimeException(e); }
        }
    }

    private Path resolvePath(UUID id) { return DIRECTORY.resolve(id + EXTENSION); }

    @Override
    public UserStatus save(UserStatus userStatus) {
        Path path = resolvePath(userStatus.getId());
        try (FileOutputStream fos = new FileOutputStream(path.toFile());
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(userStatus);
        } catch (IOException e) { throw new RuntimeException(e); }
        return userStatus;
    }

    @Override
    public Optional<UserStatus> findById(UUID id) {
        UserStatus value = null;
        Path path = resolvePath(id);
        if (Files.exists(path)) {
            try (FileInputStream fis = new FileInputStream(path.toFile());
                 ObjectInputStream ois = new ObjectInputStream(fis)) {
                value = (UserStatus) ois.readObject();
            } catch (IOException | ClassNotFoundException e) { throw new RuntimeException(e); }
        }
        return Optional.ofNullable(value);
    }

    @Override
    public Optional<UserStatus> findByUserId(UUID userId) {
        return findAll().stream()
                .filter(s -> s.getUserId().equals(userId))
                .findFirst();
    }

    @Override
    public List<UserStatus> findAll() {
        try {
            return Files.list(DIRECTORY)
                    .filter(p -> p.toString().endsWith(EXTENSION))
                    .map(p -> {
                        try (FileInputStream fis = new FileInputStream(p.toFile());
                             ObjectInputStream ois = new ObjectInputStream(fis)) {
                            return (UserStatus) ois.readObject();
                        } catch (IOException | ClassNotFoundException e) { throw new RuntimeException(e); }
                    })
                    .toList();
        } catch (IOException e) { throw new RuntimeException(e); }
    }

    @Override
    public void deleteById(UUID id) {
        try { Files.deleteIfExists(resolvePath(id)); }
        catch (IOException e) { throw new RuntimeException(e); }
    }
}