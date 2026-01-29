/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import main.Logable;


/**
 *
 * @author perut
 */
public class Employee extends Person implements Logable{

    private final int employeeId = 123;

    private final String passWord = "test";

    public Employee(String name) {
        super(name);
    }

    /**
     * Get the value of password
     *
     * @return the value of password
     */
    public String getPassword() {
        return passWord;
    }

    /**
     * Get the value of employeeld
     *
     * @return the value of employeeld
     */
    public int getEmployeeld() {
        return employeeId;
    }

    @Override
    public String toString() {
        return "Employee: " + employeeId + " password: " + passWord;
    }

    @Override
    public boolean login(int user, String password) {
                   
            
        if(user != employeeId || !password.equalsIgnoreCase(passWord)){
            
            return false;
            
        }else{
            
            
            return true;
        }
        
    }

}
