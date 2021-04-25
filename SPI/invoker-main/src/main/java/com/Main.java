package com;

import java.io.IOException;
import java.util.Properties;
import java.util.ServiceLoader;

@SuppressWarnings("all")
public class Main {

    private Printer printer;

    public static void main(String[] args) throws IOException {
        new Main().init().print();
    }

    public Main init() throws IOException {
        Properties properties = new Properties();
        properties.load(Main.class.getClassLoader().getResource("app.properties").openStream());

        String configuedDriver = properties.getProperty("print-driver");
        ServiceLoader<Printer> printers = ServiceLoader.load(Printer.class);
        for (Printer printer : printers) {
            if (printer.getClass().getName().equals(configuedDriver)) {
                this.printer = printer;
            }
        }
        return this;
    }

    public void print(){
        this.printer.print();
    }
}
