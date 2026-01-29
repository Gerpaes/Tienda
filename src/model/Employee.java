/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import model.Person;

/**
 *
 * @author perut
 */
public class Employee extends Person {

    private final int employeeld = 123;

    private final String password = "test";

    public Employee(String name) {
        super(name);
    }

    /**
     * Get the value of password
     *
     * @return the value of password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Get the value of employeeld
     *
     * @return the value of employeeld
     */
    public int getEmployeeld() {
        return employeeld;
    }

    @Override
    public String toString() {
        return "Employee: " + employeeld + " password: " + password;
    }

}
