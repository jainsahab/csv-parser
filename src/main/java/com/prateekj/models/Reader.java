package com.prateekj.models;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Reader {
  private File file;

  public Reader(File file) {
    this.file = file;
  }

  public String readFile() throws IOException {
    BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
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
    deleteLastNewLineCharacter(sb);
    return sb.toString();
  }

  private void deleteLastNewLineCharacter(StringBuilder sb) {
    if(sb.toString().endsWith("\n"))
      sb.deleteCharAt(sb.length()-1);
  }


}
