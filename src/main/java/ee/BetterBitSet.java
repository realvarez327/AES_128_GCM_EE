package ee;

import java.util.BitSet;

public class BetterBitSet extends BitSet {
    int properLength;
    BetterBitSet(int lengthGiven) {
        super(lengthGiven);
        this.properLength = lengthGiven;
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
            throw new RuntimeException("You tried to access an index out of length :(");
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
        super.set(bitIndex, value);
    }

    public static String bitSetToAsciiString(BetterBitSet input) {
        int index = 0;
        StringBuilder toReturn = new StringBuilder();
        int intform;
        while ((15 + index) <= input.length() - 1) {
            intform = input.get(index, index + 16).bitsetToInteger();
            toReturn.append((char) intform);
            index+= 16;
        }

        if (index < input.length() - 1) {
            intform = input.get(index, input.length()).bitsetToInteger();
            toReturn.append((char) intform);
        }

        return toReturn.toString();
    }

    public String bitSetToAsciiString() {
        int index = 0;
        StringBuilder toReturn = new StringBuilder();
        int intform = 0;
        while ((15 + index) <= this.length() - 1) {
            intform = this.get(index, index + 16).bitsetToInteger();
            toReturn.append((char) intform);
            index+= 16;
        }

        if (index < this.length() - 1) {
            intform = this.get(index, this.length()).bitsetToInteger();
            toReturn.append((char) intform);
        }

        return toReturn.toString();
    }

    public static BetterBitSet asciiStringToBitset(String input) {//todo double check all usages are correct
        //make it work, then make it good
        BetterBitSet toReturn = new BetterBitSet(input.length()*16);
        StringBuilder inputAsBitString = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            char current = input.charAt(i);
            StringBuilder toAddOn = new StringBuilder(Integer.toBinaryString(current)).reverse();
            inputAsBitString.append(toAddOn);
            if (current < 0x80) {
                inputAsBitString.append("0");
                if (current < 0x40) {
                    inputAsBitString.append("0");
                }
            }
            inputAsBitString.append("0".repeat(inputAsBitString.length() % 16));
        }
        int index = inputAsBitString.indexOf("1");
        while (index != -1) {
            toReturn.set(index);
            index = inputAsBitString.indexOf("1", index + 1);
        }
        return toReturn;
    }

    //todo, will bitsSecond be needed when proper length fully implemented
    public static BetterBitSet concatenate(BitSet first, BitSet second, int bitsSecond) {

        final int lengthOfFirst = first.length();
        //dont want to cut off characters, pad to 16
        //final int lengthOfSecond = second.length()+(second.length()%16);


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
    public static BetterBitSet concatenate(BitSet first, int bitsFirst, BitSet second, int bitsSecond) {
        BetterBitSet toReturn = (BetterBitSet) second.clone();
        toReturn.setProperLength(second.length()+first.length());
        int highestSetIndexOfNext = first.nextSetBit(0);
        while (highestSetIndexOfNext != -1) {
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
            toReturn += (int) Math.pow(2, input.length() - 1 - index);
            index = input.nextSetBit(index + 1);
        }
        return toReturn;
    }

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
}
