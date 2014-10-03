package com.prateekj.models;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Reader {

  public String readFile(File aFile) throws IOException {
    BufferedReader bufferedReader = new BufferedReader(new FileReader(aFile));
    StringBuilder sb = new StringBuilder();
    boolean fileEnd = false;

    while (!fileEnd) {
      String line = bufferedReader.readLine();
      if (line != null) {
        sb.append(line);
        sb.append("\n");
      } else
        fileEnd = true;
    }
    deleteLastNewLineCharacters(sb);
    return sb.toString();
  }

  private void deleteLastNewLineCharacters(StringBuilder sb) {
    if(!sb.toString().endsWith("\n")) return;
    deleteLastNewLineCharacters(sb.deleteCharAt(sb.length() - 1));
  }
}
