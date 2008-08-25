/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.trinisoft.dbthings.test;

import com.trinisoft.libraries.*;
import com.don.nlp.Word;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;
import junit.framework.TestCase;

/**
 *
 * @author Administrator
 */
public class TestDBThings extends TestCase {

    DBThings things;

    public TestDBThings() {
        things = new DBThings();
    }

    public void testGetResultSet() throws Exception {
        Word word = new Word();
        Word word2 = new Word();
        String query = "select * from word_table where root = 'go'";
        ResultSet resultSet;
        ArrayList list2 = new ArrayList();
        Statement st;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost/nlp", "root", "mysql");
            st = con.createStatement();
            resultSet = st.executeQuery(query);
            int i = 0;
            while (resultSet.next()) {
                word2.setword(resultSet.getString(1));
                word2.setroot(resultSet.getString(2));
                word2.setpos(resultSet.getString(3));
                word2.setindy(resultSet.getString(4));
                list2.add(word2);
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
        
        ArrayList arrayList = new ArrayList();
        arrayList = things.getResultSet(query, word);
        Iterator ite = arrayList.iterator();
        while(ite.hasNext()) {
            Word wr = (Word) ite.next();
            System.out.println(wr.word);
        }
        assertEquals(arrayList.size(), list2.size());
        assertEquals(list2.get(1).toString(), arrayList.get(1).toString());
    }
    
    public void testInsert() throws Exception {
        Word word = new Word();
        word.indy = "auto_increment";
        word.pos = "adverb";
        word.root = "friend";
        word.word = "friendship";        
        things.insert(word,"word_table");
    }

    public void testUpdate() throws Exception {
        Word word = new Word();
        word.indy = "dont_update";
        word.pos = "adjective";
        word.root = "dont_update";
        word.word = "dont_update";        
        String where = "where indy = '23'";
        things.update(word,"word_table",where);
    }    
    
    public void testExecuteQuery() throws Exception {
			DBThings things = new DBThings("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/hashql", "hashql", "hashql");
        String q = "select distinct dbname from hashql_databases";
        ArrayList retVal = things.executeQuery(q, 1);
        Iterator ite = retVal.iterator();
        while(ite.hasNext()) {
            Vector vec = (Vector) ite.next();
            Enumeration enu = vec.elements();
            while(enu.hasMoreElements()) {
                System.out.print(enu.nextElement() + ", ");
            }
            System.out.println("");
        }
    }
}
