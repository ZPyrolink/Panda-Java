package fr.uga_ctj.panda;

import lombok.Getter;

import java.io.*;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;

public class DataFrame {
    static class Obj<T> {
        @Getter
        private T value;

        public Obj(T value) {
            this.value = value;
        }
    }

    private Map<String, Obj[]> map;

    //region Ctor
    //x and y are switch
    public DataFrame(String[] label, Object[][] values) {
        map = new HashMap<>();
        for(int i=0;i<label.length;i++)
        {
            Obj[] currentCol = new Obj[values[i].length];
            
            for(int j=0;j<values[i].length;j++)
            {
                //checking all of those type to cast correctly (yes this list isn't exhaustive)
                Obj obj;
                if(values[i][0] instanceof Integer)
                {
                    obj = new Obj<>((Integer) values[i][j]);
                }
                else if(values[i][0] instanceof Double)
                {
                    obj = new Obj<>((Double) values[i][j]);
                }
                else if(values[i][0] instanceof Float)
                {
                    obj = new Obj<>((Float) values[i][j]);
                }
                else if(values[i][0] instanceof Character)
                {
                    obj = new Obj<>((Character) values[i][j]);
                }
                else if(values[i][0] instanceof Long)
                {
                    obj = new Obj<>((Long) values[i][j]);    
                }
                else if(values[i][0] instanceof Short)
                {
                    obj = new Obj<>((Short) values[i][j]);    
                }
                else if(values[i][0] instanceof String)
                {
                    obj = new Obj<>((String) values[i][j]);
                }
                else
                {
                    throw new RuntimeException("Type not recognised from this list : Long, Float, Integer, Char, String, Short, Double");
                }
                
                currentCol[j]=obj;
            }
            map.put(label[i],currentCol);
        }
            
    }

    public DataFrame(File file) {
        String name = file.getName();
        int length = name.length();
        String[] split = name.split(".");

        //check if file has a format of .csv
        if(split.length==0 || split[1].compareTo("csv")!=0)
        {
            throw new RuntimeException("File isn't a .csv");
        }
        try (BufferedReader ReadFile = new BufferedReader(new InputStreamReader(new FileInputStream(file)));){
            String line=ReadFile.readLine();
            if(line == null)
            {
                System.out.println("File is empty");
            }
            else
            {
                map = new HashMap<>();
                //first line is the list of row names
                String[] Row = line.split(",");
                String[] Columns;
                Obj[] DataConverted;
                int k=0;
                while((line= ReadFile.readLine()) != null)
                {
                    //read column by column
                    Columns = line.split(",");
                    DataConverted = new Obj[Columns.length];
                    for(int i =0;i< Columns.length;i++) {
                        DataConverted[i]=Convert(Columns[i]);
                    }
                    map.put(Row[k],DataConverted);
                    k++;
                };
            }


        }catch(IOException e)
        {
            System.out.println(e);
        }

    }

    //endregion

    //region selection

    public DataFrame get(int index){
        throw new RuntimeException();
    }

    private Obj Convert(String data)
    //use to convert from a string to an object
    {

        //it's a Integer
        try
        {
            return new Obj<>((Integer)Integer.parseInt(data));
        }catch (NumberFormatException e){}

        //it's a Float
        try
        {
            return new Obj<>((Float)Float.parseFloat(data));
        }catch (NumberFormatException e){}

        //it's a Long
        try
        {
            return new Obj<>((Long)Long.parseLong(data));
        }catch (NumberFormatException e){}

        //it's a double
        try
        {
            return new Obj<>((Double)Double.parseDouble(data));
        }catch (NumberFormatException e){}

        //it's a short
        try
        {
            return new Obj<>((Short)Short.parseShort(data));
        }catch (NumberFormatException e){}

        //string or char
        return new Obj<>((String)data);
    }

    public DataFrame get(String label) {
        throw new RuntimeException();
    }

    //endregion

    //region display

    @Override
    public String toString() {
        String out = new String();
        Set<String> keys = this.map.keySet();
        out += "\t";

        for (String key : keys){
            out += "["+key+"]\t";

        }
        out += "\n";
        for(int i = 0; i <this.length(); i++){
            out += "\t";
            for(String key : keys){
                out += this.map.get(key)[i].getValue()+" \t";

            }
            out += "\n";

        }
        return out;
    }

    public String toString(int nb) {
        throw new RuntimeException();
    }

    public String toString(int min, int max) {
        throw new RuntimeException();
    }

    //endregion

    //region stats

    public Map<String, Float> avg() {
        throw new RuntimeException();
    }

    public Map<String, Obj> min() {
        throw new RuntimeException();
    }

    public Map<String, Float> max() {
        throw new RuntimeException();
    }

    public int length() {

        return this.map.get(this.map.keySet().iterator().next()).length;
    }
}