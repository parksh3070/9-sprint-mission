package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.dto.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.BinaryContentDto;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BasicBinaryContentService implements BinaryContentService {

    private final BinaryContentRepository binaryContentRepository;
    private final BinaryContentStorage binaryContentStorage;

    @Override
    @Transactional
    public BinaryContentDto create(BinaryContentCreateRequest request) {
        byte[] bytes = request.getData();

        BinaryContent binaryContent = new BinaryContent(
                request.getFilename(),
                (long) bytes.length,
                request.getContentType()
        );

        BinaryContent saved = binaryContentRepository.save(binaryContent);

        binaryContentStorage.put(saved.getId(), bytes);

        return toDto(saved);
    }

    @Override
    public BinaryContentDto find(UUID id) {
        BinaryContent binaryContent = binaryContentRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("BinaryContent with id " + id + " not found"));

        return toDto(binaryContent);
    }

    @Override
    public List<BinaryContentDto> findAllByIdIn(List<UUID> ids) {
        return binaryContentRepository.findAllById(ids).stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public ResponseEntity<?> download(UUID id) {
        BinaryContent binaryContent = binaryContentRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("BinaryContent with id " + id + " not found"));

        return binaryContentStorage.download(toDto(binaryContent));
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        if (!binaryContentRepository.existsById(id)) {
            throw new NoSuchElementException("BinaryContent with id " + id + " not found");
        }

        binaryContentRepository.deleteById(id);
    }

    private BinaryContentDto toDto(BinaryContent binaryContent) {
        return new BinaryContentDto(
                binaryContent.getId(),
                binaryContent.getFileName(),
                binaryContent.getSize(),
                binaryContent.getContentType()
        );
    }
}