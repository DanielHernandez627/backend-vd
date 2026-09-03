package com.back.vd.infrastructure.storage;

import com.back.vd.domain.ports.output.VideoStoragePort;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

@Component
public class LocalFileSystemVideoAdapter implements VideoStoragePort {

    @Override
    public Resource loadVideoAsResource(String videoPath) throws IOException {
        File file = new File(videoPath);
        if (!file.exists() || !file.isFile()) {
            throw new FileNotFoundException("El archivo de video no existe en la ruta: " + videoPath);
        }
        return new FileSystemResource(file);
    }

    @Override
    public boolean exists(String videoPath) {
        if (videoPath == null || videoPath.isBlank()) {
            return false;
        }
        File file = new File(videoPath);
        return file.exists() && file.isFile();
    }
}
