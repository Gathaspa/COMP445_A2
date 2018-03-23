package com.comp445.lab2.file.server;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileServerHandler {
    private static final String FILE_DIR = "src/main/resources/";

    public String fetchDirectory(DirectoryOutputMethod method) {
        switch (method) {
            case XML:
                return getFilesInXml();
            case HTML:
                return getFilesInHtml();
            case JSON:
                return getFilesInJson();
            default:
                return getFilesInPlain();
        }
    }

    public String fetchFile(String path) throws FileNotFoundException {
        try {
            File f = new File(FILE_DIR + path);

            synchronized (f.getCanonicalPath().intern()) {
                return new String(Files.readAllBytes(Paths.get(FILE_DIR + path)));
            }
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

    private String getFilesInHtml() {
        StringBuilder str = new StringBuilder("<!DOCTYPE html>\n" +
                "<head><title>File List</title></head>\n<body>\n" +
                "<table style=\"border: 1px solid black;\">\n");
        for (File file : fetchFiles()) {
            str.append("<tr>\n<td>").append(file.getName()).append("</td>\n</tr>\n");
        }
        str.append("</table>\n</body>\n</html>");
        return str.toString();
    }

    private String getFilesInXml() {
            StringBuilder str = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                    "<table style=\"border: 1px solid black;\">\n");
            for (File file : fetchFiles()) {
                str.append("<tr>\n<td>").append(file.getName()).append("</td>\n</tr>\n");
            }
            str.append("</table>");
            return str.toString();
    }

    private String getFilesInJson() {
        StringBuilder str = new StringBuilder("[");
        File[] files = fetchFiles();
        for (int i = 0; i < files.length; i++) {
            str.append(String.format("\"%s\"",files[i].getName()));
            if(i != files.length - 1)
                str.append(",");
        }
        str.append("]");
        return str.toString();
    }

    public void writeFile(String path, String body) throws IOException {
        File f = new File(FILE_DIR + path);
        synchronized (f.getCanonicalPath().intern()) {
            PrintWriter writer = new PrintWriter(FILE_DIR + path, "UTF-8");
            writer.println(body);
            writer.close();
        }

    }
}
