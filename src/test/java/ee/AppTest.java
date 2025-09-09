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
        String IV = "fizzle";//6 characters*16 bit length = 96
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
    public void GcmEncryptionTest(){
        String key ="þÿé\u0092\u0086es\u001Cmj\u008F\u0094g0\u0083\b";
        String IV = "Êþº¾úÎÛ\u00ADÞÊø\u0088";
        String P = "Ù12%ø\u0084\u0006å¥Y\tÅ¯õ&\u009A\u0086§©S\u00154÷Ú.L0=\u008A1\u008Ar\u001C<\f\u0095\u0095h\tS/Ï\u000E$I¦µ%±jíõª\n" +
                "æWºc{9\u001A¯ÒU";

        String toCheckCipherText = "B\u0083\u001EÂ!wt$Kr!·\u0084ÐÔ\u009Cãª!/,\u0002¤à5Á~#)¬¡.!Õ\u0014²Tf\u0093\u001C}\u008FjZ¬\u0084ª\u0005\u001B£\u000B9j\n" +
                "¬\u0097=Xà\u0091G?Y\u0085";
        String A = "";
         GCM gcmFactory = new GCM();
         gcmFactory.setKey(key);
         EncryptionReturnPackage erp = gcmFactory.gcmEncryption(P, IV, A);

         assertEquals(toCheckCipherText, erp.ciphertext());
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

    @Test
    public void AesToGCMInputComparison(){
        String test = "fantasticalities";
        int[][] baseOnly = plaintextTo2DIntArray(test);

    }
}
