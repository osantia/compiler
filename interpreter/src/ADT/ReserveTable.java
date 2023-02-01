package ADT;

import java.util.*;
import java.io.*;

public class ReserveTable {

    //
    ArrayList<String> nameT= new ArrayList<String>();
    ArrayList<Integer> codeT = new ArrayList<Integer>();
    int max;
    public int notFound = -1;
    
    public ReserveTable(int maxSize) {
        max = maxSize;
    }
    public int Add(String name, int code) {
        if (nameT.size() < max) {
            nameT.add(name);
            codeT.add(code);
            int ind = nameT.size() - 1;
            return ind;
        }
        else {
            return -1;
        }
    }

    public int LookupName(String name) {
        int nameIndex = -1;
        for(int i = 0; i < nameT.size(); i++) {
            String j = nameT.get(i);
            if (j.compareToIgnoreCase(name) == 0) {
            	nameIndex = i;
                nameIndex = codeT.get(nameIndex);
                break;
            }
        }
        return nameIndex;
    }
    
    public String LookupCode(int code) {
        String codeName = "";
        // for the size of the array list
        for(int i = 0; i < nameT.size(); i++) {
        	// get integer code of current
            int j = codeT.get(i);
            if (code == j) {
                codeName = nameT.get(i);
                break;
            }
        }
        return codeName;
    }

    String pad(String input, int len, boolean left) {
        while (input.length() < len){
            if (left)
                input = " " +input ;
            else
                input = input + " ";
        }
        return input;
    }
    public String PrintRow(int i) {
        String myNumberString = pad(pad(Integer.toString(i),3,true),8,false);
        String name = pad(pad(nameT.get(i),3,true),8,false);
        String code = pad(pad(Integer.toString(codeT.get(i)),3,true),8,false);
        String row = myNumberString + name + code;
        return row;
    }

    public void PrintReserveTable(String filename){
        String ind = pad(pad("Index",3,true),8,false);
        String nam = pad(pad("Name",3,true),8,false);
        String cod = pad(pad("Code",3,true),8,false);
        String labels = ind + nam + cod;
        try {
            FileOutputStream outputStream = new FileOutputStream(filename);
//	            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, "UTF-16");
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
            BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
            bufferedWriter.write(labels);
            bufferedWriter.newLine();
            for(int i = 0; i < nameT.size(); i++) {
                bufferedWriter.write(PrintRow(i));
                bufferedWriter.newLine();
            }
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}