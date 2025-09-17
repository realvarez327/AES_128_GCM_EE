package ee;

import org.junit.jupiter.api.Test;

import static ee.AES_128.cipherIntState;
import static ee.AES_128.invCipher;
import static ee.Utils.*;
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
    public void basicAES(){
        String aesPlainText = "fantasticalities";
        int[] cipherText = cipherIntState(keyString, plaintextTo2DIntArray(aesPlainText));
        System.out.println(linearIntArrayToString(cipherText));
        int[] backToPlainText = invCipher(keyString, linearToTwoDimensionalArray(cipherText));
        System.out.println(linearIntArrayToString(backToPlainText));
        assertEquals(aesPlainText, linearIntArrayToString(backToPlainText));
    }

}
