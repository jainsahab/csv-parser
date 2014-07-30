package com.prateekj.models;

import com.prateekj.annotation.Column;
import com.prateekj.exceptions.IncompatibleFields;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static java.util.Arrays.asList;

public class CVSParser {
  private HashMap<String, Integer> headingsMap;
  private Reader reader;

  public CVSParser() {
    headingsMap = new HashMap<String, Integer>();
    reader = new Reader();
  }

  public <T> List<T> parse(File fileToBeParse, Class<T> dClass) throws IOException, InstantiationException, IllegalAccessException {
    List<String> fileRecords = new ArrayList<String>(asList(reader.readFile(fileToBeParse).split("\n")));
    String[] headings = fileRecords.remove(0).split(",");
    throwExceptionIfColumnsAreNotCompatible(dClass, headings);
    prepareHeadingIndexMap(headings);
    List<T> resultObjects = matchObjects(fileRecords, dClass);
    return resultObjects;
  }

  private <T> List<T> matchObjects(List<String> fileRecords, Class<T> dClass) throws IllegalAccessException, InstantiationException {
    ArrayList<T> recordObject = new ArrayList<T>();
    Field[] requestedObjectFields = dClass.getDeclaredFields();
    for (String record : fileRecords) {
      String[] recordInCell = record.split(",");
      T newInstance = dClass.newInstance();
      assignCellsRecordsToFields(recordInCell, requestedObjectFields, newInstance);
      recordObject.add(newInstance);
    }
    return recordObject;
  }

  private <T> void assignCellsRecordsToFields(String[] recordInCell, Field[] requiredObjectFields, T newInstance) throws IllegalAccessException {
    for (Field objectField : requiredObjectFields) {
      objectField.setAccessible(true);
      String cellValue = recordInCell[headingsMap.get(prepareClassFieldNames(objectField))];
      assignFieldAccordingToType(newInstance, objectField, cellValue);
    }
  }

  private <T> void assignFieldAccordingToType(T result, Field objectField, String cellValue) throws IllegalAccessException {
    if(objectField.getType().getName().equals("int"))
      objectField.set(result, Integer.parseInt(cellValue));
    else
      objectField.set(result, cellValue);
  }

  private  String prepareClassFieldNames(Field field) {
      Column annotation = field.getAnnotation(Column.class);
      return (annotation == null) ?
          field.getName() :
          annotation.name();
  }

  private void prepareHeadingIndexMap(String[] headings) {
    int index = 0;
    for (String heading : headings) {
      headingsMap.put(heading, index);
      index++;
    }
  }

  private <T> void throwExceptionIfColumnsAreNotCompatible(Class<T> dClass, String[] headings) {
    for (Field field : dClass.getDeclaredFields()) {
      Column columnNameAnnotation = field.getAnnotation(Column.class);
      String fieldNameForFile = columnNameAnnotation != null ?
          columnNameAnnotation.name() :
          field.getName();
      if(!asList(headings).contains(fieldNameForFile))
        throw new IncompatibleFields("Fields doesn't match with Columns in File");
    }
  }
}

