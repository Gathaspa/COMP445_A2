package com.comp445.lab2.file.server;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.util.Arrays;

public class FileServerHandler {
    private File[] files;

    public String fetchDirectory(directoryOutputMethod method) throws IOException {
        fetchFiles(); // update every time fetch is called in case the directory contents changed.
        switch (method) {
            case Plain:
                return getFilesInPlain();
            default:
                return "Not Implemented Yet"; // TODO
        }
    }

    public String fetchFile(String path) throws Exception {
        fetchFiles(); // update every time fetch is called in case the directory contents changed.
        try {
            return new String(Files.readAllBytes(Paths.get("src/main/resources/" + path)));
        } catch (Exception e) {
            return "Error: File not found!";
        }
    }

    private void fetchFiles() throws IOException {
        File folder = new File("src/main/resources");
        File[] listOfFiles = folder.listFiles();
        if (listOfFiles == null) throw new IOException("Invalid Path!");
        for (File f : listOfFiles) {
            if (f.isFile()) {
                System.out.println("File " + f.getName());
            } else if (f.isDirectory()) {
                System.out.println("Directory " + f.getName());
            }
        }
        files = listOfFiles;
    }

    private String getFilesInPlain() {
        StringBuilder str = new StringBuilder("files:\n");
        for (File file : files) {
            str.append(file.getName()).append("\n");
        }
        return str.toString();
    }
}
