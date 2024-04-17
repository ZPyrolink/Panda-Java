package fr.uga_ctj.panda;

import lombok.Getter;

import java.io.*;
import java.util.*;

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

            for (int j = 0; j < values[i].length; j++) {
                Obj obj;
                if (values[i][0] instanceof Integer) {
                    obj = new Obj<>((Integer) values[i][j]);
                } else if (values[i][0] instanceof Double) {
                    obj = new Obj<>((Double) values[i][j]);
                } else if (values[i][0] instanceof Float) {
                    obj = new Obj<>((Float) values[i][j]);
                } else if (values[i][0] instanceof Character) {
                    obj = new Obj<>((Character) values[i][j]);
                } else if (values[i][0] instanceof Long) {
                    obj = new Obj<>((Long) values[i][j]);
                } else if (values[i][0] instanceof Short) {
                    obj = new Obj<>((Short) values[i][j]);
                } else {
                    throw new RuntimeException();
                }

                currentCol[j] = obj;
            }
            Data.put(label[i], currentCol);
        }

    }

    public DataFrame(File file) {
        String name = file.getName();
        int length = name.length();
        String[] split = name.split(".");

        if (split.length == 0 || split[1].compareTo("csv") != 0) {
            throw new RuntimeException();
        }
        try (BufferedReader ReadFile = new BufferedReader(new InputStreamReader(new FileInputStream(file)));) {
            String line = ReadFile.readLine();
            if (line == null) {
                System.out.println("File is empty");

            } else {
                Data = new HashMap<>();
                String[] Row = line.split(",");
                String[] Columns;
                Obj[] DataConverted;
                int k = 0;
                while ((line = ReadFile.readLine()) != null) {
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
    //use to convert string to a object
    {

        //it's a Integer
        try {
            return new Obj<>((Integer) Integer.parseInt(data));
        } catch (NumberFormatException e) {
        }

        //it's a Float
        try {
            return new Obj<>((Float) Float.parseFloat(data));
        } catch (NumberFormatException e) {
        }

        //it's a Long
        try {
            return new Obj<>((Long) Long.parseLong(data));
        } catch (NumberFormatException e) {
        }

        //it's a double
        try {
            return new Obj<>((Double) Double.parseDouble(data));
        } catch (NumberFormatException e) {
        }

        //it's a short
        try {
            return new Obj<>((Short) Short.parseShort(data));
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
        throw new RuntimeException();
    }

    public String toString(int nb) {
        throw new RuntimeException();
    }

    public String toString(int min, int max) {
        throw new RuntimeException();
    }

    //endregion

    //region stats

    private double numeric2double(Object obj) {
        return switch (obj) {
            case Byte b -> Double.valueOf(b);
            case Short s -> Double.valueOf(s);
            case Integer i -> Double.valueOf(i);
            case Long l -> Double.valueOf(l);
            case Float f -> Double.valueOf(f);
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
                    if(Data.get(key)[0].getValue() instanceof Long)
                    {
                        for (int j = 0; j < Data.get(key).length; j++) {
                            tmp += Double.valueOf((int)Data.get(key)[j].getValue());
                        }
                    }
                    else {
                        for (int j = 0; j < Data.get(key).length; j++) {
                            tmp += (double) Data.get(key)[j].getValue();
                        }
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
            String[]cols =(String[])Data.keySet().toArray();
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
               for(int j=0;j<width;j++)
               {
                   Obj value=Data.get(cols[j])[i];
                   if(IsArithmetics(value))
                   {
                       if(value.getValue() instanceof Integer)
                       {
                           tmp += Double.valueOf((int)value.getValue());
                       }
                       else
                       {
                           tmp += (double) value.getValue();
                       }
                   }
               }
               tmp/=width;
               output.put(Integer.toString(i),tmp);
           }
        }
        return output;
    }

    private boolean IsArithmetics(Obj data)
    {
        if(data.getValue() instanceof Integer || data.getValue() instanceof Float || data.getValue() instanceof Double || data.getValue() instanceof Long)
            return true;
        return false;
    }

    public Map<String, Double> min() {
        Map<String, Double> result = new HashMap<>();

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

        return result;
    }

    public Map<String, Double> max() {
        Map<String, Double> result = new HashMap<>();

        for (String label : Data.keySet()) {
            Obj[] tmp = Data.get(label);

            if (tmp[0].getValue() instanceof String || tmp[0].getValue() instanceof Character)
                continue;

            Obj<Double>[] objs = (Obj<Double>[]) tmp;
            double currentMax = Double.MIN_VALUE;

            for (Obj obj : objs) {
                double d = numeric2double(obj.getValue());

                if (d > currentMax)
                    currentMax = d;
            }

            result.put(label, currentMax);
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