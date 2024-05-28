package org.example.view;

import org.example.model.entities.Peix;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.util.List;

public class Model {

    private DefaultTableModel modelPeix;
    private DefaultTableModel modelHab;
    private TableColumn objecte;
    //private DefaultTableModel modelPeix;

    private ComboBoxModel<Peix.Habitat.Regio> comboBoxModel;

    //Getters dels objectes del model

    public ComboBoxModel<Peix.Habitat.Regio> getComboBoxModel() {
        return comboBoxModel;
    }

    public DefaultTableModel getModelPeix() {
        return modelPeix;
    }
    public DefaultTableModel getModelHab() {
        return modelHab;
    }

    public void setModelPeix(List<Peix> peixos) {
        // Limpiar el modelo antes de agregar los datos
        modelPeix.setRowCount(0);

        // Agregar cada Peix a la tabla
        for (Peix peix : peixos) {
            modelPeix.addRow(new Object[]{
                    peix.getCampNomCientific(),
                    peix.getCampNomComu(),
                    peix.getCampMida(),
                    peix.getCampNumEx(),
                    peix.isAiguaSalada(),
                    peix
            });
        }
    }

    public Model() {

        //Definim l'estructura de la taula dels peixos
        modelPeix = new DefaultTableModel(new Object[]{"Nom Científic","Nom Comú","Mida (metres)", "Número Exemplars", "Aigua Salada?","Object"},0){
            @Override
            public boolean isCellEditable(int row, int column) {
                //fem que totes les cel·les siguin no editables
                return false;
            }
            //Definim el tipus de cada columna
            @Override
            public Class getColumnClass(int column){
                switch (column){
                    case 0:
                        return String.class;
                    case 1:
                        return String.class;
                    case 2:
                        return Double.class;
                    case 3:
                        return Integer.class;
                    case 4:
                        return Boolean.class;
                    case 5:
                        return Peix.class;
                    default:
                        return Object.class;
                }
            }
        };
        //Definim l'estructura de la taula dels habitats
        modelHab = new DefaultTableModel(new Object[]{"Zona","Profunditat (metres)"},0){
            @Override
            public boolean isCellEditable(int row, int column) {
                //fem que totes les cel·les siguin no editables
                return false;
            }
            //Definim el tipus de cada columna

            @Override
            public Class getColumnClass(int column) {
                switch (column) {
                    case 0:
                        return Peix.Habitat.Regio.class;
                    case 1:
                        return Integer.class;
                    default:
                        return Object.class;
                }
            }
        };



        //Definim el model de la comboBox
        comboBoxModel = new DefaultComboBoxModel<>(Peix.Habitat.Regio.values());
    }

    public DefaultTableModel getModel() {
        return modelPeix;
    }
}
