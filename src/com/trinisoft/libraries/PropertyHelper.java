package com.trinisoft.libraries;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyHelper {

    public Properties getProperties(String path) throws IOException {
        InputStream is = null;
        if (path == null) {
            return new Properties();
        } else {
            Properties properties = new Properties();
            System.out.println("trying to load from jar file");
            try {
                is = getClass().getClassLoader().getResourceAsStream(path);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

            if (is == null) {
                System.out.println("trying to load from current dir");
                try {
                    is = new FileInputStream(new File(".\\" + path));
                } catch (FileNotFoundException fne) {
                    System.out.println(fne.getMessage());
                }
            }

            if (is == null) {
                System.out.println("trying to load from path as specified");
                try {
                    is = new FileInputStream(new File(path));
                } catch (FileNotFoundException fne) {
                    System.out.println(fne.getMessage());
                }
            }
            if(is == null) {
                System.out.println("trying to load from user home directory");
                try {
                    is = new FileInputStream(new File(System.getProperty("user.home") + File.pathSeparator + path));
                } catch(FileNotFoundException fnfe) {
                    System.out.println(fnfe.getMessage());
                }
            }
            
            if (is == null) {
                return properties;
            } else {
                properties.load(is);
                return properties;
            }
        }
    }
}