/*
Oscar Santiago 
CS4100
11/11/2022

Part A of SYNTAX ANALYZER

"Syntax Analysis or Parsing is the second phase, i.e. after lexical analysis.
It checks the syntactical structure of the given input, i.e. whether the given
input is in the correct syntax (of the language in which the input has been
written) or not. It does so by building a data structure, called a Parse 
tree or Syntax tree. The parse tree is constructed by using the pre-defined
Grammar of the language and the input string. If the given input string
can be produced with the help of the syntax tree (in the derivation 
process), the input string is found to be in the correct syntax. 
if not, the error is reported by the syntax analyzer." (GeeksForGeeks)

https://www.geeksforgeeks.org/introduction-to-syntax-analysis-in-compiler-design/

Part B of this third step of the compiler project builds the rest of the recursive descent Parser
from Part A to fill out the remaining language features

WITH/ 
 SWITCHABLE TEST MODE ENTRY/EXIT MESSAGES
 GENERATE ERROR MESSAGES. PART B requires more meaningful error handling
than Part A needed
RECOVER FROM ERRORS.
and 


<variable> -> <identifier> Note: this non-terminal is for type-checking
<relexpression> -> <simple expression> <relop> <simple expression>
<relop> -> $EQ | $LSS | $GTR | $NEQ | $LEQ | $GEQ
<simple expression>-> [<sign>] <term> {<addop> <term>}*
<addop> -> $PLUS | $MINUS
<sign> -> $PLUS | $MINUS
<term> -> <factor> {<mulop> <factor> }*
<mulop> -> $MULT | $DIVIDE
<factor> -> <unsigned constant> |
<variable> |
$LPAR <simple expression> $RPAR
<simple type> -> $INTEGER | $FLOAT | $STRING
<constant> -> [<sign>] <unsigned constant>
<unsigned constant>-> <unsigned number>
<unsigned number>-> $FLOATTYPE | $INTTYPE Token codes 52 or 51
 **note: as defined for Lexical 
<identifier> -> $IDENTIFIER Token code 50
 **note: <letter> {<letter> |<digit> | $ | _ }*
<stringconst> -> $STRINGTYPE Token code 53




 */
package Analysers;
import ADT.*;
/**
 *
 * @author abrouill
 */
public class Syntactic {

	private int errorInt = -1;
    private String filein;              //The full file path to input file
    private SymbolTable symbolList;     //Symbol table storing ident/const
    private Lexical lex;                //Lexical analyzer 
    private Lexical.token token;        //Next Token retrieved 
    private boolean traceon;            //Controls tracing mode 
    private int level = 0;              //Controls indent for trace mode
    private boolean anyErrors;          //Set TRUE if an error happens 
    private int LookupSymbolNegative = -1;// Output of lookup symbol if token not found
    private int UnitVarName = 0;
    private final int symbolSize = 250;
    private Lexical.token peekToken;

    public Syntactic(String filename, boolean traceOn) {
        filein = filename;
        traceon = traceOn;
        symbolList = new SymbolTable(symbolSize);
        lex = new Lexical(filein, symbolList, true);
        lex.setPrintToken(traceOn);
        anyErrors = false;
    }

//The interface to the syntax analyzer, initiates parsing
// Uses variable RECUR to get return values throughout the non-terminal methods    
    public void parse() {
        int recur = 0;
// prime the pump to get the first token to process
        token = lex.GetNextToken();
// call PROGRAM
        recur = Program();
    }
    
    private Boolean CheckSymbolTable() {
    	
    	Boolean check = false;
    	int symbolNumber = symbolList.LookupSymbol(token.lexeme);
    	
    	// not the first identfier associated w/ unit name
    	if((symbolNumber == LookupSymbolNegative) || (symbolNumber == UnitVarName)) {
    		return false;
    	}
    	// This variable was delcared and used, and allow in
    	else if((symbolList.table[symbolNumber].getUsage() == 'P')){
    		return true;
    	}
    	else {
    		return false;
    	}
    	
    }

//Non Terminal PROGIDENTIFIER is fully implemented here, leave it as-is.        
    private int ProgIdentifier() {
        int recur = 0;
        if (anyErrors) {
            return -1;
        }

        // This non-term is used to uniquely mark the program identifier
        else if (token.code == lex.codeFor("IDENT")) {
            // Because this is the progIdentifier, it will get a 'P' type to prevent re-use as a var
            symbolList.UpdateSymbol(symbolList.LookupSymbol(token.lexeme), 'P', 0);
            //move on
            token = lex.GetNextToken();
        }
        return recur;
    }

//Non Terminal PROGRAM is fully implemented here.    
    private int Program() {
        int recur = 0;
        if (anyErrors) {
            return -1;
        }
        trace("Program", true);
        if (token.code == lex.codeFor("UNITT")) {
            token = lex.GetNextToken();
            recur = ProgIdentifier();
            if (token.code == lex.codeFor("SEMCO")) {
                token = lex.GetNextToken();
                recur = Block();
                if (token.code == lex.codeFor("PERID")) {
                    if (!anyErrors) {
                        System.out.println("Success.");
                    } else {
                        System.out.println("Compilation failed.");
                    }
                } else {
                    error(lex.reserveFor("PERID"), token.lexeme);
                }
            } else {
                error(lex.reserveFor("SEMCO"), token.lexeme);
            }
        } else {
            error(lex.reserveFor("UNITT"), token.lexeme);
        }
        trace("Program", false);
        return recur;
    }

//Non Terminal BLOCK is fully implemented here.    
    private int Block() {
        int recur = 0;
        if (anyErrors) {
            return -1;
        }
        trace("Block", true);
        while(token.code == lex.codeFor("VARRR")) {
        	recur = VariableDecSec();
        }
        if (token.code == lex.codeFor("BEGIN")) {
            token = lex.GetNextToken();
            recur = Statement();
            while ((token.code == lex.codeFor("SEMCO")) && (!lex.EOF()) && (!anyErrors)) {
                token = lex.GetNextToken();
                recur = Statement();
            }
            if (token.code == lex.codeFor("ENDDD")) {
                token = lex.GetNextToken();
            } else {
                error(lex.reserveFor("ENDDD"), token.lexeme);
            }

        } else {
            error(lex.reserveFor("BEGIN"), token.lexeme);
        }

        trace("Block", false);
        return recur;
    }

//Not a NT, but used to shorten Statement code body for readability.   
    //<variable> $COLON-EQUALS <simple expression>
    private int handleAssignment() {
        int recur = 0;
        if (anyErrors) {
            return -1;
        }
        trace("handleAssignment", true);
        //have ident already in order to get to here, handle as Variable
        recur = Variable();  //Variable moves ahead, next token ready

        if (token.code == lex.codeFor("ASIGN")) {
            token = lex.GetNextToken();
            recur = SimpleExpression();
        } else {
            error(lex.reserveFor("ASIGN"), token.lexeme);
        }

        trace("handleAssignment", false);
        return recur;
    }

// NT This is dummied in to only work for an identifier. 
//  It will work with the SyntaxAMiniTest file having ASSIGNMENT statements
//     containing only IDENTIFIERS.  TERM and FACTOR and numbers will be
//     needed to complete Part A.
// SimpleExpression MUST BE 
//  COMPLETED TO IMPLEMENT CFG for <simple expression>
    
    // Begin my code part
    private int SimpleExpression() {
        int recur = 0; 
        if (anyErrors) {
            return -1;
        }
        
        trace("SimpleExpression", true);
        
        // [<sign>] 
        // ooptional + or - can be added in front of the inital term
        if((token.code == lex.codeFor("ADD:)"))||(token.code == lex.codeFor("SUBTR"))){
        	recur = Sign();
        }
        
        // <term> 
        // The beginning terminal for a simple expr should be an identifier, float, int, or left par
        if ((token.code == lex.codeFor("IDENT"))||(token.code == lex.codeFor("PAR_L"))
        		||(token.code == lex.codeFor("FLOAT"))||(token.code == lex.codeFor("INTEG"))) {
        	recur = Term();
        }
        // if here and the token isnt a simple expr, raise error:o
        else {
        	error("SimpleExpression", token.lexeme);
        }
        
        // {<addop> <term>}* -> expressed in while statement
        // test token against rule until end
        while((token.code == lex.codeFor("ADD:)")||(token.code == lex.codeFor("SUBTR")))) {
        	// enter optional simple expression field
        	// 
        	recur = Addop();
        	recur = Term();
        	// if a -1 was raised, break and leave loop
        	if(recur == errorInt) {
        		break;
        	}
      
        }
        
        trace("SimpleExpression", false);
        return recur;
    }
    
    // Term NT, finds if var, (, or number are used
    private int Term() {
    	int recur = 0; // Return value used l8r
    	if (anyErrors) {
    		return -1;
    	}
    	trace("Term", true);
    	
    	// first factor, must have one
    	// <factor>
    	if ((token.code == lex.codeFor("IDENT"))||(token.code == lex.codeFor("PAR_L"))
        		||(token.code == lex.codeFor("FLOAT"))||(token.code == lex.codeFor("INTEG"))) {
        	recur = Factor();
        }
    	// error flag if no init factor
        else {
        	error("Number, Variable or '('", token.lexeme);
        }
    	
    	// {<mulop> <factor>}*
    	// optional field, keeps checking if * or / are used in the current token
    	// if not continue as normal, if it is: loop, get next factor, until end condition met  
        while(token.code == lex.codeFor("MULTI")||(token.code == lex.codeFor("DIVID"))) {
        	// 
        	recur = Mulop();
        	recur = Factor();
        	// if a -1 was raised, break and leave loop
        	if(recur == errorInt) {
        		break;
        	}
        }
    	
    	trace("Term", false);
    	return recur;
    }
    
    // Mulop Terminal: bookkeeps * or /
    private int Mulop() {
    	int recur = 0; // Return value used l8r
    	if (anyErrors) {
    		return -1;
    	}
    	trace("Mulop", true);
    	// must have a * or / used
        if ((token.code == lex.codeFor("MULTI")||(token.code == lex.codeFor("DIVID")))){
            // bookkeeping and move on
        	token = lex.GetNextToken();
        }
        // error if mul or div not found
        else {
            error("Mulop", token.lexeme);
        }
    	trace("Mulop", false);
    	return recur;

}
    
    // Factor NT that distinguishes unsigned cons, vars, and ( <sim. exp. )
    private int Factor() {
    	int recur = 0; // Return value used l8r
    	if (anyErrors) {
    		return -1;
    	}
    	trace("Factor", true);
    	// was gonna do a very cool switch statement, but java denied me what the heck :(
    	
    	// Token's terminal is a Float or int, enter unsigned constant NT
    	if((token.code == lex.codeFor("FLOAT")||(token.code == lex.codeFor("INTEG")))) {
    		recur = UnsignedConstant();
    	}
    	// if not float then check for variable
    	else if(token.code == lex.codeFor("IDENT")){
    		recur = Variable();
    	}
    	// if a left parth, then check if $LPAR <sim. exp.> $RPAR chain
    	else if(token.code == lex.codeFor("PAR_L")) {
    		// enter simple expression
    		// move to next token after (
    		token = lex.GetNextToken();
    		recur = SimpleExpression();
    		if(token.code == lex.codeFor("PAR_R")) {
    			// Yay!!! the left par has a pair:] move onto next token!
    			token = lex.GetNextToken();
    		}
    		else {
    			// no pair: something is wrong raise error 
    			error("')'", token.lexeme);
    		}
    	}
    	// no var, (, or number, raise error
    	else {
    		error("Number, Variable or '('", token.lexeme);
    	}
    	
    	trace("Factor", false);
    	return recur;
    	
    }
    
    // Unsigned constant nonterminal, goes to unsigned number terminal
    private int UnsignedConstant() {
    	int recur = 0; // Return value used l8r
    	if (anyErrors) {
    		return -1;
    	}
    	trace("UnsignedConstant", true);
    	// if float or int, go to unsignednumber terminal
        if ((token.code == lex.codeFor("FLOAT")||(token.code == lex.codeFor("INTEG")))){
            // bookkeeping and move on
           recur = UnsignedNumber();
        }
        // raise error
        else {
            error("UnsignedConstant", token.lexeme);
        }
    	trace("UnsignedConstant", false);
    	return recur;
    	
    }
    
    // unsigned number terminal, finds if float or int, does bookeeping stuff
    private int UnsignedNumber() {
    	int recur = 0; // Return value used l8r
    	if (anyErrors) {
    		return -1;
    	}
    	trace("UnsignedNumber", true);
        if ((token.code == lex.codeFor("FLOAT")||(token.code == lex.codeFor("INTEG")))){
            // bookkeeping and move on
           token = lex.GetNextToken();
        }
        else {
            error("UnsignedNumber", token.lexeme);
        }
    	trace("UnsignedNumber", false);
    	return recur;
    	
    }
    
    // Finds the sign used in a sim. exp (terminal)
    private int Sign() {
    	int recur = 0; // Return value used l8r
    	if (anyErrors) {
    		return -1;
    	}
    	trace("Sign", true);
    	
    	// check if + or -
        if ((token.code == lex.codeFor("ADD:)")||(token.code == lex.codeFor("SUBTR")))){
            // bookkeeping and move on
           token = lex.GetNextToken();
        }
        // raise error if not plusle or minun
        else {
            error("Sign", token.lexeme);
        }
    	trace("Sign", false);
    	return recur;
    	
    }
    
    // find the sign used in a simple expression, terminal
    private int Addop() {
    	int recur = 0; // Return value used l8r
    	if (anyErrors) {
    		return -1;
    	}
    	trace("Addop", true);
    	// check if + or -
        if ((token.code == lex.codeFor("ADD:)")||(token.code == lex.codeFor("SUBTR")))){
            // bookkeeping and move on
           token = lex.GetNextToken();
        }
        // raise error if not plusle or minun 
        else {
            error("Addop", token.lexeme);
        }
    	trace("Addop", false);
    	return recur;
    	
    }
    //<relexpression> -> <simple expression> <relop> <simple expression>
    private int RelExpression() {
    	int recur = 0;
    	if (anyErrors) {
    		return -1;
    	}
    	trace("RelExpression", true);
    	recur = SimpleExpression();
    	recur = Relop();
    	recur = SimpleExpression();
    	return recur;
    	
    }
    
    // <variable-dec-sec> -> $VAR <variable-declaration>
    private int VariableDecSec() {
    	int recur = 0; // Return value used l8r
    	if (anyErrors) {
    		return -1;
    	}
    	trace("VariableDecSec", true);
    	if(token.code == lex.codeFor("VARRR")) {
    		// move on, bookeeping stuff
    		token = lex.GetNextToken();
    	}
    	else {
    		error("VAR", token.lexeme);
    	}
    	
    	recur = VariableDeclaration();
    	
    	trace("VariableDecSec", false);
    	return recur;
    }
    
    // <variable-dec-sec> -> $VAR <variable-declaration>
    private int VariableDeclaration() {
    	int recur = 0; // Return value used l8r
    	if (anyErrors) {
    		return -1;
    	}
    	trace("VariableDeclaration", true);
    	if(token.code == lex.codeFor("IDENT")) {
    		recur = ProgIdentifier();
    	}
    	else {
    		error("Identifier", token.lexeme);
    	}
    	
    	while(token.code == lex.codeFor("COMMA")) {
    		// repeatable comma check, keep looping and finding 
    		// identifiers until no more commas
    		// optional
    		token = lex.GetNextToken();
    		recur = ProgIdentifier();
    	}
    	if(token.code == lex.codeFor("COLON")) {
    		// check if COLON, if yes move on , if no raise error
    		// bookkeeping
    		token = lex.GetNextToken();
    	}
    	else {
    		error("COLON", token.lexeme);
    	}
    	
    	recur = SimpleType();
    	
    	if(token.code == lex.codeFor("SEMCO")) {
    		// check if SemiColon, if yes move on , if no raise error
    		// bookkeeping
    		token = lex.GetNextToken();
    	}
    	else {
    		error("SEMICOLON", token.lexeme);
    	}
    	
    	
    	trace("VariableDeclaration", false);
    	return recur;
    }
    // <simple type> -> $INTEGER | $FLOAT | $STRING
    private int SimpleType() {
    	int recur = 0; // Return value used l8r
    	if (anyErrors) {
    		return -1;
    	}
    	trace("SimpleType", true);
    	if (token.code == lex.codeFor("FLOAT")||
    			(token.code == lex.codeFor("INTEG")) ||
    			(token.code == lex.codeFor("STRNG"))) {
    		// end terminal reached, do end terminal things here
    		// for now just move onto next token
    		// bookkeeping
    		token = lex.GetNextToken();
    	}
    	else {
    		error("FLOAT, INTEGER, OR STRING", token.lexeme);
    	}
    	
    	trace("SimpleType", false);
    	return recur;
    }
    
    // <block-body> -> $BEGIN <statement> {$SCOLN <statement>} $END
    private int BlockBody() {
    	int recur = 0;
    	if (anyErrors) {
    		return -1;
    	}
    	
    	token = lex.GetNextToken();
    	
    	// Begin goes into statement
    	recur = Statement();
    	// While there is a semicolon, go to the statement afterwards
    	// let the statement call handle any errors/the statement things
    	// when the token code no longer a semicolon, leave next stage of CFG
    	while(token.code == lex.codeFor("SEMCO")) {
    		recur = Statement();
    	}
    	if(token.code == lex.codeFor("ENDDD")) {
    		// Final
    		token = lex.GetNextToken();
    	}
    	// error getting $ENDDD token, its and error:(
    	else {
    		error("Block Body", token.lexeme);
    	}
    	return recur;
    }
    
    private int Relop() {
    	int recur = 0;
    	if (anyErrors) {
    		return -1;
    	}
    	trace("Relop", true);
    	// again im trying to use a switch here but java is being weird
    	// and i have no friends who program to help me T_T
    	
    	// if token is: " ==, >, <, <=, >=, <> "
    	// pass through to next step, for now just get next token and move on
    	if((token.code == lex.codeFor("GRTHN")) || (token.code == lex.codeFor("LETHN"))
    			|| (token.code == lex.codeFor("LOETN")) || (token.code == lex.codeFor("EQUAL"))
    			|| (token.code == lex.codeFor("NTEQL"))) {
    		token = lex.GetNextToken();
    	}
    	else {
    		error("Relop", token.lexeme); 
    	}
    	trace("Addop", false);
    	return recur;
    }
    
    private int Stringconst() {
    	int recur = 0;
    	if (anyErrors) {
    		return -1;
    	}
    	trace("StringConst", true);
    	if(token.code == lex.codeFor("STRNG")) {
    		// // end terminal reached/ move on to next token
    		token = lex.GetNextToken();
    	}
    	else {
    		// error how did you get here?
    		error("StringConst", token.lexeme); 
    	}
    	trace("Stringconst", false);
    	return recur;
    }
    
    // end of my part of code

// Eventually this will handle all possible statement starts in 
//    a nested if/else structure. Only ASSIGNMENT is implemented now.
    private int Statement() {
        int recur = 0;
        if (anyErrors) {
            return -1;
        }
        trace("Statement", true);
        
        
        // <variable> $ASSIGN (<simple expression> | <string literal>) 
        if (token.code == lex.codeFor("IDENT")) {  //must be an ASSIGNMENT
            recur = handleAssignment();
        } 
        // $IF <relexpression> $THEN <statement> [$ELSE <statement>]
        else if (token.code == lex.codeFor("IFFFF")) {
        	token = lex.GetNextToken();
        	recur = RelExpression();
        	if(token.code == lex.codeFor("THENN")) {
        		// Terminal, bookkeep and move on
        		token = lex.GetNextToken();
        	}
        	else {
        		error("THEN", token.lexeme);
        	}
        	
        	recur = Statement();
        	//
        	if(token.code == lex.codeFor("ELSEE")) {
        		token = lex.GetNextToken();
        		recur = Statement();
        	}
        } 
        // <block-body> -> $BEGIN <statement> {$SEMCO <statement>} $END
        else if(token.code == lex.codeFor("BEGIN")){
        	recur = BlockBody();
        }
        // $DOWHILE <relexpression> <statement>
        else if(token.code == lex.codeFor("DOWHI")) {
        	token = lex.GetNextToken();
        	recur = RelExpression();
        	recur = Statement();
        }
        //$REPEAT <statement> $UNTIL <relexpression>
        else if(token.code == lex.codeFor("REPEA")) {
        	token = lex.GetNextToken();
        	recur = Statement();
        	if(token.code == lex.codeFor("UNTIL")) {
        		// Terminal, bookkeep and move on
        		token = lex.GetNextToken();
        	}
        	else {
        		error("Failed to recieve Until after statement in Repeat", token.lexeme);
        	}
        	recur = RelExpression();
        }
        //$FOR <variable> $ASSIGN <simple expression>
        //$TO <simple expression> $DO <statement> 
        else if(token.code == lex.codeFor("FORRR")) {
        	token = lex.GetNextToken();
        	// check if variable is in the symbollist
        	// if it is then move on in Variable()
        	if(CheckSymbolTable()) {
        		recur = Variable();
        	}
        	// if not skip var
        	else {
        		identError(token.lexeme);
        		token = lex.GetNextToken();
        	}
        	
        	if(token.code == lex.codeFor("ASIGN")) {
        		// end terminal reached/ move on to next token
        		token = lex.GetNextToken();
        	}
        	else {
        		error("Failed to recieve Assign after repeat", token.lexeme);
        	}
        	recur = SimpleExpression();
        	if(token.code == lex.codeFor("TOOOO")) {
        		// end terminal reached/ move on to next token
        		token = lex.GetNextToken();
        	}
        	else {
        		error("Failed to recieve TO in REPEAT", token.lexeme);
        	}
        	recur = SimpleExpression();
        	if(token.code == lex.codeFor("DOOOO")) {
        		// end terminal reached/ move on to next token
        		token = lex.GetNextToken();
        	}
        	else {
        		error("Failed to recieve DO in REPEAT", token.lexeme);
        	}
        	recur = Statement();	
        }
        else if(token.code == lex.codeFor("WRILN")) {
        	token = lex.GetNextToken();
        	if(token.code == lex.codeFor("PAR_L")) {
        		// end terminal reached/ move on to next token
        		token = lex.GetNextToken();
        	}
        	else {
        		error("Failed to recieve ( in WriteLN", token.lexeme);
        	}
        	
        	// Place holder for <ident>
        	if(token.code == lex.codeFor("IDENT")){
        		recur = ProgIdentifier();
        	}
        	// can be string
        	else if(token.code == lex.codeFor("STRNG")) {
        		recur = Stringconst();
        	}
        	// or finally it can be
        	// <simple expression>-> [<sign>] <term> {<addop> <term>}*
        	else {
        		recur = SimpleExpression();
        	}
        	// One of the 3 options has come back, successfully
        	// Get check for ')'
        	
        	if(token.code == lex.codeFor("PAR_R")) {
        		// end terminal reached/ move on to next token
        		token = lex.GetNextToken();
        	}
        	else {
        		error("Failed to recieve ) in WriteLN", token.lexeme);
        	}
        	
        	
        }
        // $READLN $LPAR <identifier> $RPAR
        else if(token.code == lex.codeFor("READL")) {
        	// $LPAR
        	token = lex.GetNextToken();
        	if(token.code == lex.codeFor("PAR_L")) {
        		// end terminal reached/ move on to next token
        		token = lex.GetNextToken();
        	}
        	// Error if not ( after READLN
        	else {
        		error("Failed to recieve ( in READLN", token.lexeme);
        	}
        	
        	recur = ProgIdentifier();
        	
        	if(token.code == lex.codeFor("PAR_R")) {
        		// end terminal reached/ move on to next token
        		token = lex.GetNextToken();
        	}
        	else {
        		error("Failed to recieve ) in REALN", token.lexeme);
        		
        	}
        	
        	
        }
        else {
        	error("Statement start", token.lexeme);
        	System.out.println("Finding next statement");
        	// leave the current line until the end of statement is reached
        	while(anyErrors) {
        		token = lex.GetNextToken();
            	if((token.code == lex.codeFor("BEGIN"))|| (token.code == lex.codeFor("IFFFF")) ||
            			(token.code == lex.codeFor("DOWHI")) || (token.code == lex.codeFor("REPEA")) ||
            			(token.code == lex.codeFor("FORRR")) || (token.code == lex.codeFor("WRILN")) || 
            			(token.code == lex.codeFor("READL"))) {
            		anyErrors = false;
            		recur = Statement();
            	} 
        	}
        }

        trace("Statement", false);
        return recur;
    }
    

//Non-terminal VARIABLE just looks for an IDENTIFIER.  Later, a
//  type-check can verify compatible math ops, or if casting is required.    
    private int Variable(){
        int recur = 0;
        if (anyErrors) {
            return -1;
        }

        trace("Variable", true);
        if ((token.code == lex.codeFor("IDENT"))) {
           recur = ProgIdentifier();
        }
        else {
            error("Variable", token.lexeme);
        }
        trace("Variable", false);
        return recur;

    }  
    
/**
 * *************************************************
*/
    /*     UTILITY FUNCTIONS USED THROUGHOUT THIS CLASS */
// error provides a simple way to print an error statement to standard output
//     and avoid reduncancy
    private void error(String wanted, String got) {
        anyErrors = true;
        System.out.println("ERROR: Expected " + wanted + " but found " + got);
    }
    
    private void identError(String ident) {
    	System.out.println("Undeclared Identifier: " + ident);
    }

// trace simply RETURNs if traceon is false; otherwise, it prints an
    // ENTERING or EXITING message using the proc string
    private void trace(String proc, boolean enter) {
        String tabs = "";

        if (!traceon) {
            return;
        }

        if (enter) {
            tabs = repeatChar(" ", level);
            System.out.print(tabs);
            System.out.println("--> Entering " + proc);
            level++;
        } else {
            if (level > 0) {
                level--;
            }
            tabs = repeatChar(" ", level);
            System.out.print(tabs);
            System.out.println("<-- Exiting " + proc);
        }
    }

// repeatChar returns a string containing x repetitions of string s; 
//    nice for making a varying indent format
    private String repeatChar(String s, int x) {
        int i;
        String result = "";
        for (i = 1; i <= x; i++) {
            result = result + s;
        }
        return result;
    }
  
}
