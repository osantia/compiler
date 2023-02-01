package project3;

import ADT.*;

/**
 *
 * @author abrouill FALL 2022
 */
public class main {

    public static void main(String[] args) {
       String filePath = args[0];
        boolean traceon = true;
        System.out.println("Student Name, Last 4 of student number, CS4100/5100, FALL 2022");
        System.out.println("INPUT FILE TO PROCESS IS: "+filePath);
    
        Syntactic parser = new Syntactic(filePath, traceon);
        parser.parse();
        System.out.println("Done.");
    }

}
