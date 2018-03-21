package com.comp445.lab2.file.server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileServerHandler {
    private static final String FILE_DIR = "src/main/resources/";

    public String fetchDirectory(DirectoryOutputMethod method) {
        switch (method) {
            case Plain:
                return getFilesInPlain();
            default:
                return "Not Implemented Yet"; // TODO
        }
    }

    public String fetchFile(String path) throws FileNotFoundException {
        try {
            return new String(Files.readAllBytes(Paths.get(FILE_DIR + path)));
        } catch (Exception e) {
            throw new FileNotFoundException(path);
        }
    }

    private File[] fetchFiles() {
        File folder = new File(FILE_DIR);
        File[] listOfFiles = folder.listFiles();
        if (listOfFiles == null)
            return new File[0];
        return listOfFiles;
    }

    private String getFilesInPlain() {
        StringBuilder str = new StringBuilder("files:\n");
        for (File file : fetchFiles()) {
            str.append(file.getName()).append("\n");
        }
        return str.toString();
    }
}
