package ee;

import static ee.AES_128.cipherIntStateRconGenerate;
import static ee.AES_128.invCipherRconGenerate;
import static ee.Utils.*;

public class App {

    static String keyString = "First Try of AES";
    public static void main(String[] args) {
        String IV = "fizzle";//6 characters*16 bit length = 96
        String plaintext = "acknowledgements";
        String dataToAuthenticate = "This is my extended essay.";// has to be less than 2^64

        //Basic AES test case
        int[] cipherText = cipherIntStateRconGenerate(keyString, plaintextTo2DIntArray(plaintext));
        System.out.println(linearIntArrayToAsciiString(cipherText));
        int[] backToPlainText = invCipherRconGenerate(keyString, linearToTwoDimensionalArray(cipherText));
        System.out.println(linearIntArrayToAsciiString(backToPlainText));


    }


}





