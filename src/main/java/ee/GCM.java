package ee;


import static ee.BetterBitSet.*;
import static ee.Utils.characterBits;


public class GCM {

    private String key;
    private final static int tagLength = 128;
    private final static BetterBitSet thirtyOneZeroesAnd1 = new BetterBitSet(32);

    public void setKey(String K) {
        key = K;
        thirtyOneZeroesAnd1.set(0, true);
    }

    public static BetterBitSet inc32(BetterBitSet input) {
        final int s = 32;
        final long twoPowerOfS = 4294967296L;
        final int length = input.length();

        BetterBitSet MSBsection = MSB(length - s, input);

        BetterBitSet LSBsection = input.get(0, s);
        assert LSBsection.length() == s;

        long longRep = LSBsection.bitsetToLong();
        longRep = (longRep + 1) & (twoPowerOfS - 1);
        LSBsection = longToBitset(longRep, s);

        assert LSBsection.length() == s;
        input = BetterBitSet.concatenate(MSBsection, LSBsection, s);
        return input;
    }

    private BetterBitSet GCTR(BetterBitSet ICB, BetterBitSet X) {
        if (X.isEmpty()) {
            return new BetterBitSet(X.length());
        }
        int xLength = X.length();
        BetterBitSet Y = new BetterBitSet(0);
        int n = Math.ceilDiv(xLength, 128);
        BetterBitSet[] CB = new BetterBitSet[n];
        int u = xLength % 128;
        CB[0] = ICB;
        for (int i = 2; i <= n; i++) {
            CB[i - 1] = inc32(CB[i - 2]);
        }
        for (int i = 1; i < n; i++) {
            BetterBitSet X_Section = X.get(128 * (i - 1), 128 * i);
            X_Section.xor(AES_128.cipherBitSetState(key, CB[i - 1]));
            assert X_Section.length() == 128;
            Y = BetterBitSet.concatenate(Y, X_Section, 128);
        }
        int lastBlockLength = (u==0?128:u);

        BetterBitSet xorComponent = X.get(xLength-lastBlockLength, xLength);
        xorComponent.xor(MSB(lastBlockLength, AES_128.cipherBitSetState(key, CB[n - 1])));
        assert xorComponent.length() == lastBlockLength;
        Y = BetterBitSet.concatenate(Y, xorComponent, lastBlockLength);
        assert Y.length() == (128 * (n - 1)) + lastBlockLength;
        assert Y.length() == X.length();
        return Y;
    }

    public static BetterBitSet MSB(int t, BetterBitSet input) {
        int length = input.length();
        if (length == 0 || t == 0) {
            return new BetterBitSet(0);
        }
        if (t == 1) {
            BetterBitSet toReturn = new BetterBitSet(1);
            toReturn.set(0, input.get(length - 1));
            return toReturn;
        }

        return input.get(length - t, length);
    }

    //turn BetterBitSet input into BetterBitSet containing length
    public static BetterBitSet prepLength64Variables(BetterBitSet input) {
        long len = input.length();
        BetterBitSet intRep = longToBitset(len, 64);
        return intRep;

    }

    //need to set key before this point
    public EncryptionReturnPackage gcmEncryption(String P, String IV, String givenA) {
        if (key.isEmpty()) {
            throw new RuntimeException("You forgot to set a key. You need to set the GCM key before trying to encrypt!");
        }
        if (IV.length() != 12) {
            throw new RuntimeException("Once parsed, this iv wont be 96 bits. One char is 8 bits, give 12 characters.");
        }
        BetterBitSet bitsetP = asciiStringToBitset(P);
        if (P.length() > 68719476480L) {
            throw new RuntimeException("Message too large.");
        }
        //not checking if givenA has valid length, limit is longer than a long, so unlikely
        BetterBitSet IVbitSet = asciiStringToBitset(IV);
        assert IVbitSet.length() == 96;
        if (IVbitSet.isEmpty()) {
            throw new RuntimeException("You need to provide an initialization vector.");
            //not checking if too long, has same limit as A
        }

        BetterBitSet zeroes = new BetterBitSet(128);//todo dc, i think max is 128
        BetterBitSet A = asciiStringToBitset(givenA);
        BetterBitSet H = AES_128.cipherBitSetState(key, new BetterBitSet(128));
        assert H.length() == 128;
        BetterBitSet JZero = concatenate(IVbitSet, thirtyOneZeroesAnd1, 32);
        assert JZero.length() == 128;
        BetterBitSet C = GCTR(inc32(JZero), bitsetP);
        int u = 128 * Math.ceilDiv(C.length(), 128) - C.length();
        int v = 128 * Math.ceilDiv(A.length(), 128) - A.length();

        BetterBitSet argumentForS;

        if (!A.isEmpty()) {
            argumentForS = concatenate(A, A.length(), zeroes, v);
            argumentForS = concatenate(argumentForS, v + A.length(), C, C.length());
        } else {
            argumentForS = C;
        }
        argumentForS = concatenate(argumentForS, v + A.length() + C.length(), zeroes, u);
        System.out.println("current arg length = " + (v + A.length() + C.length()));

        argumentForS = concatenate(argumentForS, v + A.length() + C.length() + u, prepLength64Variables(A), 64);
        argumentForS = concatenate(argumentForS, v + A.length() + C.length() + u + 64, prepLength64Variables(C), 64);

        BetterBitSet S = GHASH.hash(argumentForS, H);

        BetterBitSet gctrResult = GCTR(JZero, S);
        BetterBitSet T = MSB(tagLength, gctrResult);
        return new EncryptionReturnPackage(C.bitSetToAsciiString(), T.bitSetToAsciiString(), givenA);
    }

    //Assume all input strings are given in utf-8 format
    // IV bit length = 96, char length = 6
    //review, messed up handling of bit strings
    public String gcmDecryption(String IV, String C, String A, String T) {
        final String FAIL_MESSAGE = "FAIL";
        if (IV.length() != (96 / characterBits)) {
            return FAIL_MESSAGE;
        }
        if ((T.length() * characterBits) != tagLength) {
            return FAIL_MESSAGE;
        }
        if (C.length() * (long) characterBits > 68719476480L) {
            return FAIL_MESSAGE;
        }

        BetterBitSet H = AES_128.cipherBitSetState(key, new BetterBitSet(128));
        BetterBitSet JZero = BetterBitSet.concatenate(asciiStringToBitset(IV), 96, thirtyOneZeroesAnd1, 32);
        BetterBitSet BitsetC = asciiStringToBitset(C);
        BetterBitSet BitSetP = GCTR(inc32(JZero), BitsetC);
        BetterBitSet BitsetA = asciiStringToBitset(A);

        int u = 128 * Math.ceilDiv(BitsetC.length(), 128) - BitsetC.length();
        int v = 128 * Math.ceilDiv(BitsetA.length(), 128) - BitsetA.length();

        BetterBitSet argumentForS;
        if (!BitsetA.isEmpty()) {
            argumentForS = BetterBitSet.concatenate(BitsetA, BitsetA.length(), new BetterBitSet(v), v);
            argumentForS = BetterBitSet.concatenate(argumentForS, BitsetA.length() + v, BitsetC, BitsetC.length());
        } else {
            argumentForS = BitsetC;
        }

        BetterBitSet lenA64 = prepLength64Variables(BitsetA);
        BetterBitSet lenC64 = prepLength64Variables(BitsetC);
        argumentForS = BetterBitSet.concatenate(argumentForS, BitsetA.length() + v + BitsetC.length(), new BetterBitSet(u), u);
        argumentForS = BetterBitSet.concatenate(argumentForS, BitsetA.length() + v + BitsetC.length() + u, lenA64, 64);
        argumentForS = BetterBitSet.concatenate(argumentForS, BitsetA.length() + v + BitsetC.length() + u + 64, lenC64, 64);
        assert argumentForS.length() % 128 == 0;
        BetterBitSet S = GHASH.hash(argumentForS, H);
        assert S.length() == 128;
        BetterBitSet TGiven = asciiStringToBitset(T);
        BetterBitSet TChanged = MSB(tagLength, GCTR(JZero, S));
        System.out.println(BitSetP.bitSetToAsciiString());
        if (TGiven.equals(TChanged)) {//double check review works

            return BitSetP.bitSetToAsciiString();
        } else {
            return FAIL_MESSAGE;
        }


    }
}


