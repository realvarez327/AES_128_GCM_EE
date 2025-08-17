package ee;

import java.util.BitSet;

public class BetterBitSet extends BitSet {

    BetterBitSet() {
        super();
    }

    public static String bitSetToAsciiString(BetterBitSet input) {
        int index = 0;
        StringBuilder toReturn = new StringBuilder();
        int intform = 0;
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
        BetterBitSet toReturn = new BetterBitSet();
        StringBuilder inputAsBitString = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            char current = input.charAt(i);
            StringBuilder toAddOn = new StringBuilder(Integer.toBinaryString(current));
            toAddOn = toAddOn.reverse();
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

    public static BetterBitSet concatenate(BitSet first, BitSet second, int bitsSecond) {
        final int lengthOfFirst = first.length();
        final int lengthOfSecond = second.length();
        if (lengthOfSecond > bitsSecond) {
            throw new RuntimeException("second bitset is larger than expected");
        }

        final BetterBitSet toReturn = (BetterBitSet) first.clone();
        int highestSetIndexOfSecond = second.nextSetBit(0);
        while ((highestSetIndexOfSecond < lengthOfSecond) && highestSetIndexOfSecond != -1) {
            toReturn.set(highestSetIndexOfSecond + lengthOfFirst);
            highestSetIndexOfSecond = second.nextSetBit(highestSetIndexOfSecond + 1);
        }
        return toReturn;
    }

    //todo test
    public static BetterBitSet concatenate(BitSet first, int bitsFirst, BitSet second, int bitsSecond) {
        final BetterBitSet toReturn = (BetterBitSet) first.clone();
        int highestSetIndexOfSecond = second.nextSetBit(0);
        while ((highestSetIndexOfSecond < bitsSecond) && highestSetIndexOfSecond != -1) {
            toReturn.set(highestSetIndexOfSecond + bitsFirst);
            highestSetIndexOfSecond = second.nextSetBit(highestSetIndexOfSecond + 1);
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

    public boolean get(int onlyIndex) {
        return super.get(onlyIndex);
    }

    public BetterBitSet get(int fromIndex, int toIndex) {
        return bitSetToBetterBitSet(super.get(fromIndex, toIndex));
    }

    public static BetterBitSet bitSetToBetterBitSet(BitSet in) {
        BetterBitSet toReturn = new BetterBitSet();
        int setIndex = in.nextSetBit(0);
        while (setIndex != -1) {
            toReturn.set(setIndex);
            setIndex = in.nextSetBit(setIndex + 1);
        }
        return toReturn;
    }
}
