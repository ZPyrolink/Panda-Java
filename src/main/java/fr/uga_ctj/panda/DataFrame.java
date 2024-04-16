package fr.uga_ctj.panda;

import lombok.Getter;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

public class DataFrame {
    static class Obj<T> {
        @Getter
        private T value;
    }

    private Map<String, Obj[]> Data;

    //region Ctor

    public DataFrame(String[] label, Object[] values) {

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

    public Map<String, Double> mean() {
        Map<String,Double> output = new HashMap<>();
        for(String key : Data.keySet())
        {
            double tmp=0;
            if(!(Data.get(key)[0].getValue() instanceof Long || Data.get(key)[0].getValue() instanceof String))
            {
                for (int j = 0; j < Data.get(key).length; j++) {
                    tmp += (double) Data.get(key)[j].getValue();
                }
                tmp/=Data.get(key).length;
                output.put(key,tmp);
            }
        }
        return output;
    }

    public Map<String, Obj> min() {
        throw new RuntimeException();
    }

    public Map<String, Float> max() {
        throw new RuntimeException();
    }

    public int length() {
        throw new RuntimeException();
    }
}
