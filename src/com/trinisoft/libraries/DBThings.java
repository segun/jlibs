package com.trinisoft.libraries;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

/**
 *
 * @author Da Don
 * This class helps you to quickly connect to a DB and performs reflection
 */
public class DBThings extends DBClass {
    
    Logger logger = Logger.getLogger(DBThings.class);
    public DBThings() {
        //PropertyConfigurator.configure("/etc/DBThings/log4j.properties");
        BasicConfigurator.configure();
        Random rand = new Random(5);
        this.connect();
    }

    public DBThings(String dbDriver, String dbURL, String dbUser, String dbPass) {
        this.dbDriver = dbDriver;
        this.dbUser = dbUser;
        this.dbPass = dbPass;
        this.dbURL = dbURL;
        this.connect();
    }
    
    @Override
    protected boolean connect() {
        try {
            Class.forName(dbDriver);
            con = DriverManager.getConnection(dbURL, dbUser, dbPass);
            st = con.createStatement();
            logger.info("Database Connection Successful");
            return true;
        } catch (ClassNotFoundException cnfe) {
            logger.error("Can not find database class");
            cnfe.printStackTrace();
        } catch (SQLException sqle) {
            logger.error("An SQL Exception occurred");
            sqle.printStackTrace();
        }
        return false;
    }

    /**
     * 
     * @param obj The Object that is either an ArrayList or a Vector to store returned values
     * @param queryString The query to execute
     * @param tableReflector A class that reflects all the fields in the database table
     * @return boolean 
     * @throws java.lang.Exception
     * <br>The first arguement is an Object, the object can either be an ArrayList or a Vector. The Vector implementation is coming soon <br><br>
     * The second arguement is the query string<br><br>
     * The third arguement is a class that defines the fields in the database as public members.<br>
     * <strong>NOTE:</strong>The members must all be public and the class must define all the fields in the databse as members.<br>
     * This class must also define public setter methods for all the members. The setter methods must be in the form set<variablename><br>
     * e.g if a member is foo, the the method name should be setfoo() and not setFoo(). Please take noter<br>
     * <strong>NOTE:</strong> Also that the setter methods must be defined in the order of the fields in the database<br><br>
     * 
     * The return arguement is a boolean. The values from the database are stored in the ArrayList arguement.
     */
    public ArrayList getResultSet(String queryString, Object tableReflector) throws Exception {
        ArrayList list = new ArrayList();
        Field fids[] = tableReflector.getClass().getFields();
        if (fids.length < 0) {
            throw new Exception("No public Field Found in Class " + tableReflector.getClass().getName());
        }
        Method methods[] = tableReflector.getClass().getMethods();
        if (methods.length < 0) {
            throw new Exception("No public Method Found in Class " + tableReflector.getClass().getName());
        }
        
        int j = 0;
        Method setMethods[] = new Method[fids.length];
        for(int i = 0; i < methods.length; i++) {
            if(methods[i].getName().contains("set")) {
                setMethods[j++] = methods[i];
            }
        }
        
        methods = setMethods;
        
        String args = null;
        try {
            resultSet = st.executeQuery(queryString);
            int i = 0;
            while (resultSet.next()) {
                tableReflector = tableReflector.getClass().getConstructor().newInstance();
                for (i = 0; i < fids.length; i++) {
                    args = resultSet.getString(i + 1);
                    if (!(methods[i].getName().contains("get"))) {
                        methods[i].invoke(tableReflector, args);
                        System.out.println("METHOD : " + methods[i].getName());
                        System.out.println("FID : " + fids[i].get(tableReflector));
                    }
                }
                list.add(tableReflector);
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        } catch (IllegalAccessException iae) {
            iae.printStackTrace();
        } catch (InvocationTargetException ite) {
            ite.printStackTrace();
        }
        return list;
    }

    /**
     * 
     * @param tableReflector The class that reflects all the fields in the database table
     * @param tableName The name of the table to insert into
     * @return boolean
     * @throws java.lang.Exception
     * <br>The first arguement is the class that reflects the fields in the table.<br>
     * <strong>NOTE</strong> that the member variables must be public and must bear the same name as the fields in the database<br>
     * <strong>NOTE</strong> If you have an auto-increment field in the table, just set the value to auto_increment, dont leave it null or an empty string.<br>
     * If every thing is successful, the method returns a true else an exception is thrown
     */
    public boolean insert(Object tableReflector, String tableName) throws Exception {
        Field fids[] = tableReflector.getClass().getFields();
        if (fids.length < 0) {
            throw new Exception("No public Field Found in Class " + tableReflector.getClass().getName());
        }
        String query = "insert into " + tableName + "(";
        for (int i = 0; i < fids.length; i++) {
            query += fids[i].getName() + ",";
        }
        query = query.substring(0, query.length() - 1);
        System.out.println(query);
        query += ") values ('";
        for (int i = 0; i < fids.length; i++) {
            String fid = fids[i].get(tableReflector).toString();
            if (fid.equals("auto_increment")) {
                fid = getAutoFid(tableName, fids[i].getName());
            }
            query += fid + "','";
        }
        query = query.substring(0, query.length() - 2);
        query += ")";
        System.out.println(query);
        st.execute(query);
        return true;
    }  

    /**
     * 
     * @param tableReflector The class that reflects all the fields in the database table
     * @param tableName The name of the table to update
     * @param whereField The query to use as the where clause e.g where index = '24'
     * @return boolean
     * @throws java.lang.Exception
     * <br>The first arguement is the class that reflects the fields in the table.<br>
     * <strong>NOTE</strong> that the member variables must be public and must bear the same name as the fields in the database<br>
     * <strong>NOTE</strong> If you have a field that you dont want to update, set the value to dont_update<br>
     * If every thing is successful, the method returns a true else an exception is thrown
     */
    public boolean update(Object tableReflector, String tableName, String whereField) throws Exception {
        Field fids[] = tableReflector.getClass().getFields();
        if (fids.length < 0) {
            throw new Exception("No public Field Found in Class " + tableReflector.getClass().getName());
        }
        String query = "update " + tableName + " set ";
        for (int i = 0; i < fids.length; i++) {
            String fidName = fids[i].getName();
            String fidValue = fids[i].get(tableReflector).toString();
            if (fidValue.equals("dont_update")) {
                continue;
            } else {
                query += fidName + "='" + fidValue + "',";
            }
        }
        query = query.substring(0, query.length() - 1) + " " + whereField;
        System.out.println(query);
        st.execute(query);
        return true;
    }
    
    public ArrayList executeQuery(String query, int expectedCols) throws Exception {
        ArrayList retVal = new ArrayList();
        try {
            resultSet = st.executeQuery(query);
            while(resultSet.next()) {
                Vector cols = new Vector();
                for(int i = 1; i <= expectedCols; i++) {
                    cols.add(resultSet.getString(i));
                }
                retVal.add(cols);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return retVal;
    }

	 public void executeQuery(String query) throws Exception {
		 try {
			 System.out.printf("Query : %s\n", query);
			 st.execute(query);
		 } catch(Exception e) {
			 e.printStackTrace();
		 }
	 }

    private String getAutoFid(String table, String fidName) throws Exception {
        String query = "select max(" + fidName + ") from " + table;
        resultSet = st.executeQuery(query);
        if (resultSet.next()) {
            return String.valueOf(Integer.parseInt(resultSet.getString(1)) + 1);
        }
        return "1";
    }
}
