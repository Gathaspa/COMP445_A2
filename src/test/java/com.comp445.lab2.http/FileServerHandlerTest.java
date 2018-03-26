package com.comp445.lab2.http;

import com.comp445.lab2.file.server.FileServerHandler;
import com.comp445.lab2.file.server.DirectoryOutputMethod;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class FileServerHandlerTest {

    @Test
    public void testFetchDirectory_WithEnumPlain_shouldReturnValidString() throws IOException {
        FileServerHandler FSHandler = new FileServerHandler();
        String listOfFiles = FSHandler.fetchDirectory(DirectoryOutputMethod.Plain);
        assertEquals("files:\ntest1.txt\ntest2.txt\ndont_change_me_test_file.txt\n", listOfFiles);
    }

    @Test
    public void testFetchFile_WithValidFile_shouldReturnValidResponse() throws Exception {
        FileServerHandler FSHandler = new FileServerHandler();
        String fileContents = FSHandler.fetchFile("dont_change_me_test_file.txt");
        assertEquals("test data", fileContents);
    }

    @Test(expected = FileNotFoundException.class)
    public void testFetchFile_WithNonexistentFile_shouldThrow() throws Exception {
        FileServerHandler FSHandler = new FileServerHandler();
        FSHandler.fetchFile("DOES_NOT_EXIST");
    }
}
