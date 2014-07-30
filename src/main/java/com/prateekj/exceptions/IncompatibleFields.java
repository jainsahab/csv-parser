package com.prateekj.exceptions;

public class IncompatibleFields extends RuntimeException{
  public IncompatibleFields(String message) {
    super(message);
  }
}
