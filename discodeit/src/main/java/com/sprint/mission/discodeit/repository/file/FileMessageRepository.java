package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.io.*;
import java.nio.file.*;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class FileMessageRepository implements MessageRepository {
    private final Path DIRECTORY;
    private final String EXTENSION = ".ser";

    public FileMessageRepository(String baseDirectory) {
        this.DIRECTORY = Paths.get(System.getProperty("user.dir"), baseDirectory, Message.class.getSimpleName());
        if (Files.notExists(DIRECTORY)) {
            try { Files.createDirectories(DIRECTORY); }
            catch (IOException e) { throw new RuntimeException(e); }
        }
    }

    private Path resolvePath(UUID id) { return DIRECTORY.resolve(id + EXTENSION); }

    @Override
    public Message save(Message message) {
        Path path = resolvePath(message.getId());
        try (FileOutputStream fos = new FileOutputStream(path.toFile());
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(message);
        } catch (IOException e) { throw new RuntimeException(e); }
        return message;
    }

    @Override
    public Optional<Message> findById(UUID id) {
        Message value = null;
        Path path = resolvePath(id);
        if (Files.exists(path)) {
            try (FileInputStream fis = new FileInputStream(path.toFile());
                 ObjectInputStream ois = new ObjectInputStream(fis)) {
                value = (Message) ois.readObject();
            } catch (IOException | ClassNotFoundException e) { throw new RuntimeException(e); }
        }
        return Optional.ofNullable(value);
    }

    @Override
    public List<Message> findAll() {
        try {
            return Files.list(DIRECTORY)
                    .filter(p -> p.toString().endsWith(EXTENSION))
                    .map(p -> {
                        try (FileInputStream fis = new FileInputStream(p.toFile());
                             ObjectInputStream ois = new ObjectInputStream(fis)) {
                            return (Message) ois.readObject();
                        } catch (IOException | ClassNotFoundException e) { throw new RuntimeException(e); }
                    })
                    .toList();
        } catch (IOException e) { throw new RuntimeException(e); }
    }

    @Override
    public boolean existsById(UUID id) { return Files.exists(resolvePath(id)); }

    @Override
    public void deleteById(UUID id) {
        try { Files.deleteIfExists(resolvePath(id)); }
        catch (IOException e) { throw new RuntimeException(e); }
    }
}