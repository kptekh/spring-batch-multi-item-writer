package com.poc.springbatch.multiitemwriter.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Employee {

  @Id
  private String id;
  private String firstName;
  private String lastName;
  private String city;

  public Employee() {
    super();
  }

  public Employee(String id, String firstName, String lastName, String city) {
    super();
    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
    this.city = city;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  @Override
  public String toString() {
    return "Employee [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", city="
        + city + "]";
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

}
