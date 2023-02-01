package main;
//import ADT.Lexical;
import ADT.*;
//import ADT.LexicalReserve;
//import ADT.Lexical;
/**
 *
 * @author abrouill FALL 2022
 */
public class main {

    public static void main(String[] args) {
        //String inFileAndPath = args[0];
    	String inFileAndPath = "c:\\Users\\oscan\\Downloads\\LexicalTestFA22.txt";
        //String outFileAndPath = args[1];
    	String outFileAndPath = "hello.txt";
        System.out.println("Lexical for " + inFileAndPath);
        boolean traceOn = true;
        // Create a symbol table to store appropriate3 symbols found
        SymbolTable symbolList;
        symbolList = new SymbolTable(150);
//        Lexical myLexer = new Lexical(inFileAndPath, symbolList, traceOn);
//        Lexical.token currToken;
        LexicalReserve myLexer = new LexicalReserve(inFileAndPath, symbolList, traceOn);
        LexicalReserve.token currToken;
        currToken = myLexer.GetNextToken();
        while (currToken != null) {
            System.out.println("\t" + currToken.mnemonic + " | \t" + String.format("%04d", currToken.code)
                    + " | \t" + currToken.lexeme);
            currToken = myLexer.GetNextToken();
        }
        symbolList.PrintSymbolTable(outFileAndPath);
        System.out.println("Done.");
    }

}
