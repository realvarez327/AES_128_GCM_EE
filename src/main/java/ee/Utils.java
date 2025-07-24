package ee;

import java.util.Arrays;

public class Utils {
    static final int Nr = 10;
    static final int Nb = 4;
    static final int Nk = 4;
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
}
