package ee; //an immaculate, stellar, innovative piece of code

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

    public static BitSet multiplicationBlock(BitSet X, BitSet Y){
        final BitSet R = new BitSet(128);
        R.set(121);
        R.set(125, 127);
        BitSet Z = new BitSet(128);
        BitSet V = Y;
        for (int i = 0; i < 128; i++) {
            if(X.get(i)){
                Z.xor(V);
            }
            V = V.get(1, 127); //right shift by 1
            if(V.get(0)){
              V.xor(R);
            }
        }
        return Z;
    }

    //review bc huh?
    public static BetterBitSet intLinearArrayToBitset(int[] input){
        BetterBitSet workingOn = new BetterBitSet();
        StringBuilder binaryVersion = new StringBuilder();
        for (int i = 0; i < input.length; i++) {
            String toAdd = Integer.toBinaryString(input[i]);
            binaryVersion.append(toAdd).append("0".repeat(16 - toAdd.length()));
        }

        int index = binaryVersion.indexOf("1", 0);
        while (index != -1){
            workingOn.set(index);
            index = binaryVersion.indexOf("1", index +1);
        }

        return workingOn;
    }

    //todo ask for review, will there be repeat bits?
    //bug -> java.lang.ArrayIndexOutOfBoundsException: Index 4 out of bounds for length 4
    public static int[][] bitsetToTwoDimensionalIntArray(BetterBitSet input){
        final int[][] toReturn = new int[4][4];
        int ctr = 0;
        for(int i = 0; i <(input.length()-1); i+=16){
            toReturn[ctr%4][ctr/4] = input.get(i,i+15).bitsetToInteger();
            ctr++;
        }
        return toReturn;
    }


    //todo implement
    public static BetterBitSet twoDimensionalIntArrayToBitset(int[][] in){
        BetterBitSet toReturn = new BetterBitSet();
//        int k = 0;
//        for (int i = 0; i < 16; i++) {
//            //this is the bad and slow version
//            BitSet toAddOn = intToBitset( in[i%4][i/4]);
//            int addingOnHighestSet = toAddOn.nextSetBit(0);
//            while (addingOnHighestSet<16) {
//                toReturn.set((16*k) + addingOnHighestSet);
//                addingOnHighestSet= toAddOn.nextSetBit(addingOnHighestSet+1);
//            }
//            k++;
//        }'


        return toReturn;
    }

    //this sucks, todo redo post review
    public static BitSet intToBitset(int in){
        BitSet toReturn = new BitSet(16);
        for (int i = 0; i < 16; i++) {
            if((in>>i)%2==0){
                toReturn.set(i);
            }
        }
        return toReturn;
    }

    public static int divCeil(int a, int divisor){
        int remainder = a% divisor;
        if(remainder != 0) {
            a += remainder;
        }
        return a/divisor;
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

    public static String bitsetToBinaryString(BetterBitSet in) {
        return Integer.toBinaryString(BetterBitSet.bitsetToInteger(in));
    }

    public static BetterBitSet binaryStringToBetterBitSet(String in){
        BetterBitSet toReturn = new BetterBitSet();
        int index = in.indexOf('1');
        while (index>=0){
            toReturn.set(index);
            index=in.indexOf('1',index+1);
        }
        return toReturn;
    }
}
