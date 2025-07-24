package ee;


import static ee.Utils.*;

public class AES_128 {

    static final int[][] Rcon = {
            {},//we don't use Rcon[0]
            {1, 0, 0, 0},
            {2, 0, 0, 0},
            {4, 0, 0, 0},
            {8, 0, 0, 0},
            {16, 0, 0, 0},
            {32, 0, 0, 0},
            {64, 0, 0, 0},
            {128, 0, 0, 0},
            {27, 0, 0, 0},
            {54, 0, 0, 0}
    };

    static final int[][] sBox = {
            {99, 124, 119, 123, 242, 107, 111, 197, 48, 1, 103, 43, 254, 215, 171, 118},
            {0xca, 0x82, 0xc9, 0x7d, 0xfa, 0x59, 0x47, 0xf0, 0xad, 0xd4, 0xa2, 0xaf, 0x9c, 0xa4, 0x72, 0xc0},
            {0xb7, 0xfd, 0x93, 0x26, 0x36, 0x3f, 0xf7, 0xcc, 0x34, 0xa5, 0xe5, 0xf1, 0x71, 0xd8, 0x31, 0x15},
            {0x04, 0xc7, 0x23, 0xc3, 0x18, 0x96, 0x05, 0x9a, 0x07, 0x12, 0x80, 0xe2, 0xeb, 0x27, 0xb2, 0x75},
            {0x09, 0x83, 0x2c, 0x1a, 0x1b, 0x6e, 0x5a, 0xa0, 0x52, 0x3b, 0xd6, 0xb3, 0x29, 0xe3, 0x2f, 0x84},
            {0x53, 0xd1, 0x00, 0xed, 0x20, 0xfc, 0xb1, 0x5b, 0x6a, 0xcb, 0xbe, 0x39, 0x4a, 0x4c, 0x58, 0xcf},
            {0xd0, 0xef, 0xaa, 0xfb, 0x43, 0x4d, 0x33, 0x85, 0x45, 0xf9, 0x02, 0x7f, 0x50, 0x3c, 0x9f, 0xa8},
            {0x51, 0xa3, 0x40, 0x8f, 0x92, 0x9d, 0x38, 0xf5, 0xbc, 0xb6, 0xda, 0x21, 0x10, 0xff, 0xf3, 0xd2},
            {0xcd, 0x0c, 0x13, 0xec, 0x5f, 0x97, 0x44, 0x17, 0xc4, 0xa7, 0x7e, 0x3d, 0x64, 0x5d, 0x19, 0x73},
            {0x60, 0x81, 0x4f, 0xdc, 0x22, 0x2a, 0x90, 0x88, 0x46, 0xee, 0xb8, 0x14, 0xde, 0x5e, 0x0b, 0xdb},
            {0xe0, 0x32, 0x3a, 0x0a, 0x49, 0x06, 0x24, 0x5c, 0xc2, 0xd3, 0xac, 0x62, 0x91, 0x95, 0xe4, 0x79},
            {0xe7, 0xc8, 0x37, 0x6d, 0x8d, 0xd5, 0x4e, 0xa9, 0x6c, 0x56, 0xf4, 0xea, 0x65, 0x7a, 0xae, 0x08},
            {0xba, 0x78, 0x25, 0x2e, 0x1c, 0xa6, 0xb4, 0xc6, 0xe8, 0xdd, 0x74, 0x1f, 0x4b, 0xbd, 0x8b, 0x8a},
            {0x70, 0x3e, 0xb5, 0x66, 0x48, 0x03, 0xf6, 0x0e, 0x61, 0x35, 0x57, 0xb9, 0x86, 0xc1, 0x1d, 0x9e},
            {0xe1, 0xf8, 0x98, 0x11, 0x69, 0xd9, 0x8e, 0x94, 0x9b, 0x1e, 0x87, 0xe9, 0xce, 0x55, 0x28, 0xdf},
            {0x8c, 0xa1, 0x89, 0x0d, 0xbf, 0xe6, 0x42, 0x68, 0x41, 0x99, 0x2d, 0x0f, 0xb0, 0x54, 0xbb, 0x16}
    };


    static final int[][] invSBox = {
            {0x52, 0x09, 0x6a, 0xd5, 0x30, 0x36, 0xa5, 0x38, 0xbf, 0x40, 0xa3, 0x9e, 0x81, 0xf3, 0xd7, 0xfb},
            {0x7c, 0xe3, 0x39, 0x82, 0x9b, 0x2f, 0xff, 0x87, 0x34, 0x8e, 0x43, 0x44, 0xc4, 0xde, 0xe9, 0xcb},
            {0x54, 0x7b, 0x94, 0x32, 0xa6, 0xc2, 0x23, 0x3d, 0xee, 0x4c, 0x95, 0x0b, 0x42, 0xfa, 0xc3, 0x4e},
            {0x08, 0x2e, 0xa1, 0x66, 0x28, 0xd9, 0x24, 0xb2, 0x76, 0x5b, 0xa2, 0x49, 0x6d, 0x8b, 0xd1, 0x25},
            {0x72, 0xf8, 0xf6, 0x64, 0x86, 0x68, 0x98, 0x16, 0xd4, 0xa4, 0x5c, 0xCC, 0x5d, 0x65, 0xb6, 0x9},
            {0x6c, 0x70, 0x48, 0x50, 0xfd, 0xed, 0xb9, 0xda, 0x5e, 0x15, 0x46, 0x57, 0xa7, 0x8d, 0x9d, 0x84},
            {0x90, 0xd8, 0xab, 0x00, 0x8c, 0xbc, 0xd3, 0x0a, 0xf7, 0xe4, 0x58, 0x05, 0xb8, 0xb3, 0x45, 0x06},
            {0xd0, 0x2c, 0x1e, 0x8f, 0xca, 0x3f, 0x0f, 0x02, 0xc1, 0xaf, 0xbd, 0x03, 0x01, 0x13, 0x8a, 0x6b},
            {0x3a, 0x91, 0x11, 0x41, 0x4f, 0x67, 0xdc, 0xea, 0x97, 0xf2, 0xcf, 0xce, 0xf0, 0xb4, 0xe6, 0x73},
            {0x96, 0xac, 0x74, 0x22, 0xe7, 0xad, 0x35, 0x85, 0xe2, 0xf9, 0x37, 0xe8, 0x1c, 0x75, 0xdf, 0x6e},
            {0x47, 0xf1, 0x1a, 0x71, 0x1d, 0x29, 0xc5, 0x89, 0x6f, 0xb7, 0x62, 0x0e, 0xaa, 0x18, 0xbe, 0x1b},
            {0xfc, 0x56, 0x3e, 0x4b, 0xc6, 0xd2, 0x79, 0x20, 0x9a, 0xdb, 0xc0, 0xfe, 0x78, 0xcd, 0x5a, 0xf4},
            {0x1f, 0xdd, 0xa8, 0x33, 0x88, 0x07, 0xc7, 0x31, 0xb1, 0x12, 0x10, 0x59, 0x27, 0x80, 0xec, 0x5f},
            {0x60, 0x51, 0x7f, 0xa9, 0x19, 0xb5, 0x4a, 0x0d, 0x2d, 0xe5, 0x7a, 0x9f, 0x93, 0xc9, 0x9c, 0xef},
            {0xa0, 0xe0, 0x3b, 0x4d, 0xae, 0x2a, 0xf5, 0xb0, 0xc8, 0xeb, 0xbb, 0x3c, 0x83, 0x53, 0x99, 0x61},
            {0x17, 0x2b, 0x04, 0x7e, 0xba, 0x77, 0xd6, 0x26, 0xe1, 0x69, 0x14, 0x63, 0x55, 0x21, 0x0c, 0x7d}
    };


    private final static int WEIRD_CONSTANT = 0b11011;
    private final static int ONE_BYTE = 0b11111111;

    static int xTimes(int a) {
        //multiply a by primitive element (0x02, or ob10)
        int twice = a << 1;
        if (a < 128) {//7th bit not set
            return twice;
        } else {
            return (twice & ONE_BYTE) ^ (WEIRD_CONSTANT);
        }
    }



    @SuppressWarnings("DataFlowIssue")
    static int[] cipher(String key, int[][] state) {
        int[][] w = keyExpansion(plaintextTo2DIntArray(key));
        state = addRoundKey(state, new int[][]{
                {w[0][0], w[0][1], w[0][2], w[0][3]},
                {w[1][0], w[1][1], w[1][2], w[1][3]},
                {w[2][0], w[2][1], w[2][2], w[2][3]},
                {w[3][0], w[3][1], w[3][2], w[3][3]}
        });
        for (int round = 1; round < Nr; round++) {
            //do subBytes for each byte in state
            for (int r = 0; r < 4; r++) {
                for (int c = 0; c < 4; c++) {
                    state[r][c] = subBytes(state[r][c]);
                }
            }
            state = shiftRows(state);
            state = mixColumns(state);
            state = addRoundKey(state, new int[][]{
                    {w[0][4 * round], w[0][(4 * round) + 1], w[0][(4 * round) + 2], w[0][(4 * round) + 3]},
                    {w[1][4 * round], w[1][(4 * round) + 1], w[1][(4 * round) + 2], w[1][(4 * round) + 3]},
                    {w[2][4 * round], w[2][(4 * round) + 1], w[2][(4 * round) + 2], w[2][(4 * round) + 3]},
                    {w[3][4 * round], w[3][(4 * round) + 1], w[3][(4 * round) + 2], w[3][(4 * round) + 3]}
            });
        }
        for (int r = 0; r < 4; r++) {
            for (int c = 0; c < 4; c++) {
                state[r][c] = subBytes(state[r][c]);
            }
        }
        state = shiftRows(state);

        state = addRoundKey(state, new int[][]{
                {w[0][(4 * Nr)], w[0][(4 * Nr) + 1], w[0][(4 * Nr) + 2], w[0][(4 * Nr) + 3]},
                {w[1][(4 * Nr)], w[1][(4 * Nr) + 1], w[1][(4 * Nr) + 2], w[1][(4 * Nr) + 3]},
                {w[2][(4 * Nr)], w[2][(4 * Nr) + 1], w[2][(4 * Nr) + 2], w[2][(4 * Nr) + 3]},
                {w[3][(4 * Nr)], w[3][(4 * Nr) + 1], w[3][(4 * Nr) + 2], w[3][(4 * Nr) + 3]}
        });
        return twoDimensionalToLinearArray(state);
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

    static int[][] mixColumns(int[][] state) {
        //using standards terminology
        //"input word" is the column (s0c, s1c, s2c, s3c)
        for (int column = 0; column < 4; column++) {

            int[] newColumnBytes = new int[4];
            newColumnBytes[0] =
                    multiplicationB(2, state[0][column]) ^
                            (multiplicationB(3, state[1][column])) ^
                            state[2][column] ^
                            state[3][column];
            newColumnBytes[1] =
                    state[0][column] ^
                            multiplicationB(2, state[1][column]) ^
                            multiplicationB(3, state[2][column]) ^
                            state[3][column];
            newColumnBytes[2] =
                    state[0][column] ^
                            state[1][column]
                            ^ multiplicationB(2, state[2][column])
                            ^ multiplicationB(3, state[3][column]);
            newColumnBytes[3] =
                    multiplicationB(3, state[0][column]) ^
                            state[1][column] ^
                            state[2][column] ^
                            multiplicationB(2, state[3][column]);

            for (int i = 0; i < 4; i++) {
                state[i][column] = newColumnBytes[i];
            }
        }
        return state;
    }

    static int[][] shiftRows(int[][] state) {
        for (int i = 1; i < state.length; i++) {
            int[] holder = new int[i];
            System.arraycopy(state[i], 0, holder, 0, i);
            for (int j = i; j < state.length; j++) {
                state[i][j - i] = state[i][j];
            }
            int c = 0;
            for (int j = 4 - i; j < state.length; j++) {
                state[i][j] = holder[c];
                c++;
            }
        }
        return state;
    }

    @SuppressWarnings("DataFlowIssue")
    static int[][] keyExpansion(int[][] key) {
        int[][] w = new int[4][4 * (Nr + 1)];
        //has 4 rows, but 4*Nr+1 (11) words.
        int i = 0;
        while (i <= (Nk - 1)) {
            w[0][i] = key[0][i];
            w[1][i] = key[1][i];
            w[2][i] = key[2][i];
            w[3][i] = key[3][i];

            i++;
        }
        while (i <= ((4 * Nr) + 3)) {
            int[] temp = new int[]{w[0][i - 1], w[1][i - 1], w[2][i - 1], w[3][i - 1]};
            if ((i % Nk) == 0) {
                temp = subWord(rotWord(temp));
                int[] currRcon = Rcon[i / Nk];
                temp[0] = (temp[0] ^ currRcon[0]);
                temp[1] = (temp[1] ^ currRcon[1]);
                temp[2] = (temp[2] ^ currRcon[2]);
                temp[3] = (temp[3] ^ currRcon[3]);
            }
            for (int j = 0; j < 4; j++) {
                w[j][i] = (w[j][i - Nk] ^ temp[j]);
            }
            i++;

        }
        return w;
    }

    static int[] subWord(int[] in) {
        for (int i = 0; i < 4; i++) {
            in[i] = subBytes(in[i]);
        }
        return in;
    }

    static int[] rotWord(int[] in) {
        int holder = in[0];
        for (int i = 1; i < Nb; i++) {
            in[i - 1] = in[i];
        }
        in[3] = holder;
        return in;
    }

    static int subBytes(int inputByte) {
        int row = inputByte >>> 4;
        int col = inputByte & 15;
        return sBox[row][col];
    }

    static int[][] addRoundKey(int[][] state, int[][] fourWords) {
        //each call uses 4 words
        // each column is a word.
        for (int c = 0; c < Nb; c++) {
            for (int r = 0; r < Nb; r++) {
                state[r][c] ^= fourWords[r][c];
            }
        }
        return state;
    }

    @SuppressWarnings("DataFlowIssue")
    static int[] invCipher(String keyAsString, int[][] in) {
        int[][] state = in;
        int[][] roundKeys = keyExpansion(plaintextTo2DIntArray(keyAsString));
        state = addRoundKey(state, new int[][]{
                {roundKeys[0][4 * Nr], roundKeys[0][(4 * Nr) + 1], roundKeys[0][(4 * Nr) + 2], roundKeys[0][(4 * Nr) + 3]},
                {roundKeys[1][4 * Nr], roundKeys[1][(4 * Nr) + 1], roundKeys[1][(4 * Nr) + 2], roundKeys[1][(4 * Nr) + 3]},
                {roundKeys[2][4 * Nr], roundKeys[2][(4 * Nr) + 1], roundKeys[2][(4 * Nr) + 2], roundKeys[2][(4 * Nr) + 3]},
                {roundKeys[3][4 * Nr], roundKeys[3][(4 * Nr) + 1], roundKeys[3][(4 * Nr) + 2], roundKeys[3][(4 * Nr) + 3]}
        });
        for (int round = Nr - 1; round >= 1; round--) {
            state = invShiftRows(state);
            for (int r = 0; r < state.length; r++) {
                for (int c = 0; c < state[r].length; c++) {
                    state[r][c] = invSubBytes(state[r][c]);
                }
            }
            state = addRoundKey(state, new int[][]{
                    {roundKeys[0][4 * round], roundKeys[0][(4 * round) + 1], roundKeys[0][(4 * round) + 2], roundKeys[0][(4 * round) + 3]},
                    {roundKeys[1][4 * round], roundKeys[1][(4 * round) + 1], roundKeys[1][(4 * round) + 2], roundKeys[1][(4 * round) + 3]},
                    {roundKeys[2][4 * round], roundKeys[2][(4 * round) + 1], roundKeys[2][(4 * round) + 2], roundKeys[2][(4 * round) + 3]},
                    {roundKeys[3][4 * round], roundKeys[3][(4 * round) + 1], roundKeys[3][(4 * round) + 2], roundKeys[3][(4 * round) + 3]}
            });
            state = invMixColumns(state);
        }
        state = invShiftRows(state);
        for (int r = 0; r < state.length; r++) {
            for (int c = 0; c < state[r].length; c++) {
                state[r][c] = invSubBytes(state[r][c]);
                //mini-choice: make this its own variable or not?
            }
        }
        state = addRoundKey(state, new int[][]{
                {roundKeys[0][0], roundKeys[0][1], roundKeys[0][2], roundKeys[0][3]},
                {roundKeys[1][0], roundKeys[1][1], roundKeys[1][2], roundKeys[1][3]},
                {roundKeys[2][0], roundKeys[2][1], roundKeys[2][2], roundKeys[2][3]},
                {roundKeys[3][0], roundKeys[3][1], roundKeys[3][2], roundKeys[3][3]}
        });

        //docs say to return state, but I want to maybe do a test case... so we return as linear version of state
        //return state;
        return twoDimensionalToLinearArray(state);
    }

    private static int[][] invMixColumns(int[][] in) {
        int[] newCol = new int[Nb];
        for (int c = 0; c < Nb; c++) {
            newCol[0] =
                    multiplicationB(0x0e, in[0][c])
                            ^ multiplicationB(0x0b, in[1][c])
                            ^ multiplicationB(0x0d, in[2][c])
                            ^ multiplicationB(0x09, in[3][c]);

            newCol[1] =
                    multiplicationB(0x09, in[0][c])
                            ^ multiplicationB(0x0e, in[1][c])
                            ^ multiplicationB(0x0b, in[2][c])
                            ^ multiplicationB(0x0d, in[3][c]);

            newCol[2] =
                    multiplicationB(0x0d, in[0][c])
                            ^ multiplicationB(0x09, in[1][c])
                            ^ multiplicationB(0x0e, in[2][c])
                            ^ multiplicationB(0x0b, in[3][c]);

            newCol[3] =
                    multiplicationB(0x0b, in[0][c])
                            ^ multiplicationB(0x0d, in[1][c])
                            ^ multiplicationB(0x09, in[2][c])
                            ^ multiplicationB(0x0e, in[3][c]);

            in[0][c] = newCol[0];
            in[1][c] = newCol[1];
            in[2][c] = newCol[2];
            in[3][c] = newCol[3];
        }

        return in;
    }

    static int[][] invShiftRows(int[][] in) {
        //also only used for rows 2, 3, 4
        for (int r = 1; r < Nk; r++) {
            //use systems.copyArray()
            int[] holder = new int[4];
            System.arraycopy(in[r], 4 - r, holder, 0, r);
            //last r elements in holder
            System.arraycopy(in[r], 0, in[r], r, 4 - r);
            //elements shifted right, repeats and overwrites exist at this point
            System.arraycopy(holder, 0, in[r], 0, r);
        }
        return in;
    }

    static int invSubBytes(int in) {
        int row = in >>> 4;
        int col = in & 15;
        return invSBox[row][col];
    }


}
