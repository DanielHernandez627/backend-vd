package com.back.vd.domain.ports.output;

import org.springframework.core.io.Resource;
import java.io.IOException;

public interface VideoStoragePort {
    Resource loadVideoAsResource(String videoPath) throws IOException;
    boolean exists(String videoPath);
}
