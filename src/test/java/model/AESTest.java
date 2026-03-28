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

    @Test
    void testMixColumns() {
        byte[][] state = new byte[4][4];
        state[0][0] = (byte)0xd4;
        state[1][0] = (byte)0xbf;
        state[2][0] = (byte)0x5d;
        state[3][0] = (byte)0x30;

        AES.mixColumns(state);

        // oczekiwany wynik dla pierwszej kolumny
        byte[] expected = new byte[] {(byte)0x04, (byte)0x66, (byte)0x81, (byte)0xe5};

        byte[] result = new byte[4];
        for (int i = 0; i < 4; i++) {
            result[i] = state[i][0];
        }

        assertArrayEquals(expected, result);
    }
}