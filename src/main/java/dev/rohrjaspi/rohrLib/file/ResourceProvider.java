package dev.rohrjaspi.rohrlib.file;

import java.io.InputStream;

@FunctionalInterface
public interface ResourceProvider {
    InputStream open(String resourcePath);
}