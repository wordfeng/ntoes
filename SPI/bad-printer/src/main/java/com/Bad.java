package com;

public class Bad implements Printer {
    @Override
    public void print() {
        System.out.println("Bad printer");
    }
}
