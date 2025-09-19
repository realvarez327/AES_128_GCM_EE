package ee;

import org.junit.jupiter.api.Test;

import static ee.AES_128.cipherIntState;
import static ee.AES_128.invCipher;
import static ee.Utils.*;
import static ee.Utils.linearIntArrayToAsciiString;

public class OfficialTest {
    @Test
    public void officialAESTest(){
        final String plaintext = "acknowledgements";
        final String keyString = "First Try of AES";
        int[] cipherText = cipherIntState(keyString, plaintextTo2DIntArray(plaintext));
        System.out.println(linearIntArrayToAsciiString(cipherText));
        int[] backToPlainText = invCipher(keyString, linearToTwoDimensionalArray(cipherText));
        System.out.println(linearIntArrayToAsciiString(backToPlainText));
    }
}
