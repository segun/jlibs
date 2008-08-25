/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.don.nlp;

/**
 *
 * @author Administrator
 */
public class Word {
    public String word;
    public String root;
    public String pos;
    public String indy;
    
    public void setword(String s) {
        word = s;
    }
    
    public void setroot(String s) {
        root = s;
    }    
    
    public void setpos(String s) {
        pos = s;
    }    
    
    public void setindy(String s) {
        indy = s;
    }
    
    @Override
    public String toString() {
        return word + "|" + root + "|" + pos + "|" + indy;
    }
}
