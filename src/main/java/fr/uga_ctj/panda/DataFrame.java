package fr.uga_ctj.panda;

import lombok.Getter;

import java.io.FileInputStream;
import java.util.*;
import java.io.*;

public class DataFrame {
    static class Obj<T> {
        @Getter
        private T value;

        public Obj(T value) {
            this.value = value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;

            Obj<T> obj = (Obj<T>) o;
            return value.equals(obj.value);
        }
    }

    private Map<String, Obj[]> Data;

    //region Ctor
  
    //x and y are switch
    public DataFrame(String[] label, Object[][] values) {
        Data = new HashMap<>();
        for (int i = 0; i < label.length; i++) {
            Obj[] currentCol = new Obj[values[i].length];
   
            for(int j=0;j<values[i].length;j++)
            {
                //checking all of those type to cast correctly (yes this list isn't exhaustive)
                Obj obj;
                if (values[i][0] instanceof Integer) {
                    obj = new Obj<>((Integer) values[i][j]);
                } else if (values[i][0] instanceof Double) {
                    obj = new Obj<>((Double) values[i][j]);
                } else if (values[i][0] instanceof Float) {
                    obj = new Obj<>((Float) values[i][j]);
                } else if (values[i][0] instanceof Character) {
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
                else if(values[i][0] instanceof Byte)
                {
                    obj = new Obj<>((Byte) values[i][j]);
                }
                else
                {
                    throw new RuntimeException("Type not recognised from this list : Long, Float, Integer, Char, String, Short, Double");
                }

                currentCol[j] = obj;
            }
            Data.put(label[i], currentCol);
        }
    }

    public DataFrame(File file) {
        String name = file.getName();
        int length = name.length();
        String[] split = name.split("\\.");
        //check if file has a format of .csv
        if(split.length==0 || split[1].compareTo("csv")!=0)
        {
            throw new RuntimeException("File isn't a .csv");
        }
        try (BufferedReader ReadFile = new BufferedReader(new InputStreamReader(new FileInputStream(file)));) {
            String line = ReadFile.readLine();
            if (line == null) {
                System.out.println("File is empty");
            }
            else
            {
                Data = new HashMap<>();
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
                    for (int i = 0; i < Columns.length; i++) {
                        DataConverted[i] = Convert(Columns[i]);
                    }
                    Data.put(Row[k], DataConverted);
                    k++;
                }
                ;
            }


        } catch (IOException e) {
            System.out.println(e);
        }

    }

    //endregion

    //region selection

    private Obj Convert(String data)
    //use to convert from a string to an object
    {
        //it's a Byte
        try
        {
            return new Obj<>((Byte)Byte.parseByte(data));
        }catch (NumberFormatException e){}

        //it's a short
        try
        {
            return new Obj<>((Short)Short.parseShort(data));
        }catch (NumberFormatException e){}

        //it's a Integer
        try {
            return new Obj<>((Integer) Integer.parseInt(data));
        } catch (NumberFormatException e) {
        }



        //it's a Long
        try {
            return new Obj<>((Long) Long.parseLong(data));
        } catch (NumberFormatException e) {
        }

        //it's a Double or Float
        try {
            Obj<Double> tmp=new Obj<Double>((Double) Double.parseDouble(data));
            int nbdigit=data.split("\\.")[1].length();
            //if(tmp.getValue()<Float.MAX_VALUE && tmp.getValue()>Float.MIN_VALUE && nbdigit<=7)
            {
            //    return new Obj<Float>((float) Float.parseFloat(data));
            }
            //else
            {
                return tmp;
            }
        } catch (NumberFormatException e) {
        }
        //string or char
        return new Obj<>((String) data);
    }

    /**
     * Get a specific line of the DataFrame
     *
     * @param index Index of the line
     * @return A new DataFrame corresponding to the specified line
     */
    public DataFrame get(int index) {
        if (index >= length())
            throw new IndexOutOfBoundsException("Index " + index + " is out of bound (> " + length() + ")");

        if (index < 0)
            throw new IndexOutOfBoundsException("Index " + index + " is out of bound (< 0)");

        Set<String> keys = Data.keySet();
        int labelsNb = keys.size();

        String[] labels = new String[labelsNb];
        Object[][] values = new Object[labelsNb][1];

        int labelIndex = 0;
        for (String label : keys) {
            labels[labelIndex] = label;
            values[labelIndex][0] = Data.get(label)[index].getValue();
            labelIndex++;
        }

        return new DataFrame(labels, values);
    }

    /**
     * Get a specific column of the DataFrame
     *
     * @param label Label of the column
     * @return A new DataFrame corresponding to the specified column
     */
    public DataFrame get(String label) {
        Obj[] data = Data.get(label);
        if (data == null)
            throw new StringIndexOutOfBoundsException("Label " + label + " doesn't exists !");
        int length = data.length;
        Object[][] values = new Object[1][length];

        for (int i = 0; i < length; i++)
            values[0][i] = data[i].getValue();

        return new DataFrame(new String[] { label }, values);
    }

    //endregion

    //region display

    @Override
    public String toString() {
        String out = "\t";
        Set<String> keys = this.Data.keySet();


        for (String key : keys){
            out += "["+key+"]\t";

        }
        out += "\n";
        for(int i = 0; i <this.length(); i++){
            out += "\t";
            for(String key : keys){
                out += this.Data.get(key)[i].getValue()+" \t";

            }
            out += "\n";

        }
        return out;
    }

    public String firstToString(int nb) {
        String out= "\t";;
        Set<String> keys = this.Data.keySet();


        for (String key : keys){
            out += "["+key+"]\t";

        }
        out += "\n";
        for(int i = 0; i <nb; i++){
            out += "\t";
            for(String key : keys){
                out += this.Data.get(key)[i].getValue()+" \t";

            }
            out += "\n";

        }
        return out;
    }

    public String lastToString(int nb) {
        String out= "\t";;
        Set<String> keys = this.Data.keySet();


        for (String key : keys){
            out += "["+key+"]\t";

        }
        out += "\n";
        for(int i = this.length()-1; i >=this.length()-nb; i--){
            out += "\t";
            for(String key : keys){
                out += this.Data.get(key)[i].getValue()+" \t";

            }
            out += "\n";

        }
        return out;
    }

    public String toString(int min, int max) {
        throw new RuntimeException();
    }

    //endregion

    //region stats

    public double numeric2double(Object obj) {
        return switch (obj) {
            case Byte b -> b;
            case Short s -> s;
            case Integer i -> i;
            case Long l -> l;
            case Float f -> f;
            case Double d -> d;
            case null, default -> throw new RuntimeException("Never happen");
        };
    }

    public Map<String, Double> mean(int axis) {
        Map<String,Double> output = new HashMap<>();
        if(axis==0) {
            for (String key : Data.keySet()) {
                double tmp = 0;
                if (!(Data.get(key)[0].getValue() instanceof String || Data.get(key)[0].getValue() instanceof Character)) {
                        for (int j = 0; j < Data.get(key).length; j++) {
                            tmp += numeric2double(Data.get(key)[j].getValue());
                        }
                    tmp /= Data.get(key).length;
                    output.put(key, tmp);
                }
            }
        }
        else
        {
           int width = Data.keySet().size();
           int[] colSize = new int[width];
            String[]cols =Data.keySet().toArray(String[]::new);
            //get the size of all columns
            int height=0;
           for(int i=0;i<width;i++)
           {
               colSize[i]=Data.get(cols[i]).length;
               if(colSize[i]>height)
                   height=colSize[i];
           }

           for(int i=0;i<height;i++)
           {
               double tmp=0;
               int currentWidth=0;
               for(int j=0;j<width;j++)
               {
                   Obj value=Data.get(cols[j])[i];
                   if(IsArithmetics(value) && colSize[j]>=i)
                   {
                           tmp += numeric2double(value.getValue());
                            currentWidth++;
                   }
               }
               tmp/=currentWidth;
               output.put(Integer.toString(i),tmp);
           }
        }
        return output;
    }

    private boolean IsArithmetics(Obj data)
    {
        if(data.getValue() instanceof Character || data.getValue() instanceof String)
            return false;
        return true;
    }

    public Map<String, Double> min(int axis) {
        Map<String, Double> result = new HashMap<>();

        switch (axis) {
            case 0 -> {
                for (String label : Data.keySet()) {
                    Obj[] tmp = Data.get(label);

                    if (tmp[0].getValue() instanceof String || tmp[0].getValue() instanceof Character)
                        continue;

                    Obj<Double>[] objs = (Obj<Double>[]) tmp;
                    Double currentMin = Double.MAX_VALUE;

                    for (Obj obj : objs) {
                        double d = numeric2double(obj.getValue());

                        if (d < currentMin)
                            currentMin = d;
                    }

                    result.put(label, currentMin);
                }
            }
            case 1 -> {
                for (int i = 0; i < length(); i++) {
                    double currentMin = Double.MAX_VALUE;

                    for (Obj[] value : Data.values()) {
                        if (value[i].getValue() instanceof String || value[i].getValue() instanceof Character)
                            break;

                        double d = numeric2double(value[i].getValue());

                        if (d < currentMin)
                            currentMin = d;
                    }

                    result.put(String.valueOf(i), currentMin);
                }
            }
            default -> throw new IndexOutOfBoundsException("Axis must be 0 or 1");
        }

        return result;
    }

    public Map<String, Double> max(int axis) {
        Map<String, Double> result = new HashMap<>();

        switch (axis) {
            case 0 -> {
                for (String label : Data.keySet()) {
                    Obj[] tmp = Data.get(label);

                    if (tmp[0].getValue() instanceof String || tmp[0].getValue() instanceof Character)
                        continue;

                    Obj<Double>[] objs = (Obj<Double>[]) tmp;
                    double currentMax = -Double.MAX_VALUE;

                    for (Obj obj : objs) {
                        double d = numeric2double(obj.getValue());

                        if (d > currentMax)
                            currentMax = d;
                    }

                    result.put(label, currentMax);
                }
            }
            case 1 -> {
                for (int i = 0; i < length(); i++) {
                    double currentMax = -Double.MAX_VALUE;

                    for (Obj[] value : Data.values()) {
                        if (value[i].getValue() instanceof String || value[i].getValue() instanceof Character)
                            break;

                        double d = numeric2double(value[i].getValue());

                        if (d > currentMax)
                            currentMax = d;
                    }

                    result.put(String.valueOf(i), currentMax);
                }
            }
            default -> throw new IndexOutOfBoundsException("Axis must be 0 or 1");
        }

        return result;
    }

    public int length() {
        return this.Data.get(this.Data.keySet().iterator().next()).length;
    }

    //endregion


    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (o == null || getClass() != o.getClass())
            return false;

        DataFrame dataFrame = (DataFrame) o;
        Set<Map.Entry<String, Obj[]>> entries = Data.entrySet();
        Set<Map.Entry<String, Obj[]>> otherEntries = dataFrame.Data.entrySet();

        if (entries.size() != otherEntries.size())
            return false;

        Iterator<Map.Entry<String, Obj[]>> entriesIt = entries.iterator();
        Iterator<Map.Entry<String, Obj[]>> otherEntriesIt = otherEntries.iterator();

        for (int i = 0; i < entries.size(); i++) {
            Map.Entry<String, Obj[]> entry = entriesIt.next();
            Map.Entry<String, Obj[]> otherEntry = otherEntriesIt.next();

            if (!entry.getKey().equals(otherEntry.getKey()))
                return false;

            if (!Arrays.equals(entry.getValue(), otherEntry.getValue()))
                return false;
        }

        return true;
    }
}