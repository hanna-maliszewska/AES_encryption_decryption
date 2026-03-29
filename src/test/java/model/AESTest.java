package model;

import org.junit.jupiter.api.Assertions;
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
                {0, 1, 2, 3},
                {11, 12, 13, 10},
                {22, 23, 20, 21},
                {33, 30, 31, 32},
        };

        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {

                state[row][col] = (byte) (10 * row + col);
            }
        }

        AES.shiftRows(state);
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

    @Test
    void testAES128Encryption() {
        int[] input = {0x00, 0x11, 0x22, 0x33, 0x44, 0x55, 0x66, 0x77, 0x88, 0x99, 0xaa, 0xbb, 0xcc, 0xdd, 0xee, 0xff};
        int[] key = {0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f};
        int[] expectedOutput = {0x69, 0xc4, 0xe0, 0xd8, 0x6a, 0x7b, 0x04, 0x30, 0xd8, 0xcd, 0xb7, 0x80, 0x70, 0xb4, 0xc5, 0x5a};

        byte[] inputBytes = new byte[input.length];
        for (int i = 0; i < input.length; i++) {
            inputBytes[i] = (byte) input[i];
        }

        byte[] expectedOutputBytes = new byte[expectedOutput.length];
        for (int i = 0; i < expectedOutput.length; i++) {
            expectedOutputBytes[i] = (byte) expectedOutput[i];
        }

        byte[] keyBytes = new byte[key.length];
        for (int i = 0; i < key.length; i++) {
            keyBytes[i] = (byte) key[i];
        }

        inputBytes = AES.encrypt(inputBytes, keyBytes);

        assertArrayEquals(expectedOutputBytes, inputBytes);
    }
}