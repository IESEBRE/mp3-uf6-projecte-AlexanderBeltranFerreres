package org.example.app;

import org.example.controller.*;
import org.example.model.impls.PeixDAOJDBCOracleImpl;
import org.example.view.*;
import org.example.view.PeixVista;

import javax.swing.*;
import java.util.Locale;

public class Main {
    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {

                //Definim la cultura de la nostra aplicació
                Locale.setDefault(new Locale("ca","ES"));

                new Controller(new PeixDAOJDBCOracleImpl(),new Model(), new PeixVista());

            }
        });
    }

}