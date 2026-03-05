package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.io.*;
import java.nio.file.*;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class FileChannelRepository implements ChannelRepository {
    private final Path DIRECTORY;
    private final String EXTENSION = ".ser";

    public FileChannelRepository(String baseDirectory) {
        this.DIRECTORY = Paths.get(System.getProperty("user.dir"), baseDirectory, Channel.class.getSimpleName());
        if (Files.notExists(DIRECTORY)) {
            try { Files.createDirectories(DIRECTORY); }
            catch (IOException e) { throw new RuntimeException(e); }
        }
    }

    private Path resolvePath(UUID id) { return DIRECTORY.resolve(id + EXTENSION); }

    @Override
    public Channel save(Channel channel) {
        Path path = resolvePath(channel.getId());
        try (FileOutputStream fos = new FileOutputStream(path.toFile());
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(channel);
        } catch (IOException e) { throw new RuntimeException(e); }
        return channel;
    }

    @Override
    public Optional<Channel> findById(UUID id) {
        Channel value = null;
        Path path = resolvePath(id);
        if (Files.exists(path)) {
            try (FileInputStream fis = new FileInputStream(path.toFile());
                 ObjectInputStream ois = new ObjectInputStream(fis)) {
                value = (Channel) ois.readObject();
            } catch (IOException | ClassNotFoundException e) { throw new RuntimeException(e); }
        }
        return Optional.ofNullable(value);
    }

    @Override
    public List<Channel> findAll() {
        try {
            return Files.list(DIRECTORY)
                    .filter(p -> p.toString().endsWith(EXTENSION))
                    .map(p -> {
                        try (FileInputStream fis = new FileInputStream(p.toFile());
                             ObjectInputStream ois = new ObjectInputStream(fis)) {
                            return (Channel) ois.readObject();
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