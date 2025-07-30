package ee;

import java.util.Arrays;
import java.util.BitSet;

public class Utils {
    static final int Nr = 10;
    static final int Nb = 4;
    static final int Nk = 4;
    private final static int WEIRD_CONSTANT = 0b11011;
    private final static int ONE_BYTE = 0b11111111;

    static String linearIntArrayToString(int[] input) {
        char[] toReturn = new char[input.length];
        for (int i = 0; i < input.length; i++) {
            toReturn[i] = (char) input[i];

        }
        return new String(toReturn);
    }

    static int[] twoDimensionalToLinearArray(int[][] input) {
        int width = input.length;
        int[] out = new int[width * width];
        for (int r = 0; r < width; r++) {
            for (int c = 0; c < width; c++) {
                out[r + (width * c)] = input[r][c];
            }
        }
        System.out.println(Arrays.toString(out));
        return out;
    }

    static int[][] linearToTwoDimensionalArray(int[] input) {
        int[][] output = new int[Nb][Nb];
        for (int i = 0; i < input.length; i++) {
            output[i % 4][i / 4] = input[i];
        }
        return output;
    }

    static int[][] plaintextTo2DIntArray(String textGiven) {
        //each ascii character corresponds to one byte/int
        int[][] textReturn = new int[4][4];
        for (int i = 0; i < 16; i++) {
            textReturn[i % 4][i / 4] = textGiven.charAt(i);
        }
        return textReturn;
    }

    //todo: Implement this
    static int[][] bitsetToTwoDimensionalIntArray(BitSet bitSet){
        return new int[][]{{0}};
    }

    //todo: Implement this
    static BitSet intLinearArrayToBitset(int[] input){
        return new BitSet(2);
    }

    static BitSet stringToBitset(String input){
        BitSet toReturn = new BitSet(input.length()*16);
        StringBuilder inputAsBitString = new StringBuilder();
        // not setting capacity bc don't want it to be too big
        for (int i = 0; i < input.length(); i++) {
            inputAsBitString.append(Integer.toBinaryString(input.charAt(i)));
        }
        for (int i = 0; i < inputAsBitString.length(); i++) {
            if(inputAsBitString.charAt(i)=='1'){
                toReturn.set(i);
            }
        }
        return toReturn;
    }

    static int xTimes(int a) {
        //multiply a by primitive element (0x02, or ob10)
        int twice = a << 1;
        if (a < 128) {//7th bit not set
            return twice;
        } else {
            return (twice & ONE_BYTE) ^ (WEIRD_CONSTANT);
        }
    }

    //try to understand what I did here
    static int multiplicationB(int a, int b) {
        if (a > 0 && b > 0) {
            if (a == 1) {
                return b;
            }
            if (b == 1) {
                return a;
            }
            int highestBitSet = findHighestPowerOf2(b);
            int[] xTimesValues = new int[highestBitSet + 1];
            xTimesValues[0] = a;
            for (int p = 1; p < highestBitSet + 1; p++) {
                xTimesValues[p] = xTimes(xTimesValues[p - 1]);
            }
            int remainder = b;
            int addition = 0;
            for (int p = xTimesValues.length - 1; p >= 0; p--) {
                int twoToTheP = 1 << p;
                if (remainder >= twoToTheP) {
                    addition ^= xTimesValues[p];
                    remainder -= twoToTheP;
                }
            }
            return addition;
        }
        return 0;
    }

    public static int findHighestPowerOf2(int n) {
        int index = 0;
        int pow2 = 1;
        while (n >= pow2) {
            pow2 = pow2 << 1;
            index++;
        }
        return index - 1;
    }
}
