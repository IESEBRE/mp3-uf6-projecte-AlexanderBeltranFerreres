package org.example.model.impls;

import org.example.app.LaMeuaExcepcio;
import org.example.model.daos.DAO;
import org.example.model.entities.Peix;
import org.example.model.exceptions.DAOException;

import javax.print.DocFlavor;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.TreeSet;

import static java.lang.System.getProperty;

public class PeixDAOJDBCOracleImpl implements DAO<Peix> {

    //Mètode per crear la taula si no existeix
    public void crearTaula() throws DAOException{
        //Comprovem si la taula ja existeix
        String checkTaulaExisteix = "SELECT count(*) FROM user_tables WHERE table_name = 'PEIXOS'";

        String consultaCrearTaula = " CREATE TABLE Peixos (\n" +
                "                id NUMBER PRIMARY KEY,\n" +
                "                nom_cientific VARCHAR2(100),\n" +
                "                nom_comu VARCHAR2(100),\n" +
                "                mida NUMBER,\n" +
                "                num_exemplars NUMBER,\n" +
                "                aigua_salada CHAR(1)\n" +
                "            )";

        try (Connection con = DriverManager.getConnection(
//                getProperty("db.url"),
//                getProperty("db.user"),
//                getProperty("db.password")

                "jdbc:oracle:thin:@//localhost:1521/xe",
                "C##HR",
                "HR"
        );
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(checkTaulaExisteix);
        ) {
            if (!rs.next() || rs.getInt(1) == 0) {
                st.execute(consultaCrearTaula);
            }
        } catch (SQLException throwables) {
            throw new DAOException(1);
        }
    }

//    @Override
//    public void executarPLSQL(String plsql) throws DAOException {
//        String jdbcUrl = "jdbc:oracle:thin:@//localhost:1521/xe";
//        String username = "C##HR";
//        String password = "HR";
//
//        plsql = "/home/alumne/Escriptori/MP03_Programacio/mp3-uf6-projecte-AlexanderBeltranFerreres/PLSQL/pl.sql";
//        try (Connection con = DriverManager.getConnection(jdbcUrl, username, password);
//             Statement stmt = con.createStatement()) {
//
//            // Read PL/SQL file content
//            String plsqlContent = new String(Files.readAllBytes(Paths.get(plsql)), StandardCharsets.UTF_8);
//
//            // Executar la comanda PL/SQL
//            stmt.execute(plsqlContent);
//            System.out.println("PL/SQL script executed successfully");
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//            throw new DAOException(9);
//        } catch (IOException e) {
//            e.printStackTrace();
//            throw new DAOException(10); // Assuming 10 is the code for file read error
//        }
//    }



    //Mètode per obtenir un peix de la BD a partir del seu id
    @Override
    public Peix get(Long id) throws DAOException {

        //Declaració de variables del mètode
        Connection con = null; //Estableix connexió amb la BD
        Statement st = null; //Executa sentències SQL i retorna resultats
        ResultSet rs = null; // Emmagatzema els resultats de la consulta
        Peix peix = null; //Objecte que emmagatzema les dades del peix

        //Accés a la BD usant l'API JDBC
        try {

            con = DriverManager.getConnection(
//                    getProperty("db.url"),
//                    getProperty("db.user"),
//                    getProperty("db.password")

                    "jdbc:oracle:thin:@//localhost:1521/xe",
                    "C##HR",
                    "HR"
            );
//¡            st = con.prepareStatement("SELECT * FROM estudiant WHERE id=?;");
            st = con.createStatement(); //statement per executar la consulta
//            st = con.prepareStatement("SELECT * FROM estudiant WHERE id=?;");
//            st.setLong(1, id);
            rs = st.executeQuery("SELECT * FROM PEIXOS"); //executa la consulta
//            estudiant = new Alumne(rs.getLong(1), rs.getString(2));
//            st.close();

            // Si hi ha resultats, els emmagatzemem en l'objecte peix
            if (rs.next()) {
                peix = new Peix(Long.valueOf(rs.getString(1)), rs.getString(2), rs.getString(3), rs.getDouble(4), rs.getInt(5), rs.getBoolean(6));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new DAOException(1);
        } finally {
            //Tanquem els objectes de la BD
            try {
                if (rs != null) rs.close();
                if (st != null) st.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                throw new DAOException(1);
            }

        }
        return peix;
    }


    //Mètode per obtenir tots els peixos de la BD
    @Override
    public List<Peix> getAll() throws DAOException {
        // Declaració de la llista de peixos
        List<Peix> peixos = new ArrayList<>();

        // Accés a la BD usant l'API JDBC
        try (Connection con = DriverManager.getConnection(
                "jdbc:oracle:thin:@//localhost:1521/xe",
                "C##HR",
                "HR"
        );
             PreparedStatement st = con.prepareStatement("SELECT * FROM PEIXOS");
             ResultSet rs = st.executeQuery();
        ) {

            // Si hi ha resultats, els emmagatzemem en la llista de peixos
            boolean hasResults = false;
            while (rs.next()) {
                hasResults = true;
                peixos.add(new Peix(
                        rs.getLong("id"),
                        rs.getString("nom_cientific"),
                        rs.getString("nom_comu"),
                        rs.getDouble("mida"),
                        rs.getInt("num_exemplars"),
                        rs.getBoolean("aigua_salada")
                ));
            }

            if (!hasResults) {
                System.out.println("No s'han trobat resultats en la consulta.");
            }
        } catch (SQLException throwables) {
            int tipoError = throwables.getErrorCode();
            System.out.println("Error de la base de dades: " + throwables.getMessage()); // todo TRAURE
            switch (throwables.getErrorCode()) {
                case 17002: // Error de connexió
                    tipoError = 0;
                    break;
                default:
                    tipoError = 1;  // Error desconegut
            }
            throw new DAOException(tipoError);
        }

        return peixos;
    }


    //Mètode per guardar un peix a la BD
    @Override
    public void save(Peix peix) throws DAOException {

        Connection con = null;
        try { con = DriverManager.getConnection(
                    "jdbc:oracle:thin:@//localhost:1521/xe",
                    "C##HR",
                    "HR"
            );
        if (con == null) {
            throw new DAOException(0);
        }
        if (peix == null) {
            throw new DAOException(2291);
        }

        String sql = "INSERT INTO peixos (nom_cientific, nom_comu, mida, num_exemplars, aigua_salada) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setString(1, peix.getCampNomCientific());
            st.setString(2, peix.getCampNomComu());
            st.setDouble(3, peix.getCampMida());
            st.setInt(4, peix.getCampNumEx());
            st.setBoolean(5, peix.isAiguaSalada());
            st.executeUpdate();

        } catch (SQLException e) {
            throw new DAOException(1);
        }
    } catch (SQLException e) {
        throw new DAOException(1);
    } finally {
        try {
            if (con != null) con.close();
        } catch (SQLException e) {
            throw new DAOException(1);
        }
    }
    }

    //Mètode per actualitzar un peix de la BD
    @Override
    public void update (Peix peix) throws DAOException {
        // Declara la connexió
        Connection con = null;

        try {
            // Estableix la connexió amb la base de dades
            con = DriverManager.getConnection(
                    "jdbc:oracle:thin:@//localhost:1521/xe",
                    "C##HR",
                    "HR"
            );

            // Verifica si la connexió és nul·la
            if (con == null) {
                throw new DAOException(0);
            }

            // Verifica si el peix és nul
            if (peix == null) {
                throw new DAOException(2291);
            }

            // Crea la sentència SQL per actualitzar el peix
            String sql = "UPDATE peixos SET nom_cientific = ?, nom_comu = ?, mida = ?, num_exemplars = ?, aigua_salada = ? WHERE id = ?";

            // Prepara la sentència SQL
            try (PreparedStatement st = con.prepareStatement(sql)) {
                // Estableix els paràmetres de la sentència
                st.setString(1, peix.getCampNomCientific());
                st.setString(2, peix.getCampNomComu());
                st.setDouble(3, peix.getCampMida());
                st.setInt(4, peix.getCampNumEx());
                st.setBoolean(5, peix.isAiguaSalada());
                st.setLong(6, peix.getId());

                // Executa la sentència SQL per actualitzar el peix
                st.executeUpdate();
            } catch (SQLException e) {
                throw new DAOException(1);
            }
        } catch (SQLException e) {
            throw new DAOException(1);
        } finally {
            // Tanca la connexió
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                throw new DAOException(1);
            }
        }
    }
    //Mètode per borrar un peix de la BD
    @Override
    public void delete(Peix peix) throws DAOException {
        // Declara la connexió
        Connection con = null;

        try {
            // Estableix la connexió amb la base de dades
            con = DriverManager.getConnection(
                    "jdbc:oracle:thin:@//localhost:1521/xe",
                    "C##HR",
                    "HR"
            );

            // Verifica si la connexió és nul·la
            if (con == null) {
                throw new DAOException(0);
            }

            // Verifica si el peix és nul
            if (peix == null) {
                throw new DAOException(2291);
            }

            // Crea la sentència SQL per esborrar el peix
            String sql = "DELETE FROM peixos WHERE id = ?";

            // Prepara la sentència SQL
            try (PreparedStatement st = con.prepareStatement(sql)) {
                // Estableix els paràmetres de la sentència
                st.setLong(1, peix.getId());

                // Executa la sentència SQL per esborrar el peix
                st.executeUpdate();
            } catch (SQLException e) {
                throw new DAOException(1);
            }
        } catch (SQLException e) {
            throw new DAOException(1);
        } finally {
            // Tanca la connexió
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                throw new DAOException(1);
            }
        }
    }



    //configuracio del config.properties
    public class DBConfig {
        private Properties properties = new Properties();

        public DBConfig() {
            try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
                if (input == null) {
                    System.out.println("Error al accedir a config.properties");
                    return;
                }
                properties.load(input);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        public String getProperty(String key) {
            return properties.getProperty(key);
        }
    }

}
