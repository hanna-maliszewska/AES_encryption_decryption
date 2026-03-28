package model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AESTest {

    @Test
    void testStateConversion() {
        byte[][] state = new byte[4][4];
        state[0][0] = 0x53;

        AES.subBytes(state);

        System.out.println(Integer.toHexString(state[0][0] & 0xFF));
    }

    @Test
    void testShiftRows() {
        byte[][] state = new byte[4][4];
        byte[][] test = {
                {0, 0, 0, 0},
                {11, 12, 13, 10},
                {22, 23, 20, 21},
                {33, 30, 31, 32},
        };

        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {

                state[row][col] = (byte) (10 * row + col);
            }
        }

        state = AES.shiftRows(state);

        assertArrayEquals(state, test);
    }
}