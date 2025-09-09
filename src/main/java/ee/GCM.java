package ee;


import java.util.BitSet;

import static ee.BetterBitSet.*;
import static ee.Utils.*;

public class GCM {

    private String key;
    private final static int tagLength = 128;
    private final static BetterBitSet thirtyOneZeroesAnd1 = new BetterBitSet(32);

    public void setKey(String K){
        key = K;
    }

    private static int[] getNAndU(int pLength) {
        //calculate length of P in bits
        int[] nAndU = new int[2];
        if (pLength % 128 == 0) {
            //review shouldn't this be -1
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

    public static BetterBitSet inc32(BetterBitSet input) {
        final int s = 32;
        final long twoPowerOfS = 4294967296L;
        final int length = input.length();
        BetterBitSet MSBsection = MSB(length-s, input);
        BetterBitSet LSBsection = input.get(0, s);
        long longRep = LSBsection.bitsetToLong();
        longRep = (longRep+1)&(twoPowerOfS-1);
        LSBsection = Utils.longToBitset(longRep,s);
        input = BetterBitSet.concatenate(MSBsection, LSBsection, LSBsection.length());
        return input;
    }

    private BetterBitSet GCTR(BetterBitSet ICB, BetterBitSet X) {
        if (X.isEmpty()) {
            return new BetterBitSet(X.length());
        }
        //pad to make sure no characters get lost
        int xLength = X.length()+(16 - X.length()%16);//CORRECT
        int[] nAndU = getNAndU(xLength);
        BetterBitSet[] meddlingY = new BetterBitSet[nAndU[0]];
        BetterBitSet[] CB = new BetterBitSet[nAndU[0]];
        CB[0] = ICB;
        for (int d = 0; d <= nAndU[0]; d++) {
            if (2<=d){
                CB[d-1] = inc32(CB[d - 2]);
            }
            if((d>=1)&&(d<nAndU[0])){
                BetterBitSet xorComponent = AES_128.cipherBitSetState(key, CB[d-1]);
                X.get((d-1)*128, 128*d).xor(xorComponent);
                meddlingY[d-1] =X.get((d-1)*128, 128*d);
                X.get((d-1)*128, 128*d).xor(xorComponent);
            }
        }
        BitSet cipherResult = MSB(nAndU[1], AES_128.cipherBitSetState(key, CB[nAndU[0]-1]));
        X.get(128*(nAndU[0]-1),xLength).xor(cipherResult);
        meddlingY[nAndU[0]-1] = X.get((128)*(nAndU[0]-1),xLength);
        X.get(128*(nAndU[0]-1),xLength).xor(cipherResult);//return to normal
        
        BetterBitSet Y = new BetterBitSet((nAndU[0]*128)+nAndU[1]);
        for (int i = 0; i < meddlingY.length; i++) {
            int secondLength = 128;
            if(i==nAndU[0]-1){
                secondLength = nAndU[1];
            }
            Y = BetterBitSet.concatenate(Y, (i+1)*16, meddlingY[i], secondLength);
        }
        return Y;
    }

    public static BetterBitSet MSB(int t, BetterBitSet input) {
        int length = input.length();
        if(length == 0){
            return new BetterBitSet(0);
        }
        if (t == 1) {
            BetterBitSet toReturn = new BetterBitSet(1);
            toReturn.set(input.get(length - 1) ? 1 : 0);
            return toReturn;
        }

        return input.get(length - t, length);
    }

    //review is this necessary
    private static String intLeadingZeroBinaryRep(int in){
        if(in<0){
            throw new RuntimeException("input to intLeadingZeroBianryRep was negative, not meant to be");
        }
        String toReturn = Integer.toBinaryString(in);
        if (toReturn.charAt(0)=='1'){//if highest index bit is 1
            toReturn = "0"+toReturn;
        }
        if(toReturn.length()>Math.pow(2, 64)){
            throw new RuntimeException("mistake in int leading zero binary rep function");
        }
        return toReturn;
    }

    //need to set key before this point
    public EncryptionReturnPackage gcmEncryption(String P, String IV, String givenA) {
        if(key.isEmpty()){
            throw new RuntimeException("You forgot to set a key. You need to set the GCM key before trying to encrypt!");
        }
        BetterBitSet bitsetP = BetterBitSet.asciiStringToBitset(P);
        if (P.length()> 68719476480L){
            throw new RuntimeException("Message too large.");
        }
        //not checking if givenA has valid length, limit is longer than a long, so unlikely
        BetterBitSet IVbitSet = asciiStringToBitset(IV);
        if(IVbitSet.isEmpty()){
            throw new RuntimeException("You need to provide an initialization vector.");
            //not checking if too long, has same limit as A
        }

        thirtyOneZeroesAnd1.set(0);//follow with 31 0's
        BetterBitSet zeroes = new BetterBitSet(128);//todo dc, i think max is 128
        BetterBitSet A = asciiStringToBitset(givenA);
        BetterBitSet H = Utils.intLinearArrayToBitset(
                AES_128.cipherIntState(key, new int[][]{
                                {0, 0, 0, 0},
                                {0, 0, 0, 0},
                                {0, 0, 0, 0},
                                {0, 0, 0, 0}
                        }
                )
        );
        BetterBitSet JZero = concatenate(IVbitSet,thirtyOneZeroesAnd1,32);
        BetterBitSet C = GCTR(inc32(JZero),bitsetP);
        int cLength = C.length()+(16 - C.length()%16);
        int aLength = A.length()+(16 - A.length()%16);
        int u = (divCeil(cLength,128)*128)-cLength;
        int v = (divCeil(aLength,128)*128)-aLength;

        BetterBitSet argumentForS = concatenate(A, aLength, zeroes, v);
        argumentForS = concatenate(argumentForS,v+aLength,  C, cLength);
        argumentForS = concatenate(argumentForS,v+aLength+cLength, zeroes, u);
        argumentForS = concatenate(argumentForS, v+aLength+cLength+u,binaryStringToBetterBitSet(intLeadingZeroBinaryRep(aLength)),64);
        argumentForS = concatenate(argumentForS,v+aLength+cLength+u+64, binaryStringToBetterBitSet(intLeadingZeroBinaryRep(cLength)),64);
        BetterBitSet S = GHASH.hash(argumentForS, H);
        BetterBitSet gctrResult = GCTR(JZero,S);
        BetterBitSet T = MSB(tagLength, gctrResult);
        return new EncryptionReturnPackage(C.bitSetToAsciiString(), T.bitSetToAsciiString(), givenA);
    }

    //Assume all input strings are given in utf-8 format
    // IV bit length = 96, char length = 6
    //review, messed up handling of bit strings
    public String gcmDecryption(String IV, String C, String A, String T){
        final String FAIL_MESSAGE = "FAIL";
        System.out.println(T.length()+" == "+tagLength);
        System.out.println(C.length() +"length of C");
        System.out.println(IV.length() + " length of iv");
        if(((IV.length() != 6))||T.length()!=tagLength||C.length()*16L  >68719476480L){
            return FAIL_MESSAGE;
        }
        BetterBitSet H = Utils.intLinearArrayToBitset(
                AES_128.cipherIntState(key, new int[][]{
                                {0, 0, 0, 0},
                                {0, 0, 0, 0},
                                {0, 0, 0, 0},
                                {0, 0, 0, 0}
                        }
                )
        );
        BetterBitSet JZero = BetterBitSet.concatenate(asciiStringToBitset(IV), 96, thirtyOneZeroesAnd1, 32);
        BetterBitSet P = GHASH.hash(inc32(JZero), asciiStringToBitset(C));
        int u = 128 * Utils.divCeil(C.length()*16, 128)- (C.length()*16);
        int v = 128 * Utils.divCeil(A.length()*16, 128) -(A.length()*16);

        BetterBitSet bitSetA = asciiStringToBitset(A);
        BetterBitSet bitsetC = asciiStringToBitset(C);
        BetterBitSet argumentForS = BetterBitSet.concatenate(bitSetA, A.length()*16, new BetterBitSet(v), v);
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
        if (T.equals(bitsetToBinaryString(TChanged))){//double check review works
            return P.bitSetToAsciiString();
        }else {
           return FAIL_MESSAGE;
        }


    }
}


