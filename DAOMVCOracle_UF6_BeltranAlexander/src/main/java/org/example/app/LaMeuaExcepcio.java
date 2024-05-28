package org.example.app;

import java.util.HashMap;
import java.util.Map;

public class LaMeuaExcepcio extends Exception{

    //HashMap de excepcions i errors
    private static final Map<Integer, String> EXEPCIO = new HashMap<>();

    private int codi;

    static {
        // Misstages d'error amb els codis corresponents
        EXEPCIO.put(1, "Ha petat la base de dades");
        EXEPCIO.put(2, "No s'ha pogut desar el fitxer");
        EXEPCIO.put(3, "No s'ha pogut llegir el fitxer");
        EXEPCIO.put(4, "No s'ha pogut tancar el fitxer");
        EXEPCIO.put(5, "No s'ha pogut obrir el fitxer");
        EXEPCIO.put(6, "Falta omplir alguna dada");
        EXEPCIO.put(7, "Aquesta espècie ja existeix a la taula");
        EXEPCIO.put(8, "Has d'introduïr una mida correcta (>=0 i <=20)");
        EXEPCIO.put(9, "Has d'introduïr un número d'exemplars correcte (>=0)");
        EXEPCIO.put(10, "Per borrar selecciona una fila");
        EXEPCIO.put(11, "Per modificar selecciona una fila");
        EXEPCIO.put(12, "El camp profunditat ha de ser un enter positiu");
        EXEPCIO.put(13, "PEIX ES NULL");
        EXEPCIO.put(14, "Nom cientific no pot contenir numeros ni caràcters especials");
        EXEPCIO.put(15, "Nom comú no pot contenir números ni caràcters especials");
        EXEPCIO.put(16, "El camp mida ha de ser un enter positiu");
        EXEPCIO.put(17, "El Numero d'exemplars ha de ser un enter positiu");
        EXEPCIO.put(18, "NO ES POT ESBORRAR JA QUE ES INVALID INDEX");
        EXEPCIO.put(19, "NO es pot escriure el fitxer de pescadors");
        EXEPCIO.put(20, "NO es pot llegir el fitxer de pescadors");
        EXEPCIO.put(21, "Selecciona una fila per mostrar el seu pescador");
        EXEPCIO.put(22, "L'index de la fila esta fora dels limits");

    }

    public LaMeuaExcepcio(int codi) {
        super(EXEPCIO.get(codi));
        this.codi = codi;
    }

    public String missatge() {
        return EXEPCIO.get(this.codi);
    }

    public int getCodi() {
        return codi;
    }


}
