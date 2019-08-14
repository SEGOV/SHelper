package com.server.service.file;

public class FileService {
    private static final FileService INSTANCE = new FileService();

    public static FileService getInstance() {
        return INSTANCE;
    }
}
