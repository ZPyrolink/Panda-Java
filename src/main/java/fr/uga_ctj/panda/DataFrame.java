package fr.uga_ctj.panda;

import lombok.Getter;

import java.io.FileInputStream;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;

public class DataFrame {
    static class Obj<T> {
        @Getter
        private T value;

        public Obj(T value) {
            this.value = value;
        }
    }

    private Map<String, Obj[]> Data;

    //region Ctor
    //x and y are switch
    public DataFrame(String[] label, Object[][] values) {
        Obj[] currentCol = new Obj[values[0].length];
        Data = new HashMap<>();
        for(int i=0;i<label.length;i++)
        {
            currentCol = new Obj[values[i].length];
            
            for(int j=0;j<values[i].length;j++)
            {
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
                else
                {
                    throw new RuntimeException();
                }
                
                currentCol[j]=obj;                
            }
            Data.put(label[i],currentCol);
        }
            
    }

    public DataFrame(FileInputStream stream) {

    }

    //endregion

    //region selection

    public DataFrame get(int index){
        throw new RuntimeException();
    }

    public DataFrame get(String label) {
        throw new RuntimeException();
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

    public Map<String, Float> avg() {
        throw new RuntimeException();
    }

    public Map<String, Obj> min() {
        throw new RuntimeException();
    }

    public Map<String, Float> max() {
        throw new RuntimeException();
    }

    public int length(String label) {
        return this.Data.get(label).length;
    }
}