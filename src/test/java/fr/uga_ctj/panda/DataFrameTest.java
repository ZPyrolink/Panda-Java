package fr.uga_ctj.panda;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.File;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

class DataFrameTest {
    private DataFrame frame;

    private String[] labels;
    private Object[][] data;
    private Object[][] values;

    @BeforeEach
    void initTest() {
        data = new Object[][] { //0.4598915,4.4598915,5.4598915,6.4598915
                new Object[] { 0, 0.003006459891, 0.4598915f, 9_000_000_123_899L, (short) 259/*, (byte) 0*/, 'a' },
                new Object[] { 1, 4.003006459891, 4.4598915f, 100_000_000_123_899L, (short) 260/*, (byte) 1*/, 'b' },
                new Object[] { 2, 2.003006459891, 5.4598915f, 11_000_000_123_899L, (short) 261/*, (byte) 2*/, 'c' },
                new Object[] { 3, 9.003006459891, 6.4598915f, 12_000_000_123_899L, (short) 262/*, (byte) 3*/, 'd' },
        };

        labels = new String[] { "l1", "l2", "l3", "l4", "l5", "l6"/*, "l7"*/ };
        values = new Object[labels.length][data.length];

        for (int i = 0; i < data.length; i++)
            for (int j = 0; j < labels.length; j++)
                values[j][i] = data[i][j];

        frame = new DataFrame(labels, values);
    }

    @Test
    void testToString() {
        System.out.println(frame.toString());
    }


    @Test
    void testfirstToStringint() {
        for (int i = 1; i <= frame.length(); i++) {
            System.out.println(frame.firstToString(i));
        }
    }

    @Test
    void testlasttToStringint() {
        for (int i = 1; i <= frame.length(); i++) {
            System.out.println(frame.lastToString(i));
        }
    }

    private double numeric2double(Object obj) {
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

    @ParameterizedTest
    @ValueSource(ints = { -1, 0, 1, 2, 3, 4 })
    void testIntGet(int i) {
        if (i < 0) {
            testOOBIntGet(i, "Index " + i + " is out of bound (< 0)");
            return;
        }

        if (i >= data.length) {
            testOOBIntGet(i, "Index " + i + " is out of bound (> " + data.length + ")");
            return;
        }

        DataFrame result = frame.get(i);
        Object[][] values = new Object[labels.length][1];

        for (int l = 0; l < labels.length; l++)
            values[l][0] = data[i][l];

        DataFrame expected = new DataFrame(labels, values);

        assertEquals(expected, result);
    }



    @ParameterizedTest
    @ValueSource(ints = { 0, 1 })
    void testMean(int axis) {
        Map<String, Double> expected = new HashMap<>();
        if (axis == 0) {
            for (int l = 0; l < labels.length; l++) {
                if (data[0][l] instanceof String || data[0][l] instanceof Character)
                    continue;

                if (values[l][0] instanceof Byte)
                    expected.put(labels[l], Arrays.stream(values[l]).mapToDouble(b -> (double) (byte) b).average().getAsDouble());
                else if (values[l][0] instanceof Short)
                    expected.put(labels[l], Arrays.stream(values[l]).mapToDouble(b -> (double) (short) b).average().getAsDouble());
                else if (values[l][0] instanceof Integer)
                    expected.put(labels[l], Arrays.stream(values[l]).mapToDouble(b -> (double) (int) b).average().getAsDouble());
                else if (values[l][0] instanceof Long)
                    expected.put(labels[l], Arrays.stream(values[l]).mapToDouble(b -> (double) (long) b).average().getAsDouble());
                else if (values[l][0] instanceof Float)
                    expected.put(labels[l], Arrays.stream(values[l]).mapToDouble(b -> (double) (float) b).average().getAsDouble());
                else if (values[l][0] instanceof Double)
                    expected.put(labels[l], Arrays.stream(values[l]).mapToDouble(b -> (double) (double) b).average().getAsDouble());
            }
        } else {
            for (int d = 0; d < data.length; d++) {
                List<Double> dd = new ArrayList<>();
                for (int l = 0; l < labels.length; l++) {
                    if (data[d][l] instanceof String || data[d][l] instanceof Character)
                        continue;

                    dd.add(frame.numeric2double(data[d][l]));
                }
                expected.put(String.valueOf(d), dd.stream().mapToDouble(Double::doubleValue).average().getAsDouble());
            }
        }
        Map<String, Double> result = frame.mean(axis);

        assertEquals(expected, result);
    }

    void testOOBIntGet(int i, String msg) {
        IndexOutOfBoundsException e = assertThrowsExactly(IndexOutOfBoundsException.class, () -> frame.get(i));
        assertEquals(msg, e.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = { "l", "l1", "l2", "l3", "lnone" })
    void testStringGet(String label) {
        if (!Arrays.asList(labels).contains(label)) {
            StringIndexOutOfBoundsException e = assertThrowsExactly(StringIndexOutOfBoundsException.class, () -> frame.get(label));

            assertEquals(e.getMessage(), "Label " + label + " doesn't exists !");
            return;
        }

        DataFrame result = frame.get(label);
        Object[][] values = new Object[1][data.length];

        int l = Arrays.asList(labels).indexOf(label);

        for (int i = 0; i < data.length; i++)
            values[0][i] = data[i][l];

        DataFrame expected = new DataFrame(new String[] { label }, values);

        assertEquals(expected, result);
    }

    @Test
    void testMin() {
        Map<String, Double> expected = new HashMap<>();

        for (int l = 0; l < labels.length; l++) {
            if (data[0][l] instanceof String || data[0][l] instanceof Character)
                continue;

            if (values[l][0] instanceof Byte)
                expected.put(labels[l], Double.valueOf(Arrays.stream(Arrays.stream(values[l]).toArray(Byte[]::new)).min(Byte::compareTo).get()));
            else if (values[l][0] instanceof Short)
                expected.put(labels[l], Double.valueOf(Arrays.stream(Arrays.stream(values[l]).toArray(Short[]::new)).min(Short::compareTo).get()));
            else if (values[l][0] instanceof Integer)
                expected.put(labels[l], Double.valueOf(Arrays.stream(Arrays.stream(values[l]).toArray(Integer[]::new)).min(Integer::compareTo).get()));
            else if (values[l][0] instanceof Long)
                expected.put(labels[l], Double.valueOf(Arrays.stream(Arrays.stream(values[l]).toArray(Long[]::new)).min(Long::compareTo).get()));
            else if (values[l][0] instanceof Float)
                expected.put(labels[l], Double.valueOf(Arrays.stream(Arrays.stream(values[l]).toArray(Float[]::new)).min(Float::compareTo).get()));
            else if (values[l][0] instanceof Double)
                expected.put(labels[l], Double.valueOf(Arrays.stream(Arrays.stream(values[l]).toArray(Double[]::new)).min(Double::compareTo).get()));
        }

        Map<String, Double> result = frame.min(0);

        assertEquals(expected, result);
    }

    @Test
    void testMin1() {
        Map<String, Double> expected = new HashMap<>();

        for (int d = 0; d < data.length; d++) {
            List<Double> dd = new ArrayList<>();
            for (int l = 0; l < labels.length; l++) {
                if (data[d][l] instanceof String || data[d][l] instanceof Character)
                    continue;

                dd.add(numeric2double(data[d][l]));
            }
            expected.put(String.valueOf(d), dd.stream().mapToDouble(Double::doubleValue).min().getAsDouble());
        }

        Map<String, Double> result = frame.min(1);

        assertEquals(expected, result);
    }

    @ParameterizedTest
    @ValueSource(ints = { -1, 3 })
    void testMinOthers(int axis) {
        IndexOutOfBoundsException e = assertThrowsExactly(IndexOutOfBoundsException.class, () -> frame.min(axis));
        assertEquals("Axis must be 0 or 1", e.getMessage());
    }
    @ParameterizedTest
    @ValueSource(strings = { "./TestFiles/test.csv" })
    void testCSV(String url) {
        File file = new File(url);

        DataFrame frame= new DataFrame(file);

        DataFrame expected = new DataFrame(labels, values);

        assertEquals(expected,frame);
    }
    @Test
    void testMax() {
        Map<String, Double> expected = new HashMap<>();

        for (int l = 0; l < labels.length; l++) {
            if (data[0][l] instanceof String || data[0][l] instanceof Character)
                continue;

            if (values[l][0] instanceof Byte)
                expected.put(labels[l], Double.valueOf(Arrays.stream(Arrays.stream(values[l]).toArray(Byte[]::new)).max(Byte::compareTo).get()));
            else if (values[l][0] instanceof Short)
                expected.put(labels[l], Double.valueOf(Arrays.stream(Arrays.stream(values[l]).toArray(Short[]::new)).max(Short::compareTo).get()));
            else if (values[l][0] instanceof Integer)
                expected.put(labels[l], Double.valueOf(Arrays.stream(Arrays.stream(values[l]).toArray(Integer[]::new)).max(Integer::compareTo).get()));
            else if (values[l][0] instanceof Long)
                expected.put(labels[l], Double.valueOf(Arrays.stream(Arrays.stream(values[l]).toArray(Long[]::new)).max(Long::compareTo).get()));
            else if (values[l][0] instanceof Float)
                expected.put(labels[l], Double.valueOf(Arrays.stream(Arrays.stream(values[l]).toArray(Float[]::new)).max(Float::compareTo).get()));
            else if (values[l][0] instanceof Double)
                expected.put(labels[l], Double.valueOf(Arrays.stream(Arrays.stream(values[l]).toArray(Double[]::new)).max(Double::compareTo).get()));
        }

        Map<String, Double> result = frame.max(0);

        assertEquals(expected, result);
    }

    @Test
    void testMax1() {
        Map<String, Double> expected = new HashMap<>();

        for (int d = 0; d < data.length; d++) {
            List<Double> dd = new ArrayList<>();
            for (int l = 0; l < labels.length; l++) {
                if (data[d][l] instanceof String || data[d][l] instanceof Character)
                    continue;

                dd.add(numeric2double(data[d][l]));
            }
            expected.put(String.valueOf(d), dd.stream().mapToDouble(Double::doubleValue).max().getAsDouble());
        }

        Map<String, Double> result = frame.max(1);

        assertEquals(expected, result);
    }

    @ParameterizedTest
    @ValueSource(ints = { -1, 3 })
    void testMaxOthers(int axis) {
        IndexOutOfBoundsException e = assertThrowsExactly(IndexOutOfBoundsException.class, () -> frame.max(axis));
        assertEquals("Axis must be 0 or 1", e.getMessage());
    }
}