/*
Name: Oscar Santiago
Course: CS4100
QuadTable.java: maxSize x 4 array which stores different integers
	
	"The QuadTable has unique requirements for its access and stored contents.  Each fixed indexed entry row
consits of four int values representing an opcode and three operands. The data must always be added to the
end of the list, and its index must not change.  This means it can be implemented simply with a 2-
dimensional array of integers, maxSize x 4 wide in dimension."
	
*/
package ADT;

import java.io.*;

public class QuadTable {
    // Integer to count next available row
    private int nextAvailable = 0;
    // quad table ADT
    int[][] quadTable;

    // next Available setter (increments only by 1)
    public void incNA() {
        this.nextAvailable++;
    }

    // Constructor for QuadTable
    public QuadTable(int maxSize){
        quadTable = new int[maxSize][4];
    }

    // get (nextAvailable) value :3
    public int NextQuad(){
        return nextAvailable;
    }

    // Add a new row + Expand active length of quad table + increment nextAvailable
    public void AddQuad(int opcode, int op1, int op2, int op3){
        // Surely theres a better way of doing this with a for loop
        // This is gross
        quadTable[NextQuad()][0] = opcode;
        quadTable[NextQuad()][1] = op1;
        quadTable[NextQuad()][2] = op2;
        quadTable[NextQuad()][3] = op3;
        // increment n.a.
        incNA();
    }

    // Return the row of a quadtable row
    public int[] GetQuad(int index){
        // new array of length 4
        int[] ret = new int[4];
        // Iterate through all members of the particular row
        // Copy members into new array
        for(int i = 0; i < 4; i++) {
            ret[i] = quadTable[index][i];
        }
        return ret;
    }

    // Gets the third operator from the current QuadTable
    // Changes to new value given
    public void UpdateJump(int index, int op3){
        this.quadTable[index][3] = op3;
    }

    // Big Al's big pad func
    String pad(String input, int len, boolean left) {
        while (input.length() < len){
            if (left)
                input = " " +input ;
            else
                input = input + " ";
        }
        return input;
    }

    // Stream to txt file
    public void PrintQuadTable(String filename){
        String ind = pad(pad("Index",3,true),8,false);
        String opc = pad(pad("OpCode",3,true),8,false);
        String op1 = pad(pad("Op1",3,true),8,false);
        String op2 = pad(pad("Op2",3,true),8,false);
        String op3 = pad(pad("Op3",3,true),8,false);
        String labels = ind + opc + op1 + op2 + op3;
        try {
            FileOutputStream outputStream = new FileOutputStream(filename);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
            BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
            bufferedWriter.write(labels);
            bufferedWriter.newLine();

            // double for loop to get all values of string
            for(int i = 0; i < nextAvailable ; i++) {
                // output indicies on left hand side
                bufferedWriter.write(" " + i + " |     ");
                // iterate through the four elements in row -> stream to file
                for(int j = 0; j < 4; j++) {
                    String temp = Integer.toString(quadTable[i][j]);
                    temp += "  |  ";
                    bufferedWriter.write(pad(pad(temp,3,true),8,false));
                }
                bufferedWriter.newLine();
            }
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}