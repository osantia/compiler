package interpreter;
import java.util.Scanner;

import ADT.*;
 

public class Interpreter {

    // Program Counter limit
    int MAXQUAD;
    // optable commands
    ReserveTable optable;

    /*
    Interpreter constructor
    create optable commands
    set MAXQUAD values
    */
    public Interpreter(){
        optable = new ReserveTable(16);
        initReserve(optable);
        this.MAXQUAD = 1000;
    }

    // Code command with associated numbers
    private void initReserve(ReserveTable optable){
        optable.Add("STOP", 0);
        optable.Add("DIV", 1);
        optable.Add("MUL", 2);
        optable.Add("SUB", 3);
        optable.Add("ADD", 4);
        optable.Add("MOV", 5);
        optable.Add("PRINT", 6);
        optable.Add("READ", 7);
        optable.Add("JMP", 8);
        optable.Add("JZ", 9);
        optable.Add("JP", 10);
        optable.Add("JN", 11);
        optable.Add("JNZ", 12);
        optable.Add("JNP", 13);
        optable.Add("JNN", 14);
        optable.Add("JINDR", 15);
    }

    // Initialize Symbol Table for factorials
    public static void InitSTF(SymbolTable st){
        st.AddSymbol("n", 'V', 10);
        st.AddSymbol("i", 'V', 0);
        st.AddSymbol("product", 'V', 0);
        st.AddSymbol("1", 'C', 1);
        st.AddSymbol("$temp", 'V', 0);
    }
    // Initialize Quad Table for factorials
    public void InitQTF(QuadTable qt) {
        qt.AddQuad(5, 3, 0, 2); //MOV
        qt.AddQuad(5, 3, 0, 1); //MOV
        qt.AddQuad(3, 1, 0, 4); //SUB
        qt.AddQuad(10, 4, 0, 7);//JP
        qt.AddQuad(2, 2, 1, 2); //MUL
        qt.AddQuad(4, 1, 3, 1); //ADD
        qt.AddQuad(8, 0, 0, 2); //JMP
        qt.AddQuad(6, 0, 0, 2); //PRINT
        qt.AddQuad(0, 0, 0, 0); //STOP
    }

    //Initialize Symbol Table for summation
    public void InitSTS(SymbolTable st) {
        st.AddSymbol("n", 'V', 10);
        st.AddSymbol("i", 'V', 0);
        st.AddSymbol("product", 'V', 0);
        st.AddSymbol("1", 'C', 1);
        st.AddSymbol("$temp", 'V', 0);
    }

    //Initialize Quad Table for summation
    public void InitQTS(QuadTable qt) {
        qt.AddQuad(5, 3, 0, 2); //MOV
        qt.AddQuad(5, 3, 0, 1); //MOV
        qt.AddQuad(3, 1, 0, 4); //SUB
        qt.AddQuad(10, 4, 0, 7);//JP
        qt.AddQuad(4, 2, 1, 2); //ADD
        qt.AddQuad(4, 1, 3, 1); //ADD
        qt.AddQuad(8, 0, 0, 2); //JMP
        qt.AddQuad(6, 0, 0, 2); //PRINT
        qt.AddQuad(0, 0, 0, 0); //STOP
    }

    // Set given symbol and quad table to required values for factorial
    public boolean initializeFactorialTest(SymbolTable stable, QuadTable qtable) {
        InitSTF(stable);
        InitQTF(qtable);
        return true;
    }

    // Set given symbol and quad table to required values for summation
    public boolean initializeSummationTest(SymbolTable stable, QuadTable qtable){
        InitSTS(stable);
        InitQTS(qtable);
        return true;
    }

    // trace string function that returns program counter + opcode + op1 + op2 + op3
    private String makeTraceString(int pc, int opcode,int op1,int op2,int op3 ){
        String result = "";
        result = "PC = "+String.format("%04d", pc)+": "+(optable.LookupCode(opcode)+"     ").substring(0,6)+String.format("%02d",op1)+
                ", "+String.format("%02d",op2)+", "+String.format("%02d",op3);
        return result;
    }

    // Interpret Function
    public void InterpretQuads(QuadTable Q, SymbolTable S, boolean TraceOn, String filename){
        // initialize program counter at 0
        int PC = 0;
        // initialize quad vars
        int opcode, op1, op2, op3;

        // while Program counter is larger than the max allowed value
        while(PC < MAXQUAD){
            opcode = Q.GetQuad(PC)[0];
            op1 = Q.GetQuad(PC)[1];
            op2 = Q.GetQuad(PC)[2];
            op3 = Q.GetQuad(PC)[3];

            // if opcode is not between 1-15
            if(opcode < -1 && opcode > 16) {
                System.out.println("Opcode Out of Bounds");
                break;
            }
            switch(opcode){
                // STOP
                case 0:
                    System.out.println("Execution termintated by program stop.");
                    PC = MAXQUAD;
                    break;
                // DIV
                case 1:
                    S.table[op3].setIntVal(S.table[op1].getIntVal() / S.table[op2].getIntVal());
                    PC += 1;
                    break;
                // MUL
                case 2:
                    S.table[op3].setIntVal(S.table[op1].getIntVal() * S.table[op2].getIntVal());
                    PC += 1;
                    break;
                // SUB
                case 3:
                    S.table[op3].setIntVal(S.table[op1].getIntVal() - S.table[op2].getIntVal());
                    PC += 1;
                    break;
                // ADD
                case 4:
                    S.table[op3].setIntVal(S.table[op1].getIntVal() + S.table[op2].getIntVal());
                    PC += 1;
                    break;
                // MOV
                case 5:
                    S.table[op3].setIntVal(S.table[op1].getIntVal());
                    PC += 1;
                    break;
                // PRINT
                case 6:
                    System.out.println(S.table[op3].getIntVal());
                    PC += 1;
                    break;
                // READ
                case 7:
                    Scanner sc = new Scanner(System.in);
                    int val = sc.nextInt();
                    S.table[op3].setIntVal(val);
                    PC += 1;
                    break;
                // JMP
                case 8:
                    PC = op3;
                    break;
                // JZ
                case 9:
                    if (S.table[op1].getIntVal() == 0) {
                        PC = op3;
                        break;
                    }
                    else {
                        PC += 1;
                        break;
                    }
                // JP
                case 10:
                    if (S.table[op1].getIntVal() > 0) {
                        PC = op3;
                        break;
                    }
                    else {
                        PC += 1;
                        break;
                    }
                // JN
                case 11:
                    if (S.table[op1].getIntVal() < 0) {
                        PC = op3;
                        break;
                    }
                    else {
                        PC += 1;
                        break;
                    }
                // JNZ
                case 12:
                    if (S.table[op1].getIntVal() != 0) {
                        PC = op3;
                        break;
                    }
                    else {
                        PC += 1;
                        break;
                    }
                // JNP
                case 13:
                    if (S.table[op1].getIntVal() <= 0) {
                        PC = op3;
                        break;
                    }
                    else {
                        PC += 1;
                        break;
                    }
                // JNN
                case 14:
                    if (S.table[op1].getIntVal() >= 0) {
                        PC = op3;
                        break;
                    }
                    else {
                        PC += 1;
                        break;
                    }
                // JINDR
                case 15:
                    PC = S.table[op3].getIntVal();
                    break;
            } // end switch
            // if true, print recent quad function
            if(TraceOn) {
                int temp = PC - 1;
                System.out.println(makeTraceString(temp, opcode, op1, op2, op3));
            } // end trace
        } // end while loop
    } // end interpret quad function


}