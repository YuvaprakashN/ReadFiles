package org.example;

import java.io.*;
import java.util.Properties;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws IOException {

        String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        String appConfigPath = rootPath + "application.properties";
        Properties appProps = new Properties();
        appProps.load(new FileInputStream(appConfigPath));


        System.out.println("File Reading: ");
        File file = new File(appProps.getProperty("READ_FILE_PATH"));
        FileReader fr = new FileReader(file);
        int ch;
        while ((ch=fr.read()) != -1) {
            System.out.print((char)ch);
        }
        fr.close();

        try {
            System.out.println("\nFile Create: ");

            File myObj = new File("src/main/resources/writeFile.txt");
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
            } else {
                new FileWriter(appProps.getProperty("WRITE_FILE_PATH"), false).close();
                System.out.println("File already exists.");
            }

            System.out.println("File Writer: ");
            File writeFile = new File("filename.txt");
            FileWriter fw = new FileWriter(writeFile);
            fw.write("I am writing in File!");
            fw.close();


        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}