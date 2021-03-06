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

public class CSVParser {
  private Reader reader;

  public CSVParser() {
    reader = new Reader();
  }

  public <T> List<T> parse(File fileToBeParse, Class<T> dClass) throws IOException, InstantiationException, IllegalAccessException {
    List<String> fileRecords = new ArrayList<String>(asList(reader.readFile(fileToBeParse).split("\n")));
    String[] headings = fileRecords.remove(0).split(",");
    throwExceptionIfColumnsAreNotCompatible(dClass, headings);
    HashMap<String, Integer> headingIndexMap = prepareHeadingIndexMap(headings);
    List<T> resultObjects = matchObjects(fileRecords, dClass, headingIndexMap);
    return resultObjects;
  }

  private <T> List<T> matchObjects(List<String> fileRecords, Class<T> dClass, HashMap<String, Integer> headingIndexMap) throws InstantiationException, IllegalAccessException {
    ArrayList<T> recordObject = new ArrayList<T>();
    for (String record : fileRecords) {
      String[] dataFields = record.split(",");
      T newInstance = assignCellsRecordsToFields(dataFields, headingIndexMap, dClass);
      recordObject.add(newInstance);
    }
    return recordObject;
  }

  private <T> T assignCellsRecordsToFields(String[] dataFields, HashMap<String, Integer> headingsMap, Class<T> dClass) throws IllegalAccessException, InstantiationException {
    T newInstance = dClass.newInstance();
    for (Field objectField : newInstance.getClass().getDeclaredFields()) {
      objectField.setAccessible(true);
      Integer fieldIndex = headingsMap.get(prepareClassFieldNames(objectField));
      String cellValue = dataFields[fieldIndex];
      assignFieldAccordingToType(newInstance, objectField, cellValue);
    }
    return newInstance;
  }

  private <T> void assignFieldAccordingToType(T result, Field objectField, String cellValue) throws IllegalAccessException {
    if(objectField.getType().getName().toLowerCase().contains("int"))
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

  private HashMap<String, Integer> prepareHeadingIndexMap(String[] headings) {
    HashMap<String, Integer> headingsMap = new HashMap<String, Integer>();
    int index = 0;
    for (String heading : headings) {
      headingsMap.put(heading, index);
      index++;
    }
    return headingsMap;
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

