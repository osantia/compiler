package ADT;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 *
 * @author abrouill
 */
import java.io.*;

public class LexicalReserve {

    private File file;                        //File to be read for input
    private FileReader filereader;            //Reader, Java reqd
    private BufferedReader bufferedreader;    //Buffered, Java reqd
    private String line;                      //Current line of input from file   
    private int linePos;                      //Current character position
    //  in the current line
    private SymbolTable saveSymbols;          //SymbolTable used in Lexical
    //  sent as parameter to construct
    private boolean EOF;                      //End Of File indicator
    private boolean echo;                     //true means echo each input line
    private boolean printToken;               //true to print found tokens here
    private int lineCount;                    //line #in file, for echo-ing
    private boolean needLine;                 //track when to read a new line
    private int IDENT_ID = 50;  			  //identifier id for getIdentfier
    private String IDENT_MNU = "IDENT";
    private int CHAR_LIMIT = 20;
    private int INT_LIMIT = 6;
    private int FLOAT_LIMIT = 12;
    private int STRING_BEGIN = 0;
    private int UNDEFINED_CODE = 99;
    private String UNDEFINED_MNEMONIC = "UNDEF";
    private String STRING_MNMONIC = "STRIN";
    private int STRING_CODE = 53;
    private int INTEGER_CODE = 51;
    private String INTEGER_MNEMONIC = "INTEG";
    private int FLOAT_CODE = 52;
    private String FLOAT_MNEMONIC = "FLOAT";

//Tables to hold the reserve words and the mnemonics for token codes
    private final int sizeReserveTable = 50;
    private ReserveTable reserveWords = new ReserveTable(sizeReserveTable); //a few more than # reserves
    private ReserveTable mnemonics = new ReserveTable(sizeReserveTable); //a few more than # reserves

    //constructor
    public LexicalReserve(String filename, SymbolTable symbols, boolean echoOn) {
        saveSymbols = symbols;  //map the initialized parameter to the local ST
        echo = echoOn;          //store echo status
        lineCount = 0;          //start the line number count
        line = "";              //line starts empty
        needLine = true;        //need to read a line
        printToken = false;     //default OFF, do not print tokesn here
        //  within GetNextToken; call setPrintToken to
        //  change it publicly.
        linePos = -1;           //no chars read yet
        //call initializations of tables
        initReserveWords(reserveWords);
        initMnemonics(mnemonics);

        //set up the file access, get first character, line retrieved 1st time
        try {
            file = new File(filename);    //creates a new file instance  
            filereader = new FileReader(file);   //reads the file  
            bufferedreader = new BufferedReader(filereader);  //creates a buffering character input stream  
            EOF = false;
            currCh = GetNextChar();
        } catch (IOException e) {
            EOF = true;
            e.printStackTrace();
        }
    }

    // inner class "token" is declared here, no accessors needed
    public class token {

        public String lexeme;
        public int code;
        public String mnemonic;

        token() {
            lexeme = "";
            code = 0;
            mnemonic = "";
        }
    }

    
// ******************* PUBLIC USEFUL METHODS
// These are nice for syntax to call later 
// given a mnemonic, find its token code value
    public int codeFor(String mnemonic) {
        return mnemonics.LookupName(mnemonic);
    }
// given a mnemonic, return its reserve word

    public String reserveFor(String mnemonic) {
        return reserveWords.LookupCode(mnemonics.LookupName(mnemonic));
    }

    // Public access to the current End Of File status
    public boolean EOF() {
        return EOF;
    }
// DEBUG enabler, turns on/OFF token printing inside of GetNextToken

    public void setPrintToken(boolean on) {
        printToken = on;
    }

    /* @@@ */
    private void initReserveWords(ReserveTable reserveWords) {
    	reserveWords.Add("GOTO", 0);
    	reserveWords.Add("INTEGER", 1);
    	reserveWords.Add("TO", 2);
    	reserveWords.Add("DO", 3);
    	reserveWords.Add("IF", 4);
    	reserveWords.Add("THEN", 5);
    	reserveWords.Add("ELSE", 6);
    	reserveWords.Add("FOR", 7);
    	reserveWords.Add("OF", 8);
    	reserveWords.Add("WRITELN", 9);
    	reserveWords.Add("READLN", 10);
    	reserveWords.Add("BEGIN", 11);
    	reserveWords.Add("END", 12);
    	reserveWords.Add("VAR", 13);
    	reserveWords.Add("DOWHILE", 14);
    	reserveWords.Add("UNIT", 15);
    	reserveWords.Add("LABEL", 16);
    	reserveWords.Add("REPEAT", 17);
    	reserveWords.Add("UNTIL", 18);
    	reserveWords.Add("PROCEDURE", 19);
    	reserveWords.Add("DOWNTO", 20);
    	reserveWords.Add("FUNCTION", 21);
    	reserveWords.Add("RETURN", 22);
    	reserveWords.Add("FLOAT", 23);
    	reserveWords.Add("STRING", 24);
    	reserveWords.Add("ARRAY", 25);
    	reserveWords.Add("/", 30);
    	reserveWords.Add("*", 31);
    	reserveWords.Add("+", 32);
    	reserveWords.Add("-", 33);
    	reserveWords.Add("(", 34);
    	reserveWords.Add(")", 35);
    	reserveWords.Add(";", 36);
    	reserveWords.Add(":=", 37);
    	reserveWords.Add(">", 38);
    	reserveWords.Add("<", 39);
    	reserveWords.Add(">=", 40);
    	reserveWords.Add("<=", 41);
    	reserveWords.Add("=", 42);
    	reserveWords.Add("<>", 43);
    	reserveWords.Add(",", 44);
    	reserveWords.Add("[", 45);
    	reserveWords.Add("]", 46);
    	reserveWords.Add(":", 47);
    	reserveWords.Add("", 48);
    }

    /* @@@ */
    private void initMnemonics(ReserveTable mnemonics) {
    	mnemonics.Add("GOTOO", 0);
    	mnemonics.Add("INTEG", 1);
    	mnemonics.Add("TOOOO", 2);
    	mnemonics.Add("DOOOO", 3);
    	mnemonics.Add("IFFFF", 4);
    	mnemonics.Add("THENN", 5);
    	mnemonics.Add("ELSEE", 6);
    	mnemonics.Add("FORRR", 7);
    	mnemonics.Add("OFFFF", 8);
    	mnemonics.Add("WRILN", 9);
    	mnemonics.Add("READL", 10);
    	mnemonics.Add("BEGIN", 11);
    	mnemonics.Add("ENDDD", 12);
    	mnemonics.Add("VARRR", 13);
    	mnemonics.Add("DOWHI", 14);
    	mnemonics.Add("UNITT", 15);
    	mnemonics.Add("LABEL", 16);
    	mnemonics.Add("REPEA", 17);
    	mnemonics.Add("UNTIL", 18);
    	mnemonics.Add("PROCE", 19);
    	mnemonics.Add("DOWNT", 20);
    	mnemonics.Add("FUNCT", 21);
    	mnemonics.Add("RETUR", 22);
    	mnemonics.Add("FLOAT", 23);
    	mnemonics.Add("STRNG", 24);
    	mnemonics.Add("ARRAY", 25);
    	mnemonics.Add("DIVID", 30);
    	mnemonics.Add("MULTI", 31);
    	mnemonics.Add("ADD:)", 32);
    	mnemonics.Add("SUBTR", 33);
    	mnemonics.Add("PAR_L", 34);
    	mnemonics.Add("PAR_R", 35);
    	mnemonics.Add("SEMCO", 36);
    	mnemonics.Add("ASIGN", 37);
    	mnemonics.Add("GRTHN", 38);
    	mnemonics.Add("LETHN", 39);
    	mnemonics.Add("GOETN", 40);
    	mnemonics.Add("LOETN", 41);
    	mnemonics.Add("EQUAL", 42);
    	mnemonics.Add("NTEQL", 43);
    	mnemonics.Add("COMMA", 44);
    	mnemonics.Add("BRLFT", 45); 
    	mnemonics.Add("BRRGT", 46);
    	mnemonics.Add("COLON", 47);
    	mnemonics.Add("BLANK", 48);
    	mnemonics.Add("IDENT", 50);
    }


// ********************** UTILITY FUNCTIONS
    private void consoleShowError(String message) {
        System.out.println("**** ERROR FOUND: " + message);
    }

    // Character category for alphabetic chars
    private boolean isLetter(char ch) {
        return (((ch >= 'A') && (ch <= 'Z')) || ((ch >= 'a') && (ch <= 'z')));
    }

    // Character category for 0..9 
    private boolean isDigit(char ch) {
        return ((ch >= '0') && (ch <= '9'));
    }

    // Category for any whitespace to be skipped over
    private boolean isWhitespace(char ch) {
        // SPACE, TAB, NEWLINE are white space
        return ((ch == ' ') || (ch == '\t') || (ch == '\n'));
    }

    // Returns the VALUE of the next character without removing it from the
    //    input line.  Useful for checking 2-character tokens that start with
    //    a 1-character token.
    private char PeekNextChar() {
        char result = ' ';
        if ((needLine) || (EOF)) {
            result = ' '; //at end of line, so nothing
        } else // 
        {
            if ((linePos + 1) < line.length()) { //have a char to peek
                result = line.charAt(linePos + 1);
            }
        }
        return result;
    }

    // Called by GetNextChar when the cahracters in the current line are used up.
    // STUDENT CODE SHOULD NOT EVER CALL THIS!
    private void GetNextLine() {
        try {
            line = bufferedreader.readLine();
            if ((line != null) && (echo)) {
                lineCount++;
                System.out.println(String.format("%04d", lineCount) + " " + line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (line == null) {    // The readLine returns null at EOF, set flag
            EOF = true;
        }
        linePos = -1;      // reset vars for new line if we have one
        needLine = false;  // we have one, no need
        //the line is ready for the next call to get a character
    }

    // Called to get the next character from file, automatically gets a new
    //      line when needed. CALL THIS TO GET CHARACTERS FOR GETIDENT etc.
    public char GetNextChar() {
        char result;
        if (needLine) //ran out last time we got a char, so get a new line
        {
            GetNextLine();
        }
        //try to get char from line buff
        if (EOF) {
            result = '\n';
            needLine = false;
        } else {
            if ((linePos < line.length() - 1)) { //have a character available
                linePos++;
                result = line.charAt(linePos);
            } else { //need a new line, but want to return eoln on this call first
                result = '\n';
                needLine = true; //will read a new line on next GetNextChar call
            }
        }
        return result;
    }

// The constants below allow flexible comment start/end characters    
    final char commentStart_1 = '{';
    final char commentEnd_1 = '}';
    final char commentStart_2 = '(';
    final char commentPairChar = '*';
    final char commentEnd_2 = ')';

// Skips past single and multi-line comments, and outputs UNTERMINATED 
//  COMMENT when end of line is reached before terminating
    String unterminatedComment = "Comment not terminated before End Of File";

    public char skipComment(char curr) {
        if (curr == commentStart_1) {
            curr = GetNextChar();
            while ((curr != commentEnd_1) && (!EOF)) {
                curr = GetNextChar();
            }
            if (EOF) {
                consoleShowError(unterminatedComment);
            } else {
                curr = GetNextChar();
            }
        } else {
            if ((curr == commentStart_2) && (PeekNextChar() == commentPairChar)) {
                curr = GetNextChar(); // get the second
                curr = GetNextChar(); // into comment or end of comment
//            while ((curr != commentPairChar) && (PeekNextChar() != commentEnd_2) &&(!EOF)) {
                while ((!((curr == commentPairChar) && (PeekNextChar() == commentEnd_2))) && (!EOF)) {
//                if (lineCount >=4) {
                    //              System.out.println("In Comment, curr, peek: "+curr+", "+PeekNextChar());}
                    curr = GetNextChar();
                }
                if (EOF) {
                    consoleShowError(unterminatedComment);
                } else {
                    curr = GetNextChar();          //must move past close
                    curr = GetNextChar();          //must get following
                }
            }

        }
        return (curr);
    }

    // Reads past all whitespace as defined by isWhiteSpace
    // NOTE THAT COMMENTS ARE SKIPPED AS WHITESPACE AS WELL!
    public char skipWhiteSpace() {

        do {
            while ((isWhitespace(currCh)) && (!EOF)) {
                currCh = GetNextChar();
            }
            currCh = skipComment(currCh);
        } while (isWhitespace(currCh) && (!EOF));
        return currCh;
    }

    private boolean isPrefix(char ch) {
        return ((ch == ':') || (ch == '<') || (ch == '>'));
    }

    private boolean isStringStart(char ch) {
        return ch == '"';
    }

//global char
    char currCh;
    
    // function to truncate a given string to a desired length
    private String truncString(String input, int maxLimit) {
    	return input.substring(STRING_BEGIN, maxLimit);
    }
    
    private token getIdentifier(){
    	// creating an instance of a token
        token result = new token();
        // String that adds the abbreviated token to symbol table
        String toSymbolTable;
        
        result.lexeme = "" + this.currCh;
        this.currCh = GetNextChar();
  	  //Added the underscore to the check
        while (isLetter(this.currCh)||(isDigit(this.currCh))||(this.currCh == '_')) {
           result.lexeme = result.lexeme + this.currCh; //extend lexeme
           this.currCh = GetNextChar();
        }
        toSymbolTable = result.lexeme;
        // end of token, lookup or IDENT      
        // function (reservetable-class).LookupName automatically ignores case
        result.code = reserveWords.LookupName(result.lexeme);
        // if the result from looking up the code was -1 (aka not found):
        if (result.code == reserveWords.notFound) {
        	// since it is an identifier, the token code 50
            result.code = IDENT_ID;
            result.mnemonic = IDENT_MNU;
            // Truncate if greater than 20 chars
            if(result.lexeme.length() > CHAR_LIMIT ) {
            	toSymbolTable = truncString(result.lexeme, CHAR_LIMIT);
            	System.out.println("Identifier length > 20, truncated " + result.lexeme + " to " + toSymbolTable);
            	
            }
            // store idenitfier into symbol table
            saveSymbols.AddSymbol(toSymbolTable, 'v', 0);
        }
        else {
        	// add the mnemonic since all other categories have been added
        	result.mnemonic = mnemonics.LookupCode(result.code);
        }
        return result;
    }

    private token getNumber() {
        /* a number is:   <digit>+[.<digit>*[E<digit>+]] */
        token result = new token();
        
        // string that is sent to symbol table, keep the token lexeme intact
        String toSymbolTable;
        
        // Assume its an integer, else turn it into a floater
        boolean isInteger = true;
        result.lexeme = "" + this.currCh;
        this.currCh = GetNextChar();
        
        // while the next character is a digit (1-9)
        // add it to the lexeme
        while (isDigit(this.currCh)) {
        	result.lexeme += this.currCh;
        	this.currCh = GetNextChar();
        }
        
        // Convert the integer to float, optional
        if(this.currCh == '.') {
        	// input token no longer an integer
        	isInteger = false;
        	result.lexeme += this.currCh;
        	this.currCh = GetNextChar();
        }
        
        // REPEATING CODE IS BAD: GONNA DO IT CAUSE YOLO
        // while the digits after the if statement are 
        // still digits, then add digits to lexeme
        while(isDigit(this.currCh)) {
        	result.lexeme += this.currCh;
        	this.currCh = GetNextChar();
        }
        
        // (float/integer so far) * 10^(digits)
        if(this.currCh == 'E') {
        	isInteger = false;
        	result.lexeme += this.currCh;
        	// Assuming that the E is going to have a number afterwards:
        	// This is bad, gonna do it cause YOLO
        	this.currCh = GetNextChar();
        	// if next char not digit, make result undefined
        	if(!isDigit(this.currCh)) {
        		result.code = UNDEFINED_CODE;
        		result.mnemonic = UNDEFINED_MNEMONIC;
        		return result;
        	}
        	// else digit is valid, number is valid
        	else {
	        	while(isDigit(this.currCh)) {
	        		 // REPEATING CODE IS VERY uhhhh uhhhhhhh uhhhhh ummm uhh
	        		result.lexeme += this.currCh;
	            	this.currCh = GetNextChar();
	        	}
        	}
        }

        // Case: value is an integer, store integer
        if(isInteger) {
        	toSymbolTable = result.lexeme;
        	result.code = INTEGER_CODE;
        	result.mnemonic = INTEGER_MNEMONIC;
            // If the length of the lexeme exceeds 6, then truncate and WARN
        	// I can probably make the print a function but copy and paste is °‿‿°
            if(result.lexeme.length() > INT_LIMIT) {
	        	System.out.println("Integer length > 6, truncated " + result.lexeme + 
	        	/* same line as ^^^*/		" to " + truncString(result.lexeme, INT_LIMIT));
	        	toSymbolTable = truncString(result.lexeme, INT_LIMIT);
	        	// for some reason we dont add value if the int limit was reached/ same with float
	        	saveSymbols.AddSymbol(toSymbolTable, 'c', 0);
            }
            else {
            	saveSymbols.AddSymbol(toSymbolTable, 'c', Integer.valueOf(toSymbolTable));
            }
        }
        // Case: value is a float, store the float
        else {
        	toSymbolTable = result.lexeme;
        	result.code = FLOAT_CODE;
        	result.mnemonic = FLOAT_MNEMONIC;
        	// If the length of the lexeme exceeds 12, then truncate and WARN
	        if(result.lexeme.length() > FLOAT_LIMIT) {
	        	System.out.println("Float length > 12, truncated " + result.lexeme + 
	        								" to " + truncString(result.lexeme, FLOAT_LIMIT));
	        	toSymbolTable = truncString(result.lexeme, FLOAT_LIMIT);     
	        	saveSymbols.AddSymbol(toSymbolTable, 'c', 0.0);
            }
	        else {
	        	saveSymbols.AddSymbol(toSymbolTable, 'c', Double.valueOf(toSymbolTable));
	        }
        }
        
        return result;
    }
    
    private token getString() {
    	// important: if end of line reached, GetNextChar() returns '\n'
    	token result = new token();
    	// each " must have a partner, if eof reached before '"', then partner is false
    	boolean partner = true;
    	this.currCh = GetNextChar();
    	
    	// While the next char isnt a "
    	while(this.currCh != '"') {
    		
    		// add the resulting chars to 
        	result.lexeme += this.currCh;
        	this.currCh = GetNextChar();
        	// end of line reached
        	// error:untermindated string
        	if(this.currCh == '\n') {
        		System.out.println("Unterminated String");
        		partner = false;
        		break;
        	}
    	}
    	
    	// save if a valid string
    	if(partner) {
    		result.code = STRING_CODE;
    		result.mnemonic = STRING_MNMONIC;
    	    saveSymbols.AddSymbol(result.lexeme, 'c', result.lexeme);
    	}
    	// undef if unvalid
    	else {
    		// give undefined 
    		result.code = UNDEFINED_CODE;
    		result.mnemonic = UNDEFINED_MNEMONIC;
    	}
    	this.currCh = GetNextChar();
    	return result;
    }

    private token getOtherToken() {
    	token result = new token();
    	// peeks the next character
        char peeky = PeekNextChar();
        result.lexeme += this.currCh;
        
        // first char for reserved words is case in switch else, undef symbol
        switch(this.currCh) {
        
        case '/':
        case '*':
        case '+':
        case '-':
        case '(':
        case ')':
        case ';':
        case ':':
        case '>':
        case '<':
        case '=':
        case ',':
        case '[':
        case ']':
        	
        	// case for :=
        	if (this.currCh == ':') {
            	if (peeky == '='){
            		this.currCh = GetNextChar();
            		result.lexeme += this.currCh;
            	}
            }
        	// case for >=
            else if(this.currCh == '>') {
            	if(peeky == '=') {
            		this.currCh = GetNextChar();
            		result.lexeme += this.currCh;
            	}
            }
        	// case for <= and <>
            else if(this.currCh == '<') {
            	if((peeky == '=')||(peeky == '>')) {
            		this.currCh = GetNextChar();
            		result.lexeme += this.currCh;
            	}
            }
        	// get associated code/mnemonics
            result.code = reserveWords.LookupName(result.lexeme);
            result.mnemonic = mnemonics.LookupCode(result.code);
        	break;
        	
        default:
        	// undefined symbol
        	result.code = UNDEFINED_CODE;
        	result.mnemonic = UNDEFINED_MNEMONIC;
        }
        
        this.currCh = GetNextChar();
        return result;
    }

    // Checks to see if a string contains a valid DOUBLE 
    public boolean doubleOK(String stin) {
        boolean result;
        Double x;
        try {
            x = Double.parseDouble(stin);
            result = true;
        } catch (NumberFormatException ex) {
            result = false;
        }
        return result;
    }

    // Checks the input string for a valid INTEGER

    public boolean integerOK(String stin) {
        boolean result;
        int x;
        try {
            x = Integer.parseInt(stin);
            result = true;
        } catch (NumberFormatException ex) {
            result = false;
        }
        return result;
    }
    


    public token GetNextToken() {
        token result = new token();

        currCh = skipWhiteSpace();
        if (isLetter(currCh)) { //is identifier
            result = getIdentifier();
        } else if (isDigit(currCh)) { //is numeric
            result = getNumber();
        } else if (isStringStart(currCh)) { //string literal
            result = getString();
        } else //default char checks
        {
            result = getOtherToken();
        }

        if ((result.lexeme.equals("")) || (EOF)) {
            result = null;
        }
//set the mnemonic
        if (result != null) {
// THIS LINE REMOVED-- PUT BACK IN TO USE LOOKUP            
//            result.mnemonic = mnemonics.LookupCode(result.code);
            if (printToken) {
                System.out.println("\t" + result.mnemonic + " | \t" + String.format("%04d", result.code) + " | \t" + result.lexeme);
            }
        }
        return result;

    }

}
