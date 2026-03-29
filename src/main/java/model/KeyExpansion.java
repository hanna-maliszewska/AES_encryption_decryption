package model;

import java.util.Arrays;

public class KeyExpansion{
    private static final int[] Rcon = {
        0x00, 0x01, 0x02, 0x04,
        0x08, 0x10, 0x20, 0x40,
        0x80, 0x1B, 0x36
    };

    public static void RotWord(byte[] state) {
        byte[] helper = new byte[4];

        for (int i = 0; i < 4; i++) {
            helper[i] = state[i];
        }

        state[0] = helper[1];
        state[1] = helper[2];
        state[2] = helper[3];
        state[3] = helper[0];
    }

    public static void SubWord(byte[] state) {
        for (int i = 0; i < 4; i++) {
            int value = state[i] & 0xFF;
            int substituted = SBox.substitute(value);

            state[i] = (byte) substituted;
        }
    }

    public static void applyRcon(byte[] word, int round) {
        word[0] ^= (byte) Rcon[round];
    }

    public static void g(byte[] word, int round) {
        RotWord(word);
        SubWord(word);
        applyRcon(word, round);
    }

    public static byte[][] expandKey(byte[] key) {
        byte[][] words = new byte[44][4];

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                words[i][j] = key[i * 4 + j];
            }
        }

        for (int i = 4; i < 44; i++) {
            byte[] temp = Arrays.copyOf(words[i - 1], 4);

            if (i % 4 == 0) {
                g(temp, i / 4);
            }

            for (int j = 0; j < 4; j++) {
                words[i][j] = (byte) (words[i - 4][j] ^ temp[j]);
            }
        }

        return words;
    }
}