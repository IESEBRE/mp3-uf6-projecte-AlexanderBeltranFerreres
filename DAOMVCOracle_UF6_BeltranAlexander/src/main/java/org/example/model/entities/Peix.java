package org.example.model.entities;

import java.io.Serializable;
import java.util.Collection;

public class Peix implements Serializable {

    private String campNomCientific;
    private String campNomComu;
    private double campMida;
    private boolean aiguaSalada;
    private int campNumEx;
    private Long id;

    private Collection<Habitat> habitats;

    public Peix(){}

    public Peix(String campNomCientific, String campNomComu, double campMida, int campNumEx, boolean aiguaSalada, Collection<Habitat> habitats) {
        this.campNomCientific = campNomCientific;
        this.campNomComu = campNomComu;
        this.campMida = campMida;
        this.aiguaSalada = aiguaSalada;
        this.campNumEx = campNumEx;
        this.habitats = habitats;
    }

    public Peix (Long id, String campNomCientific, String campNomComu){
        this.id = id;
        this.campNomCientific = campNomCientific;
        this.campNomComu = campNomComu;
    }

    public Peix (long id, String campNomCientific, String campNomComu, double campMida, int campNumEx){
        this.id = id;
        this.campNomCientific = campNomCientific;
        this.campNomComu = campNomComu;
        this.campMida = campMida;
        this.campNumEx = campNumEx;
    }

    public Peix (long id, String campNomCientific, String campNomComu, double campMida, int campNumEx, boolean aiguaSalada){
        this.id = id;
        this.campNomCientific = campNomCientific;
        this.campNomComu = campNomComu;
        this.campMida = campMida;
        this.campNumEx = campNumEx;
        this.aiguaSalada = aiguaSalada;
    }

    public Peix (long id, String campNomCientific, String campNomComu, double campMida, int campNumEx, boolean aiguaSalada, Collection<Habitat> habitats){
        this.id = id;
        this.campNomCientific = campNomCientific;
        this.campNomComu = campNomComu;
        this.campMida = campMida;
        this.campNumEx = campNumEx;
        this.aiguaSalada = aiguaSalada;
        this.habitats = habitats;
    }
    public Long getId() {
        return id;
    }

    public String getCampNomCientific() {
        return campNomCientific;
    }

    public void setCampNomCientific(String campNomCientific) {
        this.campNomCientific = campNomCientific;
    }

    public String getCampNomComu() {
        return campNomComu;
    }

    public void setCampNomComu(String campNomComu) {
        this.campNomComu = campNomComu;
    }

    public double getCampMida() {
        return campMida;
    }

    public void setCampMida(double campMida) {
        this.campMida = campMida;
    }

    public boolean isAiguaSalada() {
        return aiguaSalada;
    }

    public void setAiguaSalada(boolean aiguaSalada) {
        this.aiguaSalada = aiguaSalada;
    }
    public int getCampNumEx() {
        return campNumEx;
    }

    public void setCampNumEx(int campNumEx) {
        this.campNumEx = campNumEx;
    }

    public Collection<Habitat> getHabitats() {
        return habitats;
    }

    public void setHabitats(Collection<Habitat> habitats) {
        this.habitats = habitats;
    }


    public static class Habitat implements Comparable<Habitat>, Serializable{
        public Habitat(String regio, String profunditat) {

            this.regio = Regio.valueOf(regio);
            this.profunditat = Integer.parseInt(profunditat);
        }

        @Override
        public int compareTo(Habitat o) {
            return this.regio.compareTo(o.getRegio());
        }

        public static enum Regio{
            ID1("ATLÀNTIC"), ID2("PACÍFIC"), ID3("ÍNDIC"), ID4("MEDITERRANI"), ID5("POLAR"),
            ID6("POLINESI"), ID7("ÀRTIC"), ID8("ANTÀRTIC"), ID9("CARIB"), ID10("GOLF DE MÈXIC"),
            ID14("NO ESPECIFICAT");

            private String nom;

            private Regio(String nom){
                this.nom = nom;
            }
            public String getNom(){
                return nom;
            }

            @Override
            public String toString() {
                return this.name()+"-"+nom;
            }
        }
        private Regio regio;
        private int profunditat;

        public Habitat(Regio regio, int profunditat){
            this.regio = regio;
            this.profunditat = profunditat;
        }

        public Regio getRegio() {
            return regio;
        }
        public void setRegio(Regio regio) {
            this.regio = regio;
        }
        public int getProfunditat() {
            return profunditat;
        }
        public void setProfunditat(int profunditat) {
            this.profunditat = profunditat;
        }
    }

    @Override
    public String toString() {
        return "DadesPeix{" +
                "nomCientific='" + campNomCientific + '\'' +
                ", nomComu='" + campNomComu + '\'' +
                ", mida=" + campMida +
                ", numExemplars=" + campNumEx +
                ", aiguaSalada=" + aiguaSalada +
                '}';
    }
}
