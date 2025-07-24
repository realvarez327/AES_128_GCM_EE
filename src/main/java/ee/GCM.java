package ee;

import java.util.BitSet;

public class GCM {
    //todo, revisit
    static BitSet stringToBitset(String input){
        BitSet toReturn = new BitSet(input.length()*16);
        StringBuilder inputAsBitString = new StringBuilder();
        // not setting capacity bc dont want it to be too big
        for (int i = 0; i < input.length(); i++) {
            inputAsBitString.append(Integer.toBinaryString(input.charAt(i)));
        }
        for (int i = 0; i < inputAsBitString.length(); i++) {
            if(inputAsBitString.charAt(i)=='1'){
                toReturn.set(i);
            }
        }
        return toReturn;
    }



    static void gcmEncryption(String K, String P, String IV, String AAD){
        int n = 2; //todo this should eventually have amount of full "blocks in P" IMPLEMENT
        int[] H = AES_128.cipher(K,new int[][]{
                {0,0,0,0},
                {0,0,0,0},
                {0,0,0,0},
                {0,0,0,0}
        });
        BitSet[] Y = new BitSet[n+1];
        Y[0] = stringToBitset(IV + 0b00000000000000000000000000000001);

    }
}
