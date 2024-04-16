package fr.uga_ctj.panda;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

class DataFrameTest {
    private DataFrame frame;

    private String[] labels;
    private Object[][] data;
    private Object[][] values;

    @BeforeEach
    void initTest() {
        data = new Object[][] {
                new Object[] { 0, 0d, 0f, 0L, (short) 0/*, (byte) 0*/, 'a' },
                new Object[] { 1, 1d, 1f, 1L, (short) 1/*, (byte) 1*/, 'b' },
                new Object[] { 2, 2d, 2f, 2L, (short) 2/*, (byte) 2*/, 'c' },
                new Object[] { 3, 3d, 3f, 3L, (short) 3/*, (byte) 3*/, 'd' },
        };

        labels = new String[] { "l1", "l2", "l3", "l4", "l5", "l6"/*, "l7"*/ };
        values = new Object[labels.length][data.length];

        for (int i = 0; i < data.length; i++)
            for (int j = 0; j < labels.length; j++)
                values[j][i] = data[i][j];

        frame = new DataFrame(labels, values);
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

        Map<String, Double> result = frame.min();

        assertEquals(expected, result);
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

        Map<String, Double> result = frame.max();

        assertEquals(expected, result);
    }
}