package ee;

import java.util.BitSet;

import static ee.Utils.stringToBitset;

public class GCM {



    static int[] getNAndU(String P){
        //calculate length of P in bits
        int[] nAndU = new int[2];
        int stringBitLength = P.length()*16;
        if(stringBitLength%128==0){
            nAndU[0]=(stringBitLength)/128;
            nAndU[1]=128;
            return nAndU;
        }
        //only reach here if not a nicely blocked thing
        stringBitLength -= 128;
        nAndU[0] = stringBitLength/128;
        nAndU[1]= stringBitLength%128;
        return nAndU;
    }

    static BitSet incr(BitSet input){
        //input is 16 bytes
        //lord forgive me for this awful code i am going to write
        int size = input.size();
        BitSet toReturn = input;
        if(toReturn.get(size-1-5)){
            //check if "32" bit is set
            toReturn.set(size-1-5, size-1, false);
        }else{
            //increment last integer spot
            String s = toReturn.toString().substring(size-1-5, size-1);
            for (int i = 0; i < s.length(); i++) {
                if(s.charAt(i)=='0'){
                    toReturn.set(size-6+i, 0);
                }else{
                    toReturn.set(size-6+i, 1);
                }
            }
        }
        return toReturn;
    }


    static BitSet[] getArrayFormOfPlaintext(int[] nAndU, String P){
        StringBuilder plaintextMeddlingVersion = new StringBuilder(P);
        BitSet[] toReturn = new BitSet[nAndU[0]+1];
        int currStringIndex = 0;
        for (int i = 0; i < nAndU[0]; i++) {
            toReturn[i] = stringToBitset(plaintextMeddlingVersion.substring(0, 8));
            plaintextMeddlingVersion.delete(0, 8);
        }
        toReturn[nAndU[0]]= stringToBitset(String.valueOf(plaintextMeddlingVersion));
        return toReturn;
    }

    static void gcmEncryption(String K, String P, String IV, String AAD){

        int[] nAndU = getNAndU(P);
        BitSet[] ArrayP = getArrayFormOfPlaintext(nAndU, P);
        //Fill ArrayP with P, 128 bit strings until n reached. Then fill last index with m last bits
        int[] H = AES_128.cipher(K,new int[][]{
                {0,0,0,0},
                {0,0,0,0},
                {0,0,0,0},
                {0,0,0,0}
        });
        //todo H should be a bitset
        BitSet[] Y = new BitSet[nAndU[0]+1];
        Y[0] = stringToBitset(IV + 0b00000000000000000000000000000001);
        for (int i = 1; i <= nAndU[0]; i++) {
            //increment it
            Y[i] = incr(Y[i-1]);
        }

        BitSet[] C = new BitSet[nAndU[0]+1];
        for (int i = 1; i < nAndU[0]; i++) {
            ArrayP[i].xor(Utils.intLinearArrayToBitset(AES_128.cipher(K, Utils.bitsetToTwoDimensionalIntArray(Y[i]))));
            C[i] = ArrayP[i];
            ArrayP[i].xor(Utils.intLinearArrayToBitset(AES_128.cipher(K, Utils.bitsetToTwoDimensionalIntArray(Y[i]))));
            //todo: Find a better way to do this and implement helper functions
        }
        //take care of extra bits, xor extra ArrayP bits with u most significant bits of cipher (K, Yn)
        //T(tag) = t(tag.length) most sig bits of[ Ghash(H, A, C) xored with cipher of K and Y0]



    }
}
