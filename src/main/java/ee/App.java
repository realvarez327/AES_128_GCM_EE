package ee;

import java.util.Arrays;
import java.util.BitSet;

import static ee.AES_128.*;
import static ee.Utils.*;

public class App {

    static String keyString = "First Try of AES";
    public static void main(String[] args) {

        String plainText = "calligraphically";
        int[] cipherText = cipher(keyString, plaintextTo2DIntArray(plainText));

        System.out.println(linearIntArrayToString(cipherText));
        int[] backToPlainText = invCipher(keyString, linearToTwoDimensionalArray(cipherText));
        System.out.println(linearIntArrayToString(backToPlainText));
    }


}





