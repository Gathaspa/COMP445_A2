package com.comp445.lab2.http;

import com.comp445.lab2.file.server.FileServerHandler;
import com.comp445.lab2.file.server.directoryOutputMethod;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class FileServerHandlerTest {

    @Test
    public void testFetchDirectory_WithEnumPlain_shouldReturnValidString() throws IOException {
        FileServerHandler FSHandler = new FileServerHandler();
        String listOfFiles = FSHandler.fetchDirectory(directoryOutputMethod.Plain);
        assertEquals("files:\ntest2.txt\ntest.txt\n", listOfFiles);
    }

    @Test
    public void testFetchFile_WithValidFile_shouldReturnValidResponse() throws Exception {
        FileServerHandler FSHandler = new FileServerHandler();
        String fileContents = FSHandler.fetchFile("test.txt");
        assertEquals(fileContents, "hey there");
    }

}
