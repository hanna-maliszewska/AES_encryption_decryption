package model;

public class AES {
    public static byte[][] toState(byte[] input) {
        byte[][] state = new byte[4][4];

        for (int i = 0; i < 16; i++) {
            state[i % 4][i / 4] = input[i];
        }

        return state;
    }

    public static byte[] fromState(byte[][] state) {
        byte[] output = new byte[16];

        for (int i = 0; i < 16; i++) {
            output[i] = state[i % 4][i / 4];
        }

        return output;
    }

    public static byte[][] subBytes(byte[][] state) {

        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {

                int value = state[row][col] & 0xFF;
                int substituted = SBox.substitute(value);

                state[row][col] = (byte) substituted;
            }
        }

        return state;
    }

    public static byte[][] shiftRows(byte[][] state) {
        byte[][] helper = new byte[4][4];

        // first row doesn't change

        // second row rotates by one byte
        helper[1][0] = state[1][1];
        helper[1][1] = state[1][2];
        helper[1][2] = state[1][3];
        helper[1][3] = state[1][0];

        // third row rotates by two bytea
        helper[2][0] = state[2][2];
        helper[2][1] = state[2][3];
        helper[2][2] = state[2][0];
        helper[2][3] = state[2][1];

        // fourth row rotates by three bytea
        helper[3][0] = state[3][3];
        helper[3][1] = state[3][0];
        helper[3][2] = state[3][1];
        helper[3][3] = state[3][2];

        return helper;
    }

    public static byte mul2(byte x) {
        int val = x & 0xFF;

        int res = x << 1;

        if ((val & 0x80) != 0) {
            res ^= 0x1b;
        }

        return (byte) (res & 0xFF);
    }

    public static byte mul3(byte x) {
        byte val = mul2(x);
        val ^= x;

        return val;
    }

    public static void mixColumn(byte[][] state, int col) {
        byte a = state[0][col];
        byte b = state[1][col];
        byte c = state[2][col];
        byte d = state[3][col];

        byte new0 = (byte) (mul2(a) ^ mul3(b) ^ c ^ d);
        byte new1 = (byte) (a ^ mul2(b) ^ mul3(c) ^ d);
        byte new2 = (byte) (a ^ b ^ mul2(c) ^ mul3(d));
        byte new3 = (byte) (mul3(a) ^ b ^ c ^ mul2(d));

        state[0][col] = new0;
        state[1][col] = new1;
        state[2][col] = new2;
        state[3][col] = new3;
    }

    public static byte[][] mixColumns(byte[][] state) {
        for (int col = 0; col < 4; col++) {
            mixColumn(state, col);
        }
        return state;
    }
}