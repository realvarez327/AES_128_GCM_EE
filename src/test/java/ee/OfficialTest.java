package ee;

import org.junit.jupiter.api.Test;

import static ee.AES_128.*;
import static ee.Utils.*;
import static ee.Utils.linearIntArrayToAsciiString;

public class OfficialTest {

    final String plaintext = "acknowledgements";
    final String keyString = "First Try of AES";

    @Test
    public void officialAESTest_Word(){
        for (int i = 0; i < 5; i++) {
            int[] cipherText = cipherIntStateWord(keyString, plaintextToWordArray(plaintext));
            System.out.println(linearIntArrayToAsciiString(cipherText));
            int[] backToPlainText = invCipherWord(keyString, linearIntegerToWordArray(cipherText));
            System.out.println(linearIntArrayToAsciiString(backToPlainText));
        }

    }




    @Test
    public void officialAESTest_Base(){
        for (int i = 0; i < 5; i++) {
            int[] cipherText = cipherIntStateBase(keyString, plaintextTo2DIntArray(plaintext));
            System.out.println(linearIntArrayToAsciiString(cipherText));
            int[] backToPlainText = invCipherBase(keyString, linearToTwoDimensionalArray(cipherText));
            System.out.println(linearIntArrayToAsciiString(backToPlainText));
        }

    }

    @Test
    public void officialAESTest_RconValues(){
        for (int i = 0; i < 5; i++) {
            int[] cipherText = cipherIntStateRconGenerate(keyString, plaintextTo2DIntArray(plaintext));
            System.out.println(linearIntArrayToAsciiString(cipherText));
            int[] backToPlainText = invCipherRconGenerate(keyString, linearToTwoDimensionalArray(cipherText));
            System.out.println(linearIntArrayToAsciiString(backToPlainText));
        }
    }

    @Test
    public void officialAESTest_FunctionCallFrequency(){
        for (int i = 0; i < 5; i++) {
            int[] cipherText = cipherIntStateFunctionCallFrequency(keyString, plaintextTo2DIntArray(plaintext));
            System.out.println(linearIntArrayToAsciiString(cipherText));
            int[] backToPlainText = invCipherFunctionCallFrequency(keyString, linearToTwoDimensionalArray(cipherText));
            System.out.println(linearIntArrayToAsciiString(backToPlainText));
        }
    }


}
