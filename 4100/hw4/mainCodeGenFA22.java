package project5;

//import ADT.SymbolTable;
//import ADT.Lexical;
import ADT.*;

/**
 *
 * @author abrouill SPRING 2021
 */
public class main {

    public static void main(String[] args) {
        String filePath = args[0];
        System.out.println("Parsing "+filePath);
        boolean traceon = true; //false;
        Syntactic parser = new Syntactic(filePath, traceon);
        parser.parse();
        
        System.out.println("Done.");
    }

}
