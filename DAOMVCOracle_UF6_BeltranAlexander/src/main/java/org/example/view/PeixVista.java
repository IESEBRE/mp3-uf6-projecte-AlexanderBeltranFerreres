package org.example.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class PeixVista extends JFrame {
    private JPanel panel1;
    private JTabbedPane pestanyes;
    private JTabbedPane tabbedPane2;
    private JPanel panel;
    private JTable taula;
    private JTextField campNomCientific;
    private JTextField campNomComu;
    private JCheckBox aiguaSalada;
    private JTextField campMida;
    private JTextField campNumEx;
    private JButton borrarButton;
    private JButton modificarButton;
    private JButton insertarButton;
    private JButton guardarButton;
    private JPanel Peix;
    private JPanel Habitat;
    private JTable taulaHab;
    private JComboBox comboZone;
    private JTextField campProfunditat;
    private JTextField campZona;
    private JScrollPane scrollPane1;
    private JButton insertarButtonHab;
    private JButton borrarButtonHab;
    private JButton modificarButtonHab;
    private JButton filtrarButton;

    private boolean desat;
    private boolean modificat = false;
    private boolean existeix;

    //Getters dels objectes de la vista
    public JTable getTaula() {
        return taula;
    }


public JTable getTaulaHab() {
    return taulaHab;
}


    public JButton getInsertarButton() {
        return insertarButton;
    }

    public JTextField getCampNomCientific() {
        return campNomCientific;
    }

    public JTextField getCampNomComu() {
        return campNomComu;
    }

    public JTextField getCampMida() {
        return campMida;
    }

    public JTextField getCampNumEx() {
        return campNumEx;
    }

    public JButton getGuardarButton() {
        return guardarButton;
    }

    public JCheckBox getAiguaSalada() {
        return aiguaSalada;
    }

    public JButton getBorrarButton() {
        return borrarButton;
    }

    public JButton getModificarButton() {
        return modificarButton;
    }

    public JTabbedPane getPestanyes() {
        return pestanyes;
    }

    public JPanel getPanel1() {
        return panel1;
    }

    public JTabbedPane getTabbedPane2() {
        return tabbedPane2;
    }

    public JButton getBorrarButtonHab() {
        return borrarButtonHab;
    }

    public JButton getModificarButtonHab() {
        return modificarButtonHab;
    }

    public JButton getInsertarButtonHab() {
        return insertarButtonHab;
    }
        public JPanel getPeix() {
        return Peix;
    }

    public JPanel getHabitat() {
        return Habitat;
    }

    public JComboBox getComboZone() {
        return comboZone;
    }

    public JTextField getCampProfunditat() {
        return campProfunditat;
    }

    public JTextField getCampZona() {
        return campZona;
    }

    public JButton getFiltrarButton() {
        return filtrarButton;
    }

    //Constructor de la classe
    public PeixVista(){
        //Per poder vore la finestra
        this.setContentPane(panel1);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setVisible(false);
        panel1.setLayout(new BorderLayout());

        panel1.add(pestanyes, BorderLayout.CENTER);

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);

                // Preguntem si vol guardar les dades al sortir en el cas de que no ho hague fet
                if (!desat && modificat) { // Si els canvis no s'han guardat
                    int opcio = JOptionPane.showConfirmDialog(null, "Vols desar els canvis abans de sortir?", "Desar Canvis", JOptionPane.YES_NO_OPTION);
                    if (opcio == JOptionPane.YES_OPTION) {
                      // guardarDades(modelPeix, modelHab); // Guarda les dades
                    } else if (opcio == JOptionPane.NO_OPTION) {
                        System.exit(0); // Tanca l'aplicació
                    } else {
                        dispose();
                    }
                }
                dispose();
            }
        });
    }

    private void createUIComponents() {

        scrollPane1 = new JScrollPane();
        taula = new JTable();
        pestanyes = new JTabbedPane();
        taula.setModel(new DefaultTableModel());
        taula.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        scrollPane1.setViewportView(taula);

        panel1.setLayout(new BorderLayout());

        panel1.add(pestanyes, BorderLayout.CENTER);

    }

}