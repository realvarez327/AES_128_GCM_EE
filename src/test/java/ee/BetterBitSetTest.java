package ee;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BetterBitSetTest {
    @Test
    public void concatenate() {
        BetterBitSet first = new BetterBitSet(); //1101
        first.set(0);
        first.set(1);
        first.set(3);
        BetterBitSet second = new BetterBitSet(); //101
        second.set(5);
        second.set(7);
        BetterBitSet betterBitSet =  BetterBitSet.concatenate(first, second, 8);
        assertEquals(0b101000001011,betterBitSet.bitsetToInteger());

    }

    @Test
    public void bitsetToInteger(){
        BetterBitSet tester = BetterBitSet.asciiStringToBitset("w");
        int W = 'w';
        assertEquals(W, BetterBitSet.bitsetToInteger(tester));

        int a = 1;
        tester.clear();
        tester.set(0);
        assertEquals(a, tester.bitsetToInteger());

    }

    @Test
    public void asciiStringToBitset(){
        String test = " ";//00100000
        BetterBitSet returned = BetterBitSet.asciiStringToBitset(test);
        assertEquals(test, returned.bitSetToAsciiString());

        returned.clear();

        test = "a";
        returned = BetterBitSet.asciiStringToBitset(test);
        assertEquals(test, returned.bitSetToAsciiString());

        returned.clear();

        test = "ab";
        returned = BetterBitSet.asciiStringToBitset(test);
        assertEquals(test, returned.bitSetToAsciiString());

        returned.clear();

        test = "b ";
        returned = BetterBitSet.asciiStringToBitset(test);
        assertEquals(test, returned.bitSetToAsciiString());

        returned.clear();

        test = "ab x";
        returned = BetterBitSet.asciiStringToBitset(test);
        assertEquals(test, returned.bitSetToAsciiString());
    }

    @Test
    public void bitSetToAsciiString(){
        String tester = "a";
        BetterBitSet bbs = BetterBitSet.asciiStringToBitset(tester);
        String returned = bbs.bitSetToAsciiString();
        assertEquals(tester, returned);

        tester = "hello";
        bbs.clear();
        bbs = BetterBitSet.asciiStringToBitset(tester);
        returned = bbs.bitSetToAsciiString();
        assertEquals(tester, returned);


    }
}
