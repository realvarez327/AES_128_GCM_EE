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
        int size = input.size();
        BitSet toReturn = input;
        if (toReturn.get(size - 1 - 5)) {
            //check if "32" bit is set
            toReturn.set(size - 1 - 5, size - 1, false);
        } else {
            //increment last integer spot
            String s = toReturn.toString().substring(size - 1 - 5, size - 1);
            for (int i = 0; i < s.length(); i++) {
                if (s.charAt(i) == '0') {
                    toReturn.set(size - 6 + i, 0);
                } else {
                    toReturn.set(size - 6 + i, 1);
                }
            }
        }
        return toReturn;
    }

    //todo continue implementing
    private static BitSet GCTR(BitSet ICB, BitSet X, String key){
        BitSet Y = new BitSet(X.size());
        if (X.isEmpty()){
            return Y;
        }
        int n = getNAndU(X.size())[0];
        BitSet[] CB = new BitSet[n+1];
        CB[1] = ICB;
        for (int i = 2; i <=n; i++){
            CB[i] = incr(CB[i-1]);
        }
        for (int i = 1; i < n; i++) {

        }

        return new BitSet(2);
    }


    //todo implement
    private static BitSet MSB(int t) {
        return new BitSet(1);
    }


    private static BitSet[] getArrayFormOfPlaintext(int[] nAndU, String P) {
        StringBuilder plaintextMeddlingVersion = new StringBuilder(P);
        BitSet[] toReturn = new BitSet[nAndU[0] + 1];
        int currStringIndex = 0;
        for (int i = 0; i < nAndU[0]; i++) {
            toReturn[i] = stringToBitset(plaintextMeddlingVersion.substring(0, 8));
            plaintextMeddlingVersion.delete(0, 8);
        }
        toReturn[nAndU[0]] = stringToBitset(String.valueOf(plaintextMeddlingVersion));
        return toReturn;
    }

    private static String zeroesStringOfGivenLength(int a){
        return "0".repeat(a);

    }

    public static void gcmEncryption(String K, String P, String IV, String AAD) {

        int[] nAndU = getNAndU(P.length());
        BitSet[] ArrayP = getArrayFormOfPlaintext(nAndU, P);
        //Fill ArrayP with P, 128 bit strings until n reached. Then fill last index with m last bits
        BitSet H = Utils.intLinearArrayToBitset(
                AES_128.cipher(K, new int[][]{
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
        int u = 128*(Utils.divCeil(C.size(), 128)) - C.size();
        int v = 128*(Utils.divCeil(A.size(),128)) -A.size();

       // BitSet argumentForS = stringToBitset(
//                AAD
//                        + ("0".repeat(v))
//                        +Utils.bitsetToString(C)
//                        +("0".repeat(u))
//                        +Integer.toBinaryString(AAD.length()*16)//todo continue
//                )
//        BitSet S = GHASH.hash()
    }
}
