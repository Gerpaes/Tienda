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
public class Client extends Person {

    private final int memberld = 456;

    private final Amount balance = new Amount(50.00);

    public Client(String name) {
        super(name);
    }

    /**
     * Get the value of balance
     *
     * @return the value of balance
     */
    public Amount getBalance() {
        return balance;
    }

    /**
     * Get the value of memberld
     *
     * @return the value of memberld
     */
    public int getMemberld() {
        return memberld;
    }

    @Override
    public String toString() {
        return "Client: " + memberld + ", balance: " + balance;
    }

}
