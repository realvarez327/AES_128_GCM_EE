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
        BetterBitSet bbs = new BetterBitSet();
        bbs.set(5);
        BetterBitSet returned = BetterBitSet.asciiStringToBitset(test);
        bbs.xor(returned);
        System.out.println(bbs);
        assertTrue((bbs.isEmpty()));

        bbs.clear();
        returned.clear();

        test = "w";
        returned = BetterBitSet.asciiStringToBitset(test);
        assertEquals(test,returned.bitSetToAsciiString());

        bbs.clear();
        returned.clear();

        test = "a bc";
        //set's toIndex is index AFTER last set
        returned = BetterBitSet.asciiStringToBitset(test);
        assertEquals(test, returned.bitSetToAsciiString());




    }

    @Test
    public void bitSetToAsciiString(){

    }
}
