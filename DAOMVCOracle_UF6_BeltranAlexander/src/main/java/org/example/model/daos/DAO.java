package org.example.model.daos;


import org.example.model.entities.Peix;
import org.example.model.exceptions.DAOException;
import org.example.model.impls.PeixDAOJDBCOracleImpl;

import java.util.List;

public interface DAO <T>{

    T get(Long id) throws DAOException;

    List<T> getAll() throws DAOException;

    void save(T obj) throws DAOException;

    void crearTaula() throws DAOException;

    DAO<Peix> dadesPeix = new PeixDAOJDBCOracleImpl();

    void delete(T obj) throws DAOException;

    void update(T obj) throws DAOException;
//    void executarPLSQL(String plsql) throws DAOException;
    //Tots els mètodes necessaris per interactuar en la BD

}
