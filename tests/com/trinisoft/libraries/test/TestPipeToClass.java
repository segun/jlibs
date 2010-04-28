package com.trinisoft.libraries.test;

import com.trinisoft.libraries.*;

import java.util.*;
import junit.framework.TestCase;

/**
 *
 * @author Administrator
 */
public class TestPipeToClass extends TestCase {

    PipeToClass ppClass;

    public TestPipeToClass() {
        
    }

	 public void setUp() {
		 ppClass = new PipeToClass();
	 }

    public void testGoodMap() throws Exception {
		 String passThis = "Name:Don, Pass:Fred, ID:Greatest, Phone:08089370313";

		 Map getThis2 = new HashMap();
		 getThis2 = (HashMap) ppClass.pipeToClass(getThis2, passThis, ":");
		 System.out.println("HashMap: " + getThis2);
		 assertEquals(getThis2.get("Pass"), "Fred");

		 Map getThis = new Hashtable();
		 getThis = (Hashtable) ppClass.pipeToClass(getThis, passThis, ":");
		 assertEquals(getThis.get("Name"), "Don");
    }

	 public void testBadMap() {
		 try {
			 String passThis = "Name:Don, Pass:Fred, Phone:Cool";
			 Vector getThis = new Vector();
			 getThis = (Vector) ppClass.pipeToClass(getThis, passThis, ":");
			 assertNull(getThis);
		 } catch(IllegalArgumentException iae) {
			 System.err.println("testBadMap caused an Exception: " + iae.getMessage());
		 }
	 }

	 public void testGoodCollection() throws Exception {
		 String passThis = "Name:Age:Sex:Color";

		 Vector getThis = new Vector();
		 getThis = (Vector)ppClass.pipeToClass(getThis, passThis, ":");
		 System.out.println(getThis);
		 assertEquals(getThis.get(0), "Name");
		 assertEquals(getThis.get(3), "Color");
	 }

	 public void testBadCollection() {
		 try {
			 String passThis = "Foo:Bar:Baz";
			 Map getThis = new Hashtable();
			 getThis = (Map)ppClass.pipeToClass(getThis, passThis, ":");
			 assertNull(getThis);
		} catch(IllegalArgumentException iae) {
			System.err.println("testBadCollection caused an Exception: " + iae.getMessage());
		}
	 }
}
