package ee; //an immaculate, stellar, innovative piece of code

public class Utils {
    static final int Nr = 10;
    static final int Nb = 4;
    static final int Nk = 4;
    private final static int WEIRD_CONSTANT = 0b11011;
    private final static int ONE_BYTE = 0b11111111;
    static final int characterBits = 8;

    //convert linear int array into an ascii string, each int is a character
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
        return out;
    }

    //turns linear int array of length 16 to a 4 by 4 2d array
    static int[][] linearToTwoDimensionalArray(int[] input) {
        int[][] output = new int[Nb][Nb];
        for (int i = 0; i < input.length; i++) {
            output[i % 4][i / 4] = input[i];
        }
        return output;
    }

    //review, hardcoded to only work for strings of length 16
    static int[][] plaintextTo2DIntArray(String textGiven) {
        //each ascii character corresponds to one byte/int
        int[][] textReturn = new int[4][4];
        for (int i = 0; i < 16; i++) {
            textReturn[i % 4][i / 4] = textGiven.charAt(i);
        }
        return textReturn;
    }

    public static BetterBitSet multiplicationBlock(BetterBitSet X, BetterBitSet Y){
        final BetterBitSet R = new BetterBitSet(128);
        R.set(120);
        R.set(125);
        R.set(126);
        R.set(127);
        assert X.length() == 128;
        assert Y.length() == 128;
        BetterBitSet Z = new BetterBitSet(128);
        BetterBitSet V = (BetterBitSet) Y.clone();
        for (int i = 0; i < 128; i++) {
            if(X.get(i)){
                Z.xor(V);
            }
            boolean leastSigBit = V.get(0);
            V = V.get(1,128);
            V.setProperLength(128);
            if(leastSigBit){
                V.xor(R);
            }
        }
        return Z;
    }

    //review FIX TODO
    public static BetterBitSet intLinearArrayToBitset(int[] input){
        BetterBitSet workingOn = new BetterBitSet(characterBits*input.length);
        StringBuilder binaryVersion = new StringBuilder();
        for (int j = input.length-1; j>-1 ;j--) {
            StringBuilder toAdd = new StringBuilder(Integer.toBinaryString(input[j])).reverse();
            binaryVersion.append(toAdd).append("0".repeat(characterBits - toAdd.length()));
        }

        int index = binaryVersion.indexOf("1", 0);
        while (index != -1){
            workingOn.set(index);
            index = binaryVersion.indexOf("1", index +1);
        }

        return workingOn;
    }

    //todo ask for review, will there be repeat bits?
    public static int[][] bitsetToTwoDimensionalIntArray(BetterBitSet input){
        final int[][] toReturn = new int[4][4];
        int ctr = 0;
        for(int i = 0; i <(input.length()-1); i+=characterBits){
            toReturn[ctr%4][ctr/4] = input.get(i,i+characterBits).bitsetToInteger();
            ctr++;
        }
        return toReturn;
    }

    public static BetterBitSet twoDimensionalIntArrayToBitset(int[][] in){
        int len = in.length;
        BetterBitSet toReturn =new BetterBitSet(0);
        for (int i = len-1; i > -1; i--) {
            BetterBitSet toAdd = intLinearArrayToBitset(new int[]{
                    in[0][i],
                    in[1][i],
                    in[2][i],
                    in[3][i]
            });
            toReturn = BetterBitSet.concatenate(toReturn, toAdd, characterBits*4);
        }

        return toReturn;
    }

    //Math.floorDiv does not work with doubles, see if this produces same result it should todo
    public static double floorDivDouble(double a, double b){
        double remainder = a % b;
        if(remainder!=0){
            a-=remainder;
        }
        return a/b;

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

    //todo try to understand what I did here
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

    //todo see if I should substitute with logs from Math, looks faster
    public static int findHighestPowerOf2(int n) {
        int index = 0;
        int pow2 = 1;
        while (n >= pow2) {
            pow2 = pow2 << 1;
            index++;
        }
        return index - 1;
    }

    //todo check usages
    public static String bitsetToBinaryString(BetterBitSet in) {
        return Integer.toBinaryString(BetterBitSet.bitsetToInteger(in));
    }

    //todo test
    public static BetterBitSet binaryStringToBetterBitSet(String in){
        BetterBitSet toReturn = new BetterBitSet(in.length());
        StringBuilder inActual = new StringBuilder(in).reverse();

        int index = inActual.indexOf("1");
        while (index>=0){
            toReturn.set(index);
            index=inActual.indexOf("1",index+1);
        }
        return toReturn;
    }
}
