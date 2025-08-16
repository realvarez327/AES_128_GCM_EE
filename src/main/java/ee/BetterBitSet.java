package ee;

import java.util.BitSet;

public class BetterBitSet extends BitSet {

    BetterBitSet(){
        super();
    }

    public static BetterBitSet asciiStringToBitset(String input){//todo double check all usages are correct
        BetterBitSet toReturn = new BetterBitSet();
        StringBuilder inputAsBitString = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            char current = input.charAt(i);
            if(current < 0x80) {
                inputAsBitString.append("0");
                if (current <0x40){
                    inputAsBitString.append("0");
                }
            }
                inputAsBitString.append(Integer.toBinaryString(current));


        }
        System.out.println(inputAsBitString);
        int index = inputAsBitString.indexOf("1");
        while(index >=0){
            toReturn.set(index);
            index = inputAsBitString.indexOf("1", index +1);
        }
        return toReturn;
    }
    public static BetterBitSet concatenate(BitSet first, BitSet second, int bitsSecond){
        final int lengthOfFirst = first.length();
        final int lengthOfSecond = second.length();
        if (lengthOfSecond>bitsSecond){
            throw new RuntimeException("second bitset is larger than expected");
        }

        final BetterBitSet toReturn = (BetterBitSet) first.clone();
        int highestSetIndexOfSecond = second.nextSetBit(0);
        while ((highestSetIndexOfSecond<lengthOfSecond)&&highestSetIndexOfSecond!=-1){
            toReturn.set(highestSetIndexOfSecond+lengthOfFirst);
            highestSetIndexOfSecond = second.nextSetBit(highestSetIndexOfSecond+1);
        }
        return toReturn;
    }
    public static BetterBitSet concatenate(BitSet first,int bitsFirst,  BitSet second, int bitsSecond){
        final int lengthOfFirst = first.length();
        final int lengthOfSecond = second.length();
        if (lengthOfSecond>bitsSecond){
            throw new RuntimeException("second bitset is larger than expected");
        }

        final BetterBitSet toReturn = (BetterBitSet) first.clone();
        int highestSetIndexOfSecond = second.nextSetBit(0);
        while ((highestSetIndexOfSecond<lengthOfSecond)&&highestSetIndexOfSecond!=-1){
            toReturn.set(highestSetIndexOfSecond+lengthOfFirst);
            highestSetIndexOfSecond = second.nextSetBit(highestSetIndexOfSecond+1);
        }
        return toReturn;
    }
    public int bitsetToInteger() {
        //get one bitset, give one integer
        int toReturn = 0;
        byte[] byteVersionOfInput = this.toByteArray();
        for(int i = byteVersionOfInput.length-1; i > -1; i--){
            toReturn <<= 8;
            toReturn ^=byteVersionOfInput[i];
        }
        return toReturn;
    }


    public boolean get(int onlyIndex){
        return super.get(onlyIndex);
    }

    public BetterBitSet get(int fromIndex, int toIndex){
        return bitSetToBetterBitSet(super.get(fromIndex, toIndex));
    }

    public static BetterBitSet bitSetToBetterBitSet(BitSet in){
        BetterBitSet toReturn = new BetterBitSet();
        int setIndex = in.nextSetBit(0);
        while(setIndex!=-1){
            toReturn.set(setIndex);
            setIndex = in.nextSetBit(setIndex+1);
        }
        return toReturn;
    }
}
