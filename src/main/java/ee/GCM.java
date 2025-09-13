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
        int xLength = X.length();//CORRECT
        int n = divCeil(X.length(), 128);
        int u = X.length()%128;
        BetterBitSet[] meddlingY = new BetterBitSet[n];
        BetterBitSet[] CB = new BetterBitSet[n];
        CB[0] = ICB;
        for (int d = 0; d <= n; d++) {
            if (2<=d){
                CB[d-1] = inc32(CB[d - 2]);
            }
            if((d>=1)&&(d<n)){
                BetterBitSet xorComponent = AES_128.cipherBitSetState(key, CB[d-1]);
                X.get((d-1)*128, 128*d).xor(xorComponent);
                meddlingY[d-1] =X.get((d-1)*128, 128*d);
                X.get((d-1)*128, 128*d).xor(xorComponent);
            }
        }
        BitSet cipherResult = MSB(u, AES_128.cipherBitSetState(key, CB[n-1]));//dc review
        X.get(128*(n-1),xLength).xor(cipherResult);
        meddlingY[n-1] = X.get((128)*(n-1),xLength);
        X.get(128*(n-1),xLength).xor(cipherResult);//return to normal
        
        BetterBitSet Y = new BetterBitSet((n*128)+u);
        for (int i = 0; i < meddlingY.length; i++) {
            int secondLength = 128;
            if(i==n-1){
                secondLength = u;
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

    //turn BetterBitSet input into BetterBitSet containing length
    public static BetterBitSet prepLength64Variables(BetterBitSet input){
        int len = input.length();
        BetterBitSet intRep = intToBitset(len);
        int intRepLen = intRep.length();
        return BetterBitSet.concatenate(intRep, new BetterBitSet(64-intRepLen), 64-intRepLen);
    }

    //need to set key before this point
    public EncryptionReturnPackage gcmEncryption(String P, String IV, String givenA) {
        if(key.isEmpty()){
            throw new RuntimeException("You forgot to set a key. You need to set the GCM key before trying to encrypt!");
        }
        BetterBitSet bitsetP = asciiStringToBitset(P);
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
        BetterBitSet H = intLinearArrayToBitset(
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

        int u = (divCeil(C.length(),128)*128)-C.length();
        int v = (divCeil(A.length(),128)*128)-A.length();

        BetterBitSet argumentForS = concatenate(A, A.length(), zeroes, v);
        argumentForS = concatenate(argumentForS,v+A.length(),  C, C.length());
        argumentForS = concatenate(argumentForS,v+A.length()+C.length(), zeroes, u);
        argumentForS = concatenate(argumentForS, v+A.length()+C.length()+u,prepLength64Variables(A),64);
        argumentForS = concatenate(argumentForS,v+A.length()+C.length()+u+64, prepLength64Variables(C),64);
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
        if(IV.length() != 6){
            return FAIL_MESSAGE;
        }
        if(T.length()!=tagLength){
            return FAIL_MESSAGE;
        }
        if(C.length()*16L>68719476480L){
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
        BetterBitSet BitsetC = asciiStringToBitset(C);
        BetterBitSet BitSetP = GCTR(inc32(JZero), BitsetC);
        BetterBitSet BitsetA = asciiStringToBitset(A);
        int u = 128 * Utils.divCeil(BitsetC.length(), 128)- BitsetC.length();
        int v = 128 * Utils.divCeil(BitsetA.length(), 128) -BitsetA.length();

        BetterBitSet argumentForS = BetterBitSet.concatenate(BitsetA, BitsetA.length(), new BetterBitSet(v), v);
        argumentForS = BetterBitSet.concatenate(argumentForS, BitsetA.length() +v, BitsetC, BitsetC.length());
        BetterBitSet lenA64 = prepLength64Variables(BitsetA);//review
        BetterBitSet lenC64 = prepLength64Variables(BitsetC);
        argumentForS = BetterBitSet.concatenate(argumentForS, BitsetA.length()+v+ BitsetC.length(),new BetterBitSet(u), u);
        argumentForS = BetterBitSet.concatenate(argumentForS,BitsetA.length()+v+ BitsetC.length()+u,lenA64,lenA64.length());
        argumentForS = BetterBitSet.concatenate(argumentForS,BitsetA.length()+v+ BitsetC.length()+u+lenA64.length(),lenC64,lenC64.length());

        BetterBitSet S = GHASH.hash(argumentForS, H);
        BetterBitSet TChanged = MSB(tagLength, GCTR(JZero, S));
        if (T.equals(bitsetToBinaryString(TChanged))){//double check review works
            return BitSetP.bitSetToAsciiString();
        }else {
           return FAIL_MESSAGE;
        }


    }
}


