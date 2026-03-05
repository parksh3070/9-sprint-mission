package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;

import java.io.*;
import java.nio.file.*;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class FileBinaryContentRepository implements BinaryContentRepository {

    private final Path DIRECTORY;
    private final String EXTENSION = ".ser";

    public FileBinaryContentRepository(String baseDirectory) {
        this.DIRECTORY = Paths.get(System.getProperty("user.dir"), baseDirectory, BinaryContent.class.getSimpleName());
        if (Files.notExists(DIRECTORY)) {
            try { Files.createDirectories(DIRECTORY); }
            catch (IOException e) { throw new RuntimeException(e); }
        }
    }

    private Path resolvePath(UUID id) { return DIRECTORY.resolve(id + EXTENSION); }

    @Override
    public BinaryContent save(BinaryContent binaryContent) {
        Path path = resolvePath(binaryContent.getId());
        try (FileOutputStream fos = new FileOutputStream(path.toFile());
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(binaryContent);
        } catch (IOException e) { throw new RuntimeException(e); }
        return binaryContent;
    }

    @Override
    public Optional<BinaryContent> findById(UUID id) {
        BinaryContent value = null;
        Path path = resolvePath(id);
        if (Files.exists(path)) {
            try (FileInputStream fis = new FileInputStream(path.toFile());
                 ObjectInputStream ois = new ObjectInputStream(fis)) {
                value = (BinaryContent) ois.readObject();
            } catch (IOException | ClassNotFoundException e) { throw new RuntimeException(e); }
        }
        return Optional.ofNullable(value);
    }

    @Override
    public List<BinaryContent> findAllByIdIn(List<UUID> ids) {
        return ids.stream()
                .map(this::findById)
                .flatMap(Optional::stream)
                .toList();
    }

    @Override
    public void deleteById(UUID id) {
        try { Files.deleteIfExists(resolvePath(id)); }
        catch (IOException e) { throw new RuntimeException(e); }
    }
}