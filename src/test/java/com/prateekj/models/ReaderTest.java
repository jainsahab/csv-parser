package com.prateekj.models;

import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class ReaderTest {

  @Test
  public void shouldReadFile() throws IOException {
    String actualFileData = "roll,name,age\n1,Prateek,19";
    File aFile = createFile("some-file.txt", actualFileData);

    Reader reader = new Reader();
    String expectedFileContent = reader.readFile(aFile);

    assertThat(expectedFileContent, is(actualFileData));

  }

  @Test
  public void shouldReadFileByOmittingLastNewLineCharacter() throws IOException {
    String expectedFileData = "roll,name,age\n1,Prateek,19";

    String actualFileData = "roll,name,age\n1,Prateek,19\n\n\n\n";
    File aFile = createFile("some-file.txt", actualFileData);

    Reader reader = new Reader();
    String data = reader.readFile(aFile);

    assertThat(data, is(expectedFileData));
  }

  private File createFile(String fileName, String actualFileData) throws IOException {
    File aFile = new File(fileName);
    FileOutputStream fos = new FileOutputStream(aFile);
    fos.write(actualFileData.getBytes());
    return aFile;
  }
}