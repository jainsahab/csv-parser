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
    File aFile = new File("some-file.txt");
    FileOutputStream fos = new FileOutputStream(aFile);
    fos.write(actualFileData.getBytes());

    Reader reader = new Reader();
    String expectedFileContent = reader.readFile(aFile);

    assertThat(expectedFileContent, is(actualFileData));

  }

  @Test
  public void shouldReadFileByOmittingLastNewLineCharacter() throws IOException {
    String expectedFileData = "roll,name,age\n1,Prateek,19";

    String actualFileData = "roll,name,age\n1,Prateek,19\n\n\n\n";
    File aFile = new File("some-file.txt");
    FileOutputStream fos = new FileOutputStream(aFile);
    fos.write(actualFileData.getBytes());

    Reader reader = new Reader();
    String data = reader.readFile(aFile);

    assertThat(data, is(expectedFileData));
  }
}