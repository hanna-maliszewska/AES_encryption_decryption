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
}