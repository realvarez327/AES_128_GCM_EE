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


    static void gcmEncryption(String K, String P, String IV, String AAD){
        int[] nAndU = getNAndU(P);
        int n = nAndU[0];
        int m = nAndU[1]; // try to just use the int array actually
        int[] H = AES_128.cipher(K,new int[][]{
                {0,0,0,0},
                {0,0,0,0},
                {0,0,0,0},
                {0,0,0,0}
        });
        BitSet[] Y = new BitSet[n+1];
        Y[0] = stringToBitset(IV + 0b00000000000000000000000000000001);
        for (int i = 1; i <= n; i++) {
            //increment it
            Y[i] = incr(Y[i-1]);
        }

    }
}
