/*
Name: Oscar Santiago
Course: 4100
SymbolTable.java: fixed index list with different data types stored inside each object instance
		"The symbol table conceptually is a fixed index list (i.e., once a row of data has been added, its index must
not change during the run of the program) consisting of an indexed list of entries, with each entry (a
single row of data) containing all of the following: a name field (String type, the lexeme of the token); a
usage field (char to hold a label, variable, or constant indicator, represented by the characters ‘L’, ‘V’, or
‘C’); a dataType field specifying the data as an integer, float, or string (char, represented using ‘I’, ‘F’, or
‘S’); and a set of 3 different value fields of the appropriate type (integerValue, floatValue, stringValue) to
hold one of these: an integer, a double, or a String, as selected by the contents of the dataType field.
(Later, it will be clear why labels will always store their data in the integerValue field, while variables and
constants may use any one of the integerValue, floatValue, stringValue storage areas based on their
dataType field)."
*/
package ADT;

import java.io.*;

public class SymbolTable {

    // Create a symbol, size of table, and index
    public SymTable[] table;
    // Provides the max size of the SymTable
    private int size;
    // Used SymTable counter
    int count = 0;

    // increment the used sym tables by 1
    public void incCount() {
        this.count++;
    }

    // Symbol Table Class Definition
    public class SymTable{
        // Given data for the Sym Table ADT

        private String symbol;
        private char usage;
        private char dataType;
        private int intVal;
        private double doubleVal;
        private String stringVal;

        // Constructor for this thing:-)
        public SymTable(String symbol, char usage, char dataType, int intVal, double doubleVal, String stringVal) {
            super();
            this.symbol = symbol;
            this.usage = usage;
            this.dataType = dataType;
            this.intVal = intVal;
            this.doubleVal = doubleVal;
            this.stringVal = stringVal;
        }


        // Getters and setters for sym table class
        // thank goodness for the eclipse hotkey that
        // generates the getters and setters automatically

        public String getSymbol() {
            return symbol;
        }
        public void setSymbol(String symbol) {
            this.symbol = symbol;
        }
        public char getUsage() {
            return usage;
        }
        public void setUsage(char usage) {
            this.usage = usage;
        }
        public char getDataType() {
            return dataType;
        }
        public void setDataType(char dataType) {
            this.dataType = dataType;
        }
        public int getIntVal() {
            return intVal;
        }
        public void setIntVal(int intVal) {
            this.intVal = intVal;
        }
        public double getDoubleVal() {
            return doubleVal;
        }
        public void setDoubleVal(double doubleVal) {
            this.doubleVal = doubleVal;
        }
        public String getStringVal() {
            return stringVal;
        }
        public void setStringVal(String stringVal) {
            this.stringVal = stringVal;
        }

    }

    // Constructor for SymbolTable
    public SymbolTable(int maxSize){
        table = new SymTable[maxSize];
        size = maxSize;
    }

    /*

          // Checks if symbol is not in the table already
        // If it is, returns the location
        // Next, it checks if the max size == used table space
        // Returns -1 if true else:
        // Creates a SymTable class in array and
        // Sets symbol, usage, and value

     */
    public int AddSymbol(String symbol, char usage, int value){
        // int value to determine if symbol already in the table
        int check = LookupSymbol(symbol);
        if(check != -1) {
            return check;
        }
        else if(size == count){
            return -1;
        }
        else {
            table[count] = new SymTable(symbol, usage, 'I', value, 0.0, "");
            incCount();
            return count - 1;
        }
    }
    public int AddSymbol(String symbol, char usage, double value){
        int check = LookupSymbol(symbol);
        if(check != -1) {
            return check;
        }
        else if(size == count){
            return -1;
        }
        else {
            table[count] = new SymTable(symbol, usage, 'F', 0, value, "");
            incCount();
            return count - 1;
        }
    }
    public int AddSymbol (String symbol, char usage, String value){
        int check = LookupSymbol(symbol);
        if(check != -1) {
            return check;
        }
        else if(size == count){
            return -1;
        }
        else {
            table[count] = new SymTable(symbol, usage, 'S', 0, 0.0, value);
            incCount();
            return count - 1;
        }
    }

    // returns -1 if symbol is not in table
    // else returns the location of the symbol
    public int LookupSymbol(String symbol){
        // return value, will be -1 if not in table, therefore init at -1
        int ret = -1;

        // Iterates through the currently used sym table array
        for(int i = 0; i < count; i++) {
            // if the strings share the same name then return the row
            if (symbol.compareToIgnoreCase(GetSymbol(i)) == 0) {
                ret = i;
                break;
            }
        }
        return ret;
    }

    // Getters. 
    public String GetSymbol(int index){
        return table[index].getSymbol();
    }
    public char getUsage(int index){
        return table[index].getUsage();
    }
    public char GetDataType(int index){
        return table[index].getDataType();
    }
    public String GetString(int index){
        return table[index].getStringVal();
    }
    public int GetInteger(int index){
        return table[index].getIntVal();
    }
    public double GetFloat(int index){
        return table[index].getDoubleVal();
    }

    // Update Symbols, self explanatory code :3
    public void UpdateSymbol(int index, char usage, int value){
        table[index].setUsage(usage);
        table[index].setIntVal(value);
    }
    public void UpdateSymbol(int index, char usage, double value){
        table[index].setUsage(usage);
        table[index].setDoubleVal(value);
    }
    public void UpdateSymbol(int index, char usage, String value){
        table[index].setUsage(usage);
        table[index].setStringVal(value);
    }

    // Padding function given by Big Al on HW1Pt1
    String pad(String input, int len, boolean left) {
        while (input.length() < len){
            if (left)
                input = " " +input ;
            else
                input = input + " ";
        }
        return input;
    }


    // Stream to file
    public void PrintSymbolTable(String filename){
        // Labels of the top row
        String ind = pad(pad("Index",3,true),8,false);
        String name = pad(pad("Name",3,true),20,false);
        String use = pad(pad("Use",3,false),8,true);
        String typ = pad(pad("Typ",3,false),8,true);
        String value = pad(pad("Value",3,false),8,true);
        String labels = ind + name + use + typ + value;
        try {
            FileOutputStream outputStream = new FileOutputStream(filename);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
            BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
            bufferedWriter.write(labels);
            bufferedWriter.newLine();

            // go through table and stream outputs to txt file
            for(int i = 0; i < count; i++) {
                bufferedWriter.write(pad(pad(Integer.toString(i) + " | ",3,true),8,false));
                bufferedWriter.write(pad(pad(table[i].getSymbol(),3,true),20,false));
                bufferedWriter.write(pad(pad(Character.toString(table[i].getUsage())+ "|",3,false),8,true));
                bufferedWriter.write(pad(pad(Character.toString(table[i].getDataType())+ "|",5,false),12,true));

                // Find out which data type used, stream value from sym table
                switch(table[i].getDataType()) {
                    case 'I':
                        bufferedWriter.write(pad(pad(Integer.toString(table[i].getIntVal()),3,true),8,false));
                        break;
                    case 'F':
                        bufferedWriter.write(pad(pad(Double.toString(table[i].getDoubleVal()),3,true),8,false));
                        break;
                    case 'S':
                        bufferedWriter.write(pad(pad(table[i].getStringVal(),3,true),8,false));
                        break;
                }
                bufferedWriter.newLine();
            }
            bufferedWriter.newLine();
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}