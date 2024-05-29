package org.example.controller;

import org.example.app.LaMeuaExcepcio;
import org.example.model.entities.Peix;
import org.example.model.exceptions.DAOException;
import org.example.model.impls.PeixDAOJDBCOracleImpl;
import org.example.view.Model;
import org.example.view.PeixVista;

import org.example.model.exceptions.DAOException;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class Controller implements PropertyChangeListener {
    private List<Peix> llistaOriginal; // Llista de peixos sense filtre

    public static final String PROP_EXCEPCIO="excepcio";

    private LaMeuaExcepcio excepcio;

    public LaMeuaExcepcio getExcepcio() {
        return excepcio;
    }

    PropertyChangeSupport canvis=new PropertyChangeSupport(this);

    public void setExcepcio(LaMeuaExcepcio excepcio) {
        LaMeuaExcepcio valorVell=this.excepcio;
        this.excepcio = excepcio;
        canvis.firePropertyChange(PROP_EXCEPCIO, valorVell,excepcio);
    }


    //3. Propietat PropertyChangesupport necessària per poder controlar les propietats lligades
    //Culpable de que apareguen dos missatges d'error ja que quan es llança una excepció es crida a aquesta propietat
    //i això fa que es cridi a la propietat de la vista i es mostre el missatge d'error
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        LaMeuaExcepcio rebuda=(LaMeuaExcepcio)evt.getNewValue();

        try {
            throw rebuda;
        } catch (LaMeuaExcepcio e) {
            //Aquí farem ele tractament de les excepcions de l'aplicació
            switch(evt.getPropertyName()){
                case PROP_EXCEPCIO:

                    switch(rebuda.getCodi()){
                        case 1:
                        case 2:
                        case 3:
                        case 4:
                        case 5:
                        case 6:
                        case 7:
                        case 8:
                        case 9:
                        case 10:
                        case 11:
                        case 12:
                        case 13:
                        case 14:
                        case 15:
                        case 16:
                        case 17:
                        case 18:
                        case 19:
                        case 20:
                        case 21:
                        case 22:
                            JOptionPane.showMessageDialog(null, rebuda.missatge());

                            break;
                    }


            }
        }
    }


    private Model model;
    private PeixVista view;
    private boolean desat;
    private boolean modificat = false;
    private boolean existeix;
    private boolean filtratAiguaSalada = false;
    private PeixDAOJDBCOracleImpl dadesPeix;

    public Controller(PeixDAOJDBCOracleImpl dadesPeix, Model model, PeixVista view) {
        this.model = model;
        this.view = view;
        this.dadesPeix = dadesPeix;

        try {
            dadesPeix.crearTaula();

            view.setVisible(true);
        } catch (DAOException e) {
            JOptionPane.showMessageDialog(null, "No s'ha pogut connectar a la base de dades", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
        //Mètode per lligar la vista i el model
        lligarVistaModel();

        //Per a que el controller reaccioni davant de canvis de les priopietats lligades
        canvis.addPropertyChangeListener(this);

    }

    private void assignarCodiListeners() {
        Model modelo=this.model;

        DefaultTableModel modelPeix=modelo.getModelPeix();
        DefaultTableModel modelHab=modelo.getModelHab();
        JButton insertarButton=view.getInsertarButton();
        JButton borrarButton=view.getBorrarButton();
        JButton modificarButton=view.getModificarButton();
        JButton guardarButton=view.getGuardarButton();
        JTabbedPane pestanyes = view.getPestanyes();
        JButton filtrarButton=view.getFiltrarButton();

        JTextField campNomCientific=view.getCampNomCientific();
        JTextField campNomComu=view.getCampNomComu();
        JTextField campMida=view.getCampMida();
        JCheckBox aiguaSalada=view.getAiguaSalada();
        JTextField campNumEx=view.getCampNumEx();

        JTable taulaHab=view.getTaulaHab();
        JTable taula=view.getTaula();

        JButton insertarButtonHab=view.getInsertarButtonHab();
        JButton modificarButtonHab=view.getModificarButtonHab();
        JButton borrarButtonHab=view.getBorrarButtonHab();
        JTextField campProfunditat=view.getCampProfunditat();
        JComboBox comboZone=view.getComboZone();

        insertarButton.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {

                        // Comprovem que totes les caselles continguen informació
                        if (campNomCientific.getText().isBlank() || campNomComu.getText().isBlank() || campMida.getText().isBlank() || campNumEx.getText().isBlank()) {
                            setExcepcio(new LaMeuaExcepcio(6));
                            return;
                        } else {
                            // Verifiquem que el camp Nom Científic no conté números ni caràcters especials
                            String textNomCientific = campNomCientific.getText();
                            if (!textNomCientific.matches("^[a-zA-ZÀ-ÿ\\s]+$")) {
                                setExcepcio(new LaMeuaExcepcio(14));
                                campNomCientific.setSelectionStart(0);
                                campNomCientific.setSelectionEnd(campNomCientific.getText().length());
                                campNomCientific.requestFocus();
                                return;
                            }
                            // Verifiquem que el camp Nom Comú no conté números ni caràcters especials
                            String textNomComu = campNomComu.getText();
                            if (!textNomComu.matches("^[a-zA-ZÀ-ÿ\\s]+$")) {
                                setExcepcio(new LaMeuaExcepcio(15));
                                campNomComu.setSelectionStart(0);
                                campNomComu.setSelectionEnd(campNomComu.getText().length());
                                campNomComu.requestFocus();
                                return;
                            }
                            existeix = false;
                            // Verifiquem si ja existeix un valor similar al camp Nom Científic
                             textNomCientific = campNomCientific.getText();
                            for (int i = 0; i < modelPeix.getRowCount(); i++) {
                                String nomCientificActual = (String) modelPeix.getValueAt(i, 0);
                                if (textNomCientific.equals(nomCientificActual)) {
                                    existeix = true;
                                    break;
                                }
                            }
                        }

                        if (existeix) {
                            setExcepcio(new LaMeuaExcepcio(7));
                        } else {
                            // Obtenim el text introduït al camp mida
                            String textMida = campMida.getText().trim();

                            // Reemplacem les comes per punts (o punts per comes) per permetre ambdós formats
                            textMida = textMida.replaceAll(",", ".");

                            // Comprovem que el textMida és un nombre decimal vàlid en una Expresio regular
                            if (!textMida.matches("\\d+(\\.\\d+)?")) {
                                setExcepcio(new LaMeuaExcepcio(8));
                                campMida.setSelectionStart(0);
                                campMida.setSelectionEnd(campMida.getText().length());
                                campMida.requestFocus();

                            } else{

                                // Convertim el text a un nombre decimal
                                double mida = Double.parseDouble(textMida);

                                // Comprovem que el campNumEx és un enter vàlid
                                String textNumEx = campNumEx.getText();
                                if (!textNumEx.matches("\\d+")) {
                                    setExcepcio(new LaMeuaExcepcio(9));
                                    campNumEx.setSelectionStart(0);
                                    campNumEx.setSelectionEnd(campNumEx.getText().length());
                                    campNumEx.requestFocus();
                                    return;
                                }

                                // Convertim el campNumEx a un enter
                                int numEx = Integer.parseInt(textNumEx);

                                // Comprovem que el valor de numEx sigui vàlid
                                if (mida < 0 || mida > 20) {
                                    setExcepcio(new LaMeuaExcepcio(8));
                                    campMida.setSelectionStart(0);
                                    campMida.setSelectionEnd(campMida.getText().length());
                                    campMida.requestFocus();
                                } else if (numEx < 0) {
                                    setExcepcio(new LaMeuaExcepcio(9));
                                    campNumEx.setSelectionStart(0);
                                    campNumEx.setSelectionEnd(campNumEx.getText().length());
                                    campNumEx.requestFocus();
                                } else {
                                    // Actualitzem el camp amb el valor en el format correcte
                                    campMida.setText(String.format(Locale.getDefault(), "%.2f", mida));

                                    Peix peix = new Peix(campNomCientific.getText(), campNomComu.getText(), mida, numEx, aiguaSalada.isSelected(), new TreeSet<Peix.Habitat>());

                                    //guardem el peix a la BD
                                    try {
                                        new PeixDAOJDBCOracleImpl().save(peix);
                                    } catch (DAOException ex) {
                                        setExcepcio(new LaMeuaExcepcio(1));
                                        e.setSource(5);
                                        System.err.println("Error al guardar el peix a la BD: " + ex.getMessage());
                                    }

                                    modelPeix.addRow(new Object[]{campNomCientific.getText(), campNomComu.getText(), mida, numEx, aiguaSalada.isSelected(), peix});

                                    //Carreguem les dades de la BD
                                    try {
                                        setModelTaulaPeix(model.getModelPeix(),dadesPeix.getAll());
                                    } catch (DAOException ex) {
                                        setExcepcio(new LaMeuaExcepcio(1));
                                    }

                                    campNomCientific.setText("Cyprinus carpio");
                                    campNomCientific.setSelectionStart(0);
                                    campNomCientific.setSelectionEnd(campNomCientific.getText().length());
                                    campNomComu.setText("Carpa Comú");
                                    campNumEx.setText("0");
                                    campMida.setText("0,60");
                                    campNomCientific.requestFocus(); //Intentem que focus vaigue al camp nom





                                    desat=false;
                                    modificat = true;
                                }
                            }

                        }
                    }
                }
        );

        borrarButton.addActionListener(new ActionListener() {
            /**
             *
             * @param e the event to be processed
             */
            @Override
            public void actionPerformed(ActionEvent e) {

                //Miren si tenim la casella seleccionada
                int filaSel=taula.getSelectedRow();
                if(filaSel!=-1){
                    if (modelPeix.getRowCount() > 0 && filaSel >= 0 && filaSel < modelPeix.getRowCount()) {

                        //obtenim el peix seleccionat
                        Peix peix = (Peix) modelPeix.getValueAt(filaSel, 5);
                        try {
                            // Esborrem el peix de la base de dades
                            dadesPeix.delete(peix);
                        } catch (DAOException ex) {

                            System.out.println("Error al borrar el peix de la BD: " + ex.getMessage());
                        }
                        modelPeix.removeRow(filaSel);

                    } else {
                        setExcepcio(new LaMeuaExcepcio(25));
                    }

                    if (modelHab.getRowCount() != 0){
                        if (modelHab.getRowCount() > 0 && filaSel >= 0 && filaSel < modelHab.getRowCount()) {
                            //Eliminim la fila seleccionada
                            modelHab.removeRow(filaSel);

                        } else {
                            setExcepcio(new LaMeuaExcepcio(25));
                        }
                    } else {
                        setExcepcio(new LaMeuaExcepcio(25));
                    }


                    modelHab.setRowCount(0);
                    //descaivem la pestanya2
                    pestanyes.setEnabledAt(1, false);
                    pestanyes.setTitleAt(1, "Hàbitat de ...");

                    // Posem els camps de text en blanc
                    campNomCientific.setText("");
                    campNomComu.setText("");
                    campMida.setText("");
                    campNumEx.setText("");


                    desat=false;
                    modificat = true;

                }
                else setExcepcio(new LaMeuaExcepcio(10));
            }
        });

        modificarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int filaSel = taula.getSelectedRow();
                if (filaSel != -1) {
                    if (campNomCientific.getText().isBlank() || campNomComu.getText().isBlank() || campMida.getText().isBlank() || campNumEx.getText().isBlank()) {
                        setExcepcio(new LaMeuaExcepcio(6));
                        return;
                    } else {
                        // Verifiquem que el camp Nom Científic no conté números ni caràcters especials
                        String textNomCientific = campNomCientific.getText();
                        if (!textNomCientific.matches("^[a-zA-ZÀ-ÿ\\s]+$")) {
                            setExcepcio(new LaMeuaExcepcio(14));
                            campNomCientific.setSelectionStart(0);
                            campNomCientific.setSelectionEnd(campNomCientific.getText().length());
                            campNomCientific.requestFocus();
                            return;
                        }
                        // Verifiquem que el camp Nom Comú no conté números ni caràcters especials
                        String textNomComu = campNomComu.getText();
                        if (!textNomComu.matches("^[a-zA-ZÀ-ÿ\\s]+$")) {
                            setExcepcio(new LaMeuaExcepcio(15));
                            campNomComu.setSelectionStart(0);
                            campNomComu.setSelectionEnd(campNomComu.getText().length());
                            campNomComu.requestFocus();
                            return;
                        }
                        boolean existeix = false;

                         textNomCientific = campNomCientific.getText();
                        for (int i = 0; i < modelPeix.getRowCount(); i++) {
                            if (i != filaSel) {
                                String nomCientificAltresFiles = (String) modelPeix.getValueAt(i, 0);
                                if (textNomCientific.equals(nomCientificAltresFiles)) {
                                    existeix = true;
                                    break;
                                }
                            }
                        }
                        if (existeix) {
                            setExcepcio(new LaMeuaExcepcio(7));
                        } else {
                            String textMida = campMida.getText().trim().replaceAll(",", ".");
                            String textNumEx = campNumEx.getText();

                            // Comprovem que el textMida és un nombre decimal vàlid
                            if (!textMida.matches("\\d+(\\.\\d+)?")) {
                                setExcepcio(new LaMeuaExcepcio(8));
                                campMida.setSelectionStart(0);
                                campMida.setSelectionEnd(campMida.getText().length());
                                campMida.requestFocus();
                                return;
                            } else if (!textNumEx.matches("\\d+")){
                                setExcepcio(new LaMeuaExcepcio(9));
                                campNumEx.setSelectionStart(0);
                                campNumEx.setSelectionEnd(campNumEx.getText().length());
                                campNumEx.requestFocus();
                                return;
                            } else {
                                double mida = Double.parseDouble(textMida);
                                int numEx = Integer.parseInt(textNumEx);

                                if (mida < 0 || mida > 20) {
                                    setExcepcio(new LaMeuaExcepcio(8));
                                    campMida.setSelectionStart(0);
                                    campMida.setSelectionEnd(campMida.getText().length());
                                    campMida.requestFocus();
                                } else if (numEx < 0) {
                                    setExcepcio(new LaMeuaExcepcio(9));
                                    campNumEx.setSelectionStart(0);
                                    campNumEx.setSelectionEnd(campNumEx.getText().length());
                                    campNumEx.requestFocus();
                                } else {
                                    try {
                                        // Obtenim el peix existent
                                        Peix peixExist = (Peix) modelPeix.getValueAt(filaSel, 5);
                                        // Creem un nou peix amb les dades modificades i l'ID que ja existeix a la taula
                                        Peix peixNou = new Peix(
                                                peixExist.getId(),
                                                campNomCientific.getText(),
                                                campNomComu.getText(),
                                                mida,
                                                numEx,
                                                aiguaSalada.isSelected(),
                                                new TreeSet<Peix.Habitat>()
                                        );

                                        // Actualitzar el peix a la BD
                                        new PeixDAOJDBCOracleImpl().update(peixNou);

                                        // Actualitzar la fila a la taula
                                        modelPeix.removeRow(filaSel);
                                        modelPeix.insertRow(filaSel, new Object[]{campNomCientific.getText(), campNomComu.getText(), mida, numEx, aiguaSalada.isSelected(), peixNou});

                                        //Ficar en default els camps del formulari
                                        campNomCientific.setText("");
                                        campNomComu.setText("");
                                        campMida.setText("");
                                        campNumEx.setText("");
                                        campNomCientific.requestFocus();
                                        desat = false;
                                        modificat = true;
                                    } catch (DAOException ex) {
                                        setExcepcio(new LaMeuaExcepcio(1));
                                        System.err.println("Error al guardar el peix a la BD: " + ex.getMessage());
                                    }

                                }
                            }
                        }
                    }
                } else {
                    setExcepcio(new LaMeuaExcepcio(11));
                }
            }
        });

        guardarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    //Fitxers.guardarDades(modelPeix, modelHab);
                    JOptionPane.showMessageDialog(null, "Dades guardades correctament");
                } catch (Exception ex) {
                    setExcepcio(new LaMeuaExcepcio(2));
                }
            }
        });

        taula.addMouseListener(new MouseAdapter() {
            /**
             * {@inheritDoc}
             *
             * @param e
             */
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                //Al seleccionar la taula omplim els camps de text en els valors de la fila seleccionada
                int filaSel=taula.getSelectedRow();

                if(filaSel!=-1){        //Tenim una fila seleccionada
                    //Posem els valors de la fila seleccionada als camps respectius
                    campNomCientific.setText(modelPeix.getValueAt(filaSel,0).toString());
                    campNomComu.setText(modelPeix.getValueAt(filaSel,1).toString());
                    campMida.setText(modelPeix.getValueAt(filaSel,2).toString());
                    campNumEx.setText(modelPeix.getValueAt(filaSel,3).toString());
                    aiguaSalada.setSelected((Boolean)modelPeix.getValueAt(filaSel,4));

                    //Activem la pestanya del habitat del peix seleccionat
                    view.getPestanyes().setEnabledAt(1, true);
                    view.getPestanyes().setTitleAt(1, "Hàbitat de " + campNomCientific.getText());

                    //Omplim el combo amb les zones
                    view.getComboZone().setModel(modelo.getComboBoxModel());
//                    ompliHabitat((Peix) modelPeix.getValueAt(filaSel, 5), modelHab);
                }else{                  //Hem deseleccionat una fila
                    //Posem els camps de text en blanc
                    campNomCientific.setText("");
                    campNomComu.setText("");
                    campMida.setText("");
                    campNumEx.setText("");

                    //Desactivem la pestanya del habitat del peix seleccionat
                    view.getPestanyes().setEnabledAt(1, false);
                    view.getPestanyes().setTitleAt(1, "Hàbitat de ...");
                }
            }
        });

        taulaHab.addMouseListener(new MouseAdapter() {
            /**
             * {@inheritDoc}
             *
             * @param e
             */
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                //Al seleccionar la taula omplim els camps de text en els valors de la fila seleccionada
                int filaSel=taulaHab.getSelectedRow();

                if(filaSel!=-1){        //Tenim una fila seleccionada
                    //Posem els valors de la fila seleccionada als camps respectiu
                    comboZone.setSelectedItem(modelHab.getValueAt(filaSel,0)); // Omple el camp combozone
                    campProfunditat.setText(modelHab.getValueAt(filaSel,1).toString());

                }else{                  //Hem deseleccionat una fila
                    //Posem els camps de text en blanc
                    campProfunditat.setText("");

                }
            }
        });


        insertarButtonHab.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int filaSel = taula.getSelectedRow();
                if (filaSel != -1) {
                    Peix peix = (Peix) modelPeix.getValueAt(filaSel, 5);
                    if (peix == null) {
                        setExcepcio(new LaMeuaExcepcio(13)); // Excepció si no s'ha seleccionat cap peix
                        return;
                    }

                    // Validem que s'ha introduït una profunditat vàlida
                    String textProfunditat = view.getCampProfunditat().getText();
                    if (textProfunditat.isBlank() || !textProfunditat.matches("\\d+")) {
                        setExcepcio(new LaMeuaExcepcio(12)); // Excepció si la profunditat no és un enter vàlid
                        view.getCampProfunditat().setSelectionStart(0);
                        view.getCampProfunditat().setSelectionEnd(view.getCampProfunditat().getText().length());
                        view.getCampProfunditat().requestFocus();
                        return;
                    }

                    int profunditat = Integer.parseInt(textProfunditat);

//                    // Afegim l'habitat al peix seleccionat i actualitzem la taula d'habitats
//                    Peix.Habitat habitat = new Peix.Habitat((Peix.Habitat.Regio) view.getComboZone().getSelectedItem(), profunditat);
//                    peix.getHabitats().add(habitat);
//                    ompliHabitat(peix, modelHab);

                    // Reiniciem el camp de profunditat
                    view.getCampProfunditat().setText("");
                    view.getCampProfunditat().requestFocus(); //Intentem que el focus vagi al camp de profunditat
                }
            }
        });

        modificarButtonHab.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int filaSel = taula.getSelectedRow();
                if (filaSel != -1) {
                    Peix peix = (Peix) modelPeix.getValueAt(filaSel, 5);
                    if (peix == null) {
                        setExcepcio(new LaMeuaExcepcio(13)); // Excepció si no s'ha seleccionat cap peix
                        return;
                    }

                    int filaHabSel = taulaHab.getSelectedRow();
                    if (filaHabSel != -1) {
                        List<Peix.Habitat> habitatsList = new ArrayList<>(peix.getHabitats());

                        Peix.Habitat habitatSel = null;
                        if (filaHabSel < habitatsList.size()) {
                            habitatSel = habitatsList.get(filaHabSel);
                        } else {
                            setExcepcio(new LaMeuaExcepcio(22)); // Excepció si l'índex de fila seleccionat està fora dels límits
                            return;
                        }

                        Peix.Habitat.Regio novaRegio = (Peix.Habitat.Regio) view.getComboZone().getSelectedItem();
                        String textNovaProfunditat = view.getCampProfunditat().getText();

                        if (textNovaProfunditat.isBlank() || !textNovaProfunditat.matches("\\d+")) {
                            setExcepcio(new LaMeuaExcepcio(12)); // Excepció si la profunditat no és un enter vàlid
                            view.getCampProfunditat().setSelectionStart(0);
                            view.getCampProfunditat().setSelectionEnd(view.getCampProfunditat().getText().length());
                            view.getCampProfunditat().requestFocus();
                            return;
                        }

                        int novaProfunditat = Integer.parseInt(textNovaProfunditat);

                        habitatSel.setRegio(novaRegio);
                        habitatSel.setProfunditat(novaProfunditat);

//                        ompliHabitat(peix, modelHab);

                        view.getCampProfunditat().setText("");
                        view.getCampProfunditat().requestFocus(); //Intentem que el focus vagi al camp de profunditat
                    } else {
                        setExcepcio(new LaMeuaExcepcio(11)); // Excepció si no s'ha seleccionat cap habitat
                    }
                }
            }
        });

        borrarButtonHab.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int filaSelHab = taulaHab.getSelectedRow();
                int filaSelPeix = taula.getSelectedRow(); // Obtenim la fila seleccionada de la taula de peixos
                if (filaSelHab != -1 && filaSelPeix != -1) { // Comprovem que ambdues files estan seleccionades
                    Peix peix = (Peix) modelPeix.getValueAt(filaSelPeix, 5); // Utilitzem l'índex de la fila de la taula de peixos
                    if (peix == null) {
                        setExcepcio(new LaMeuaExcepcio(13)); // Excepció si no s'ha seleccionat cap peix
                        return;
                    }

                    // Convertim la col·lecció d'habitats a una llista
                    List<Peix.Habitat> habitatsList = new ArrayList<>(peix.getHabitats());

                    // Verifiquem si l'índex de la fila seleccionada està dins els límits de la llista
                    if (filaSelHab < habitatsList.size()) {
                        // Eliminem l'habitat seleccionat de la llista
                        habitatsList.remove(filaSelHab);

                        // Actualitzem la col·lecció d'habitats del peix amb la nova llista
                        peix.setHabitats(habitatsList);

                        // Actualitzem la taula d'habitats
//                        ompliHabitat(peix, modelHab);
                    } else {
                        setExcepcio(new LaMeuaExcepcio(11)); // Excepció si index de fila seleccionat està fora dels límits
                    }
                } else {
                    setExcepcio(new LaMeuaExcepcio(11)); // Excepció si no hi ha fila seleccionada
                }
            }
        });

        //Plenem la llistaOriginal amb els peixos de la taula
        llistaOriginal = new ArrayList<>();
        for (int i = 0; i < modelPeix.getRowCount(); i++) {
            Peix peix = (Peix) modelPeix.getValueAt(i, 5); //Obtenim l'objecte peix de la columna 5
            llistaOriginal.add(peix);
        }

        filtrarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!filtratAiguaSalada) {
                    // Guardar les dades originals abans d'aplicar el filtre
                    llistaOriginal = new ArrayList<>();
                    for (int i = 0; i < modelPeix.getRowCount(); i++) {
                        Peix peix = (Peix) modelPeix.getValueAt(i, 5);
                        llistaOriginal.add(peix);
                    }
                    List<Peix> peixosAiguaSalada = filtraPerAiguaSalada();
                    actualitzaTaula(peixosAiguaSalada);
                    filtratAiguaSalada = true;
                } else {
                    // Restaurar les dades originals quan es desfa el filtre
                    actualitzaTaula(llistaOriginal);
                    filtratAiguaSalada = false;
                }
            }
        });

    }



//    private static void ompliHabitat(Peix peix,DefaultTableModel modelHab){
//        //Omplim el model de la taula dels habitats del peix seleccionat
//        modelHab.setRowCount(0);
//
//        if (peix != null) {
//            for (Peix.Habitat habitat : peix.getHabitats()) {
//                modelHab.addRow(new Object[]{habitat.getRegio(), habitat.getProfunditat()});
//            }
//        } else {
////             setExcepcio(new LaMeuaExcepcio(10));
//           System.out.println("Ha Petat, Peix es Null");
//
//        }
//    }

    private void setModelTaulaPeix(DefaultTableModel model, List<Peix> all) {
        model.setRowCount(0);
        for (Peix peix : all) {
            model.addRow(new Object[]{peix.getCampNomCientific(), peix.getCampNomComu(), peix.getCampMida(), peix.getCampNumEx(), peix.isAiguaSalada(), peix});
        }
    }
    private void lligarVistaModel() {

        //Carreguem la taula d'alumnes en les dades de la BD

        try {
           setModelTaulaPeix(model.getModelPeix(),dadesPeix.getAll());
        } catch (DAOException e) {
            this.setExcepcio(new LaMeuaExcepcio(1));
        }

        //Fixem el model de la taula dels peixos
        JTable taula = view.getTaula();
        taula.setModel(this.model.getModelPeix());
        //Amago la columna que conté l'objecte peix
        taula.getColumnModel().getColumn(5).setMinWidth(0);
        taula.getColumnModel().getColumn(5).setMaxWidth(0);
        taula.getColumnModel().getColumn(5).setPreferredWidth(0);

        //Fixem el model de la taula dels Hàbitats
        JTable taulaHab = view.getTaulaHab();
        taulaHab.setModel(this.model.getModelHab());

        //Desactivem pestanyes del panel
        view.getPestanyes().setEnabledAt(1, false);
        view.getPestanyes().setTitleAt(1, "Hàbitat de ...");

        //Forcem a que només se pugue seleccionar una fila de la taula
        taula.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        //Forcem a que només se pugue seleccionar una fila de la taula
        taulaHab.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        //Afegim listeners als objectes
        assignarCodiListeners();

        //Per a que el controller reaccioni davant de canvis de les priopietats lligades
        canvis.addPropertyChangeListener(this);


    }


    //Mètode Stream per filtrar els peixos d'aigua salada
    public List<Peix> filtraPerAiguaSalada() {
        // Obtenim el model de la taula
         DefaultTableModel modelPeix = model.getModelPeix();

        // Creem una llista buida per a guardar els peixos
        List<Peix> peixos = new ArrayList<>();

        // Recorrem totes les files del model de la taula
        for (int i = 0; i < modelPeix.getRowCount(); i++) {
            // Obtenim el peix de la columna 5 on esta el objecte peix
            Peix peix = (Peix) modelPeix.getValueAt(i, 5);
            // Afegim el peix a la llista
            peixos.add(peix);
        }

        // Utilitzem el mètode stream() per a filtrar els peixos d'aigua salada
        List<Peix> peixosAiguaSalada = peixos.stream()
                .filter(Peix::isAiguaSalada)
                .collect(Collectors.toList());

        return peixosAiguaSalada;
    }

    //Mètode per actualitzar la taula de peixos quan estiguin filtrats
    public void actualitzaTaula(List<Peix> peixos) {
        // Obtenim el model de la taula
        DefaultTableModel modelPeix = model.getModelPeix();

        // Eliminem totes les files existents
        modelPeix.setRowCount(0);

        // Afegim les files de la llista de peixos
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



}
