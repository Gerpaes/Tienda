/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author perut
 */
public class Amount {
   private double value;
    private String currency = String.valueOf('\u20ac'); 
    
    public Amount(double value){
        this.value = value;
        this.currency = String.valueOf('\u20ac');
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
  public String toString() {
    return String.format("%.2f ", value) + currency;
}
}
