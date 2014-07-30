package com.prateekj.models;

import com.prateekj.exceptions.IncompatibleFields;
import com.prateekj.helperModels.Person;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class CVSParserTest {
  @Test
  public void shouldParseFileIntoObject() throws IOException, IllegalAccessException, InstantiationException {
    String actualFileData = "roll_no,name,age\n1,Prateek,19\n2,Prayas,15";
    File aFile = new File("some-file.txt");
    FileOutputStream fos = new FileOutputStream(aFile);
    fos.write(actualFileData.getBytes());

    List<Person> persons = new CVSParser().parse(aFile, Person.class);

    assertThat(persons, hasSize(2));
    assertThat(persons.get(0), is(new Person().withRoll(1).withName("Prateek").withAge(19)));
    assertThat(persons.get(1), is(new Person().withRoll(2).withName("Prayas").withAge(15)));
  }

  @Test(expected = IncompatibleFields.class)
  public void shouldThrowExceptionWithIncompatibleColumns() throws IOException, IllegalAccessException, InstantiationException {
    String actualFileData = "roll_no,name,class\n1,Prateek,5\n2,Prayas,2";
    File aFile = new File("some-file.txt");
    FileOutputStream fos = new FileOutputStream(aFile);
    fos.write(actualFileData.getBytes());

    new CVSParser().parse(aFile, Person.class);
  }

}