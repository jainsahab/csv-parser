package com.prateekj.helperModels;

import com.prateekj.annotation.Column;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class Person {
  @Column(name = "roll_no")
  private int roll;

  @Column(name = "name")
  private String name;

  @Column(name = "age")
  private Integer age;

  public Person withRoll(int rollNo) {
    this.roll = rollNo;
    return this;
  }

  public Person withName(String name) {
    this.name = name;
    return this;
  }

  public Person withAge(int age) {
    this.age = age;
    return this;
  }
}
