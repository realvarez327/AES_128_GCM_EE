package ee;

import org.junit.jupiter.api.Test;

import static ee.AES_128.*;
import static ee.Utils.*;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AppTest {
    final static String keyString = "First Try of AES";
    final String gcmPlainText = "This is a slightly longer plaintext than for aes.";

    @Test
    public void fullGcmEncryptionDecryptionTest(){
        String IV = "bacchanalian";//12 characters*8 bit length = 96
        String dataToAuthenticate = "wazzocks";// has to be less than 2^64
        GCM gcmFactory = new GCM();
        gcmFactory.setKey(keyString);
        EncryptionReturnPackage gcmReturn = gcmFactory.gcmEncryption(
                gcmPlainText,IV,dataToAuthenticate
        );
        System.out.println(gcmReturn.ciphertext());
        String returnText = gcmFactory.gcmDecryption(IV, gcmReturn.ciphertext(), gcmReturn.AAD(), gcmReturn.tag());

        assertEquals(gcmPlainText, returnText);
    }

    @Test
    public void basicAESInt(){
        String aesPlainText = "fantasticalities";
        int[] cipherText = cipherIntStateRconGenerate(keyString, plaintextTo2DIntArray(aesPlainText));
        System.out.println(linearIntArrayToAsciiString(cipherText));
        int[] backToPlainText = invCipherRconGenerate(keyString, linearToTwoDimensionalArray(cipherText));
        System.out.println(linearIntArrayToAsciiString(backToPlainText));
        assertEquals(aesPlainText, linearIntArrayToAsciiString(backToPlainText));
    }

    @Test
    public void basicAESBitset(){
        String aesPlainText = "fantasticalities";
        BetterBitSet cipherText = cipherBitSetState(keyString, BetterBitSet.asciiStringToBitset(aesPlainText));
        System.out.println("ciphertext = "+cipherText.bitSetToAsciiString());
        int[] backToPlainText = invCipherRconGenerate(keyString, bitsetToTwoDimensionalIntArray(cipherText));
        System.out.println("back to plaintext = "+ linearIntArrayToAsciiString(backToPlainText));
        assertEquals(aesPlainText, linearIntArrayToAsciiString(backToPlainText));
    }

    @Test
    public void sanityCheck(){
        int[][] intState = new int[][]{
                {1,5,9,13},
                {2,6,10,14},
                {3,7,11,15},
                {4,8,12,16}
        };
        BetterBitSet bs = twoDimensionalIntArrayToBitset(intState);
        int[][] back = bitsetToTwoDimensionalIntArray(bs);
        assertArrayEquals(intState,back);
    }
}
