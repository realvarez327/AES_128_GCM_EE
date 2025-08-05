package ee;

import java.util.BitSet;

import static ee.Utils.stringToBitset;

public class GCM {


    private final static int tagLength = 128;

    private static int[] getNAndU(int pLength) {
        //calculate length of P in bits
        int[] nAndU = new int[2];
        if (pLength % 128 == 0) {
            nAndU[0] = (pLength) / 128;
            nAndU[1] = 128;
            return nAndU;
        }
        //only reach here if not a nicely blocked thing
        pLength -= 128;
        nAndU[0] = pLength / 128;
        nAndU[1] = pLength % 128;
        return nAndU;
    }

    private static BitSet incr(BitSet input) {
        //input is 16 bytes
        //lord forgive me for this awful code i am going to write
        final int length = input.length();
        BitSet toReturn = input;
        if (toReturn.get(length - 1 - 5)) {
            //check if "32" bit is set
            toReturn.set(length - 1 - 5, length - 1, false);
        } else {
            //increment last integer spot
            String s = toReturn.toString().substring(length - 1 - 5, length - 1);
            for (int i = 0; i < s.length(); i++) {
                if (s.charAt(i) == '0') {
                    toReturn.set(length - 6 + i, 0);
                } else {
                    toReturn.set(length - 6 + i, 1);
                }
            }
        }
        return toReturn;
    }

    //todo continue implementing
    private static BitSet GCTR(BitSet ICB, BitSet X, String key) {
        int xLength = X.length();
        int[] nAndU = getNAndU(xLength);
        BitSet[] meddlingY = new BitSet[nAndU[0]];
        if (X.isEmpty()) {
            return new BitSet(((nAndU[0]-1)*128)+nAndU[1]);
        }

        BitSet[] CB = new BitSet[nAndU[0] + 1];
        CB[1] = ICB;
        for (int i = 2; i <= nAndU[0]; i++) {
            CB[i] = incr(CB[i - 1]);
        }
        for (int i = 1; i < nAndU[0]; i++) {
            BitSet xorComponent = AES_128.cipherBitSetState(key, CB[i]);
            X.get((i-1)*128, 128*i).xor(xorComponent);
            meddlingY[i] =X.get((i-1)*128, 128*i);
            X.get((i-1)*128, 128*i).xor(xorComponent);
        }

        BitSet cipherResult = MSB(nAndU[1], AES_128.cipherBitSetState(key, CB[nAndU[0]]));
        X.get((128)*(nAndU[0]-1),xLength-1).xor(cipherResult);
        meddlingY[nAndU[0]] = X.get((128)*(nAndU[0]-1),xLength-1);
        X.get((128)*(nAndU[0]-1),xLength-1).xor(cipherResult);
        
        BitSet Y = new BitSet((128*(nAndU[0]-1))+ nAndU[1]);
        for (int i = 0; i < meddlingY.length; i++) {
            BitSet currentToAdd = meddlingY[i];
            int index = currentToAdd.nextSetBit(0);
            while(index < currentToAdd.length()){
                Y.set(index+(128*i));
                index = currentToAdd.nextSetBit(index+1);
            }
        }
        return Y;
    }

    private static BitSet MSB(int t, BitSet input) {
        int length = input.length();
        if (t == 1) {
            BitSet toReturn = new BitSet(1);
            toReturn.set(input.get(length - 1) ? 1 : 0);
            return toReturn;
        }
        return input.get(length - t, length - 1);
    }

    private static BitSet[] getArrayFormOfPlaintext(int[] nAndU, String P) {
        StringBuilder plaintextMeddlingVersion = new StringBuilder(P);
        BitSet[] toReturn = new BitSet[nAndU[0] + 1];
        for (int i = 0; i < nAndU[0]; i++) {
            toReturn[i] = stringToBitset(plaintextMeddlingVersion.substring(0, 8));
            plaintextMeddlingVersion.delete(0, 8);
        }
        toReturn[nAndU[0]] = stringToBitset(String.valueOf(plaintextMeddlingVersion));
        return toReturn;
    }

    private static String leadingZeroBinaryRep(int in){
        String toReturn = Integer.toBinaryString(in);
        if (toReturn.charAt(0)=='1'){
            toReturn = "0"+toReturn;
        }
        return toReturn;
    }
    
    public static void gcmEncryption(String K, String P, String IV, String AAD) {
        int[] nAndU = getNAndU(P.length());
        BitSet[] ArrayP = getArrayFormOfPlaintext(nAndU, P);
        BitSet H = Utils.intLinearArrayToBitset(
                AES_128.cipherIntState(K, new int[][]{
                                {0, 0, 0, 0},
                                {0, 0, 0, 0},
                                {0, 0, 0, 0},
                                {0, 0, 0, 0}
                        }
                )
        );
        BitSet[] J = new BitSet[nAndU[0] + 1];
        J[0] = stringToBitset(IV + 0b00000000000000000000000000000001);
        for (int i = 1; i <= nAndU[0]; i++) {
            //increment it
            J[i] = incr(J[i - 1]);
        }
        BitSet A = stringToBitset(AAD);

        BitSet C = GCTR(incr(J[0]), Utils.stringToBitset(P), K);
        int u = 128 * (Utils.divCeil(C.size(), 128)) - C.size();
        int v = 128 * (Utils.divCeil(A.size(), 128)) - A.size();

        BitSet argumentForS = stringToBitset(
                    Utils.bitsetToString(A)
                        + ("0".repeat(v))
                        +Utils.bitsetToString(C)
                        +("0".repeat(u))
                        +leadingZeroBinaryRep(AAD.length()*16)
                        +leadingZeroBinaryRep(C.length())
                );
        BitSet S = GHASH.hash(argumentForS, H);
        BitSet T = MSB(tagLength, GCTR(J[0], S, K));

    }
}


