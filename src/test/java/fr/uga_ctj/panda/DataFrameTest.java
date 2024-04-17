package fr.uga_ctj.panda;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

class DataFrameTest {
    private DataFrame frame;

    private String[] labels;
    private Object[][] data;

    @BeforeEach
    void initTest() {
        data = new Object[][] {
                new Object[] { 0, 0f, 'a' },
                new Object[] { 1, 1.5f, 'b' },
                new Object[] { 2, 2.9f, 'c' },
                new Object[] { 3, 3.2f, 'd' },
        };

        labels = new String[] { "l1", "l2", "l3" };
        Object[][] values = new Object[labels.length][data.length];

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

    @ParameterizedTest
    @ValueSource(ints = { 0,1 })
    void testMean(int axis) {

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
}