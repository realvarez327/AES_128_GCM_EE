package ee; //successful


import java.util.BitSet;

import static ee.BetterBitSet.asciiStringToBitset;
import static ee.Utils.*;

public class GCM {

    private String key;
    private final static int tagLength = 128;
    private final static BetterBitSet thirtyOneZeroesAnd1 = new BetterBitSet();

    public void setKey(String K){
        key = K;
    }

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

    private static BetterBitSet incr(BetterBitSet input) {
        //input is 16 bytes
        final int s = 5;

        final int length = input.length();
        final BitSet lsb = input.get(length - s-1, length-1);

        if (input.get(length - 6)) {
            //check if "32" bit is set
            input.set(length - 6, length - 1, false);
        } else {
            //increment last integer spot
            String str = input.toString().substring(length - 1 - 5, length - 1);
            for (int i = 0; i < str.length(); i++) {
                input.set(length - 6 + i, str.charAt(i) != '0');
            }
        }
        return input;
    }

    private BetterBitSet GCTR(BetterBitSet ICB, BetterBitSet X) {
        if (X.isEmpty()) {
            return new BetterBitSet();
        }

        int xLength = X.length();
        int n = Utils.divCeil(xLength,128);
        int[] nAndU = getNAndU(xLength);
        BetterBitSet[] meddlingY = new BetterBitSet[nAndU[0]];
        BetterBitSet[] CB = new BetterBitSet[n];
        CB[0] = ICB;

        for (int i = 2; i <= n; i++) {
            CB[i-1] = incr(CB[i - 2]);
        }

        for (int i = 1; i < n-1; i++) {
            BetterBitSet xorComponent = AES_128.cipherBitSetState(key, CB[i-1]);
            X.get((i-1)*128, 128*i).xor(xorComponent);
            meddlingY[i-1] =X.get((i-1)*128, 128*i);
            X.get((i-1)*128, 128*i).xor(xorComponent);//return to normal
        }

        BitSet cipherResult = MSB(nAndU[1], AES_128.cipherBitSetState(key, CB[n-1]));
        X.get((128)*(n-1),xLength-1).xor(cipherResult);
        meddlingY[n-1] = X.get((128)*(n-1),xLength-1);
        X.get((128)*(n-1),xLength-1).xor(cipherResult);//return to normal
        
        BetterBitSet Y = new BetterBitSet();
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

    private static BetterBitSet MSB(int t, BetterBitSet input) {
        int length = input.length();
        if (t == 1) {
            BetterBitSet toReturn = new BetterBitSet();
            toReturn.set(input.get(length - 1) ? 1 : 0);
            return toReturn;
        }
        return input.get(length - t, length - 1);
    }

    //review is this necessary
    private static String intLeadingZeroBinaryRep(int in){
        String toReturn = Integer.toBinaryString(in);
        if (toReturn.charAt(0)=='1'){
            toReturn = "0"+toReturn;
        }
        return toReturn;
    }

    //need to set key before this point
    public EncryptionReturnPackage gcmEncryption(String P, String IV, String givenA) {
        if(key.isEmpty()){
            throw new RuntimeException("You forgot to set a key. You need to set the GCM key before trying to encrypt!");
        }
        BetterBitSet bitsetP = BetterBitSet.asciiStringToBitset(P);
        if (bitsetP.length()> 68719476480L){
            throw new RuntimeException("Message too large.");
        }
        //not checking if givenA has valid length, limit is longer than a long, so unlikely
        BetterBitSet IVbitSet = asciiStringToBitset(IV);
        if(IVbitSet.isEmpty()){
            throw new RuntimeException("You need to provide an initialization vector.");
            //not checking if too long, has same limit as A
        }

        thirtyOneZeroesAnd1.set(0);//follow with 31 0's
        int[] nAndU = getNAndU(P.length());

        BetterBitSet H = Utils.intLinearArrayToBitset(
                AES_128.cipherIntState(key, new int[][]{
                                {0, 0, 0, 0},
                                {0, 0, 0, 0},
                                {0, 0, 0, 0},
                                {0, 0, 0, 0}
                        }
                )
        );

        BetterBitSet[] J = new BetterBitSet[nAndU[0] + 1];
        J[0] = BetterBitSet.concatenate(IVbitSet, thirtyOneZeroesAnd1,32);
        for (int i = 1; i <= nAndU[0]; i++) {
            //increment it
            J[i] = incr(J[i - 1]);
        }
        BitSet A = asciiStringToBitset(givenA);
        BetterBitSet C = GCTR(incr(J[0]), asciiStringToBitset(P));
        int u = 128 * (Utils.divCeil(C.size(), 128)) - C.size();
        int v = 128 * (Utils.divCeil(A.size(), 128)) - A.size();
        BetterBitSet bitSetOfZeroes = new BetterBitSet();
        BetterBitSet argumentForS = BetterBitSet.concatenate(A, bitSetOfZeroes, v);
        BetterBitSet bitsetA = asciiStringToBitset(intLeadingZeroBinaryRep(givenA.length()*16));

        argumentForS = BetterBitSet.concatenate(argumentForS, C, C.length());
        argumentForS = BetterBitSet.concatenate(argumentForS, bitSetOfZeroes, u);
        argumentForS = BetterBitSet.concatenate(argumentForS, bitsetA, givenA.length()*16);
        BetterBitSet bitsetC = asciiStringToBitset(intLeadingZeroBinaryRep(C.length()*16));
        argumentForS = BetterBitSet.concatenate(argumentForS, bitsetC, C.length()*16);

        BetterBitSet S = GHASH.hash(argumentForS, H);
        BetterBitSet T = MSB(tagLength, GCTR(J[0], S));
        return new EncryptionReturnPackage(bitsetToBinaryString(C), bitsetToBinaryString(T), givenA);

    }

    //Assume all input strings are given in utf-8 format
    // IV bit length = 96, char length = 6
    public String gcmDecryption(String IV, String C, String A, String T){
        final String FAIL_MESSAGE = "FAIL";
        if(((IV.length() != 6))||T.length()!=tagLength||C.length()*16L  >68719476480L){
            return FAIL_MESSAGE;
        }
        BitSet H = Utils.intLinearArrayToBitset(
                AES_128.cipherIntState(key, new int[][]{
                                {0, 0, 0, 0},
                                {0, 0, 0, 0},
                                {0, 0, 0, 0},
                                {0, 0, 0, 0}
                        }
                )
        );
        BetterBitSet JZero = BetterBitSet.concatenate(asciiStringToBitset(IV), 96, thirtyOneZeroesAnd1, 32);
        BetterBitSet P = GHASH.hash(incr(JZero), asciiStringToBitset(C));
        int u = 128 * Utils.divCeil(C.length()*16, 128)- (C.length()*16);
        int v = 128 * Utils.divCeil(A.length()*16, 128) -(A.length()*16);

        BetterBitSet bitSetA = asciiStringToBitset(A);
        BetterBitSet bitsetC = asciiStringToBitset(C);
        BetterBitSet argumentForS = BetterBitSet.concatenate(bitSetA, A.length()*16, new BetterBitSet(), v);
        argumentForS = BetterBitSet.concatenate(argumentForS, A.length()*16 +v, bitsetC, C.length()*16);
        String lenA64 = intLeadingZeroBinaryRep(A.length()*16);
        String lenC64 = intLeadingZeroBinaryRep(C.length()*16);
        argumentForS = BetterBitSet.concatenate(
                argumentForS,
                A.length()*16+v+C.length()*16,
                Utils.binaryStringToBetterBitSet("0".repeat(u)+lenA64 +lenC64),
                u+lenA64.length()+lenC64.length()
        );
        BetterBitSet S = GHASH.hash(argumentForS, H);
        BetterBitSet TChanged = MSB(tagLength, GCTR(JZero, S));
        if (T.equals(bitsetToBinaryString(TChanged))){
            return P.bitSetToAsciiString();
        }else {
            return FAIL_MESSAGE;
        }


    }
}


