package fr.uga_ctj.panda;


import fr.uga_ctj.panda.DataFrame;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
public class DataFrameTest {
    private DataFrame frame;

    private String[] labels;
    private Object[][] data;

    @BeforeEach
    void initTest() {
        data = new Object[][] {
                new Object[] { 1111, 1000f, 'a' },
                new Object[] { 1000, 1100f, 'b' },
                new Object[] { 2000, 2100f, 'c' },
                new Object[] { 3000, 3100f, 'd' },
        };

        labels = new String[] { "l1", "l2", "l3" };
        Object[][] values = new Object[labels.length][data.length];

        for (int i = 0; i < data.length; i++)
            for (int j = 0; j < labels.length; j++)
                values[j][i] = data[i][j];

        frame = new DataFrame(labels, values);
    }

    @Test
    void testToString(){
        System.out.println(frame.toString());
    }


    @Test
    void testfirstToStringint(){
        for(int i =1; i<=frame.length();i++){
            System.out.println(frame.firstToString(i));
        }
    }

    @Test
    void testlasttToStringint(){
        for(int i =1; i<=frame.length();i++){
            System.out.println(frame.lastToString(i));
        }
    }
}
