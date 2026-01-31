/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

import model.Amount;

/**
 *
 * @author Estudio-Trabajo
 */
public interface Payable {
    public abstract boolean pay(Amount amount);
}
