package model;

import app.MainApp;
import controller.MainController;

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

    public static void subBytes(byte[][] state) {

        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {

                int value = state[row][col] & 0xFF;
                int substituted = SBox.substitute(value);

                state[row][col] = (byte) substituted;
            }
        }

    }

    public static void shiftRows(byte[][] state) {
        byte[][] helper = new byte[4][4];

        for (int row = 0; row < 4; row++) {
            for (int  col = 0; col < 4; col++) {
                helper[row][col] = state[row][col];
            }
        }

        // first row doesn't change

        // second row rotates by one byte
        state[1][0] = helper[1][1];
        state[1][1] = helper[1][2];
        state[1][2] = helper[1][3];
        state[1][3] = helper[1][0];

        // third row rotates by two bytea
        state[2][0] = helper[2][2];
        state[2][1] = helper[2][3];
        state[2][2] = helper[2][0];
        state[2][3] = helper[2][1];

        // fourth row rotates by three bytea
        state[3][0] = helper[3][3];
        state[3][1] = helper[3][0];
        state[3][2] = helper[3][1];
        state[3][3] = helper[3][2];
    }

    public static byte mul2(byte x) {
        int val = x & 0xFF;

        int res = val << 1;

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

    public static void mixColumns(byte[][] state) {
        for (int col = 0; col < 4; col++) {
            mixColumn(state, col);
        }
    }

    public static void addRoundKey(byte[][] state, byte[][] roundKey) {
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                state[row][col] ^= roundKey[row][col];
            }
        }
    }

    public static byte[] encrypt(byte[] input, byte[] key) {
        byte[][] state = toState(input);

        byte[][] words = KeyExpansion.expandKey(key);

        // round 0
        byte[][] roundKey = KeyExpansion.getRoundKey(words, 0);
        addRoundKey(state, roundKey);

        // rounds 1 - 9
        for (int round = 1; round <= 9; round++) {
            subBytes(state);
            shiftRows(state);
            mixColumns(state);

            roundKey = KeyExpansion.getRoundKey(words, round);
            addRoundKey(state, roundKey);
        }

        // final round
        subBytes(state);
        shiftRows(state);
        roundKey = KeyExpansion.getRoundKey(words, 10);
        addRoundKey(state, roundKey);

        return fromState(state);
    }

    public static byte[] encryptData(byte[] input, byte[] key) {
        byte[] padded = MainController.addPadding(input);
        byte[] output = new byte[padded.length];

        for (int i = 0; i < padded.length; i += 16) {
            byte[] block = new byte[16];

            // kopiuj blok
            for (int j = 0; j < 16; j++) {
                block[j] = padded[i + j];
            }

            byte[] encrypted = encrypt(block, key);

            // zapisz wynik
            for (int j = 0; j < 16; j++) {
                output[i + j] = encrypted[j];
            }
        }

        return output;
    }
}