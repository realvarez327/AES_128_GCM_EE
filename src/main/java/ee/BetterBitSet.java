package ee;

import java.util.BitSet;

import static ee.Utils.floorDivDouble;
import static ee.Utils.characterBits;

public class BetterBitSet extends BitSet {
    int properLength;
    BetterBitSet(int lengthGiven) {
        super(lengthGiven);
        this.properLength = lengthGiven;
    }

    //todo test
    public static BetterBitSet longToBitset(long in, int bitSetLength){
        BetterBitSet toReturn = new BetterBitSet(bitSetLength);
        for (int i = 0; i <bitSetLength; i++){
            if(((in>>i)&1L) == 1L){
                toReturn.set(i);
            }
        }
        return toReturn;
    }

    public void setProperLength(int properLength) {
        this.properLength = properLength;
    }

    @Override
    public int length() {
        return this.properLength;
    }

    @Override
    public void set(int bitIndex) {
        if(bitIndex<this.properLength){
            super.set(bitIndex);
        }else {
            throw new RuntimeException("You tried to access an index, "+bitIndex+", out of length :(");
        }

    }

    @Override
    public void set(int fromIndex, int toIndex) {
        if (toIndex <= this.properLength) {
            super.set(fromIndex, toIndex);
        }else {
            throw new IndexOutOfBoundsException(
                    "You tried to access an index,"+ toIndex+", that was out of length."
            );
        }

    }

    @Override
    public void set(int bitIndex, boolean value) {
        if(bitIndex<this.properLength){
            super.set(bitIndex, value);
        }else {
            throw new IndexOutOfBoundsException(
                    "You tried to access an index,"+ bitIndex+", that was out of length."
            );
        }
    }

    public static String bitSetToAsciiString(BetterBitSet input) {
        int index = 0;
        StringBuilder toReturn = new StringBuilder();
        int intform;
        while ((characterBits + index) <= input.length()) {
            intform = input.get(index, index + characterBits).bitsetToInteger();
            toReturn.append((char) intform);
            index+= 16;
        }

        if (index < input.length() - 1) {
            intform = input.get(index, input.length()).bitsetToInteger();
            toReturn.append((char) intform);
        }

        return toReturn.reverse().toString();
    }

    public String bitSetToAsciiString() {
        int index = 0;
        StringBuilder toReturn = new StringBuilder();
        int intform;
        while ((characterBits + index) <= this.length()) {
            intform = this.get(index, index + characterBits).bitsetToInteger();
            toReturn.append((char) intform);
            index+= characterBits;
        }

        if (index < this.length() - 1) {
            intform = this.get(index, this.length()).bitsetToInteger();
            toReturn.append((char) intform);
        }

        return toReturn.reverse().toString();
    }

    //todo test
    public static BetterBitSet asciiStringToBitset(String input) {
        int len = input.length();
        StringBuilder reversedInput = new StringBuilder(input).reverse();
        BetterBitSet toReturn = new BetterBitSet(len*characterBits);
        for (int i = len-1; i > -1; i--) {
            int curr = reversedInput.charAt(i);
            for (int j = 0; j <=characterBits; j++){// < or <=?, see full range of j
                if(((curr>>>j)&1) == 1){
                    toReturn.set((characterBits*i)+j);
                }
                if((2<<j)>curr){
                    break;
                }
            }
        }
        return toReturn;
    }

    //todo, will bitsSecond be needed when proper length fully implemented
    public static BetterBitSet concatenate(BetterBitSet first, BetterBitSet second, int bitsSecond) {
        if(bitsSecond == 0){
            return first;
            //basically, if you don't want to concatenate anything, don't
        }
        if(first.length()==0){
            return second;
        }
        final int lengthOfFirst = first.length();
        BetterBitSet toReturn = (BetterBitSet) second.clone();
        toReturn.setProperLength(first.length()+second.length());
        int highestSetIndexOfNext = first.nextSetBit(0);
        while (highestSetIndexOfNext != -1) {
            toReturn.set(highestSetIndexOfNext + bitsSecond);
            highestSetIndexOfNext = first.nextSetBit(highestSetIndexOfNext + 1);
        }

        return toReturn;
    }

    //todo test
    public static BetterBitSet concatenate(BetterBitSet first, int bitsFirst, BetterBitSet second, int bitsSecond) {
        if(bitsSecond == 0){
            return first;
        }
        if(bitsFirst==0){
            return second;
        }
        BetterBitSet toReturn = (BetterBitSet) second.clone();
        toReturn = toReturn.get(0, bitsSecond);
        toReturn.setProperLength(bitsSecond+bitsFirst);
        int highestSetIndexOfNext = first.nextSetBit(0);
        while (highestSetIndexOfNext != -1 && highestSetIndexOfNext<bitsFirst) {
            toReturn.set(highestSetIndexOfNext + bitsSecond);
            highestSetIndexOfNext = first.nextSetBit(highestSetIndexOfNext + 1);
        }
        return toReturn;
    }

    public int bitsetToInteger() {
        int toReturn = 0;
        int index = this.nextSetBit(0);

        while (index != -1) {
            toReturn += (int) Math.pow(2, index);
            index = this.nextSetBit(index + 1);
        }

        return toReturn;
    }

    public static int bitsetToInteger(BetterBitSet input) {
        int toReturn = 0;
        int index = input.nextSetBit(0);
        while (index != -1) {
            toReturn += (int) Math.pow(2, index);
            index = input.nextSetBit(index + 1);
        }
        return toReturn;
    }

    //todo test
    public static long bitsetToLong(BetterBitSet input){
        long toReturn = 0;
        int index = input.nextSetBit(0);
        while (index != -1) {
            toReturn |= (1L<<index);
            index = input.nextSetBit(index + 1);
        }
        return toReturn;
    }

    public long bitsetToLong(){
        long toReturn = 0;
        int index = this.nextSetBit(0);
        while (index != -1) {
            toReturn |= (1L<<index);
            index = this.nextSetBit(index + 1);
        }
        return toReturn;
    }

    public boolean get(int onlyIndex) {
        if ((onlyIndex <this.properLength) &&(onlyIndex>-1)){
            return super.get(onlyIndex);
        }else {
            throw new IndexOutOfBoundsException(
                    "Tried to get index out of length bounds, index was "+ onlyIndex
            );
        }
    }

    public BetterBitSet get(int fromIndex, int toIndex) {
//        System.out.println("Getting a range of bits from bitset");
//        System.out.println("toIndex -fromIndex = " + (toIndex-fromIndex));
//        System.out.println("fromIndex = " + fromIndex);
//        System.out.println("toIndex = " + toIndex);
        assert fromIndex<toIndex;
        assert fromIndex> -1;
        assert toIndex> -1;
        return bitSetToBetterBitSet(super.get(fromIndex, toIndex), toIndex-fromIndex);
    }

    public static BetterBitSet bitSetToBetterBitSet(BitSet in, int length) {
        BetterBitSet toReturn = new BetterBitSet(length);
        int setIndex = in.nextSetBit(0);
        while (setIndex != -1) {
            toReturn.set(setIndex);
            setIndex = in.nextSetBit(setIndex + 1);
        }
        return toReturn;
    }

    public static BetterBitSet intToBitset(int in){
        double len = floorDivDouble(Math.log10(in), Math.log10(2)) +1;
        BetterBitSet toReturn = new BetterBitSet((int) len + 1);
        for (int i = 0; i <=len; i++){
            if(((in>>>i)&1) == 1){
                toReturn.set(i);
            }
        }
        return toReturn;
    }
}
