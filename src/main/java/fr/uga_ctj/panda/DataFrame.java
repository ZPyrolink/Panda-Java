package fr.uga_ctj.panda;

import lombok.Getter;

import java.io.FileInputStream;
import java.util.Map;

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

    public int length() {
        throw new RuntimeException();
    }
}
