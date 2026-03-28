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
}