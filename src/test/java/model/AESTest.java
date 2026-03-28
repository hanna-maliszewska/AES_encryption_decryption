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
}