package fr.uga_ctj.panda;

import lombok.Getter;

import java.io.FileInputStream;
import java.util.Map;
import java.util.Set;

public class DataFrame {
    static class Obj<T> {
        @Getter
        private T value;
    }

    private Map<String, Obj[]> map;

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
        String out = new String();
        Set<String> keys = this.map.keySet();
        out += "      ";

        for (String key : keys){
            out += "["+key+"] - ";

        }
        out += "\n";
        out += "      ";

        for(int i = 0; i <this.length(); i++){
            for(String key : keys){
                out += " "+this.map.get(key)[i]+"  - ";

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
        throw new RuntimeException();
    }
}
