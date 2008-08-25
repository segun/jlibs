/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.trinisoft.libraries;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Vector;

/**
 *
 * @author Administrator
 */
public abstract class DBClass {

    String dbUser = "root";
    String dbPass = "mysql";
    String dbDriver = "com.mysql.jdbc.Driver";
    String dbURL = "jdbc:mysql://localhost/nlp";
    Connection con = null;
    Statement st = null;
    ResultSet resultSet;
    Vector vector;

    protected abstract boolean connect();

    public abstract ArrayList getResultSet(String queryString, Object tableReflector) throws Exception;
    public abstract boolean insert(Object tableReflector, String tableName) throws Exception;
    public abstract boolean update(Object tableReflector, String tableName, String where) throws Exception;
    public abstract ArrayList executeQuery(String queryString, int exepectedCols) throws Exception;
}
