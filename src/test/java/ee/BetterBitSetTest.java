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
        BetterBitSet tester = new BetterBitSet();
        tester.set(14);
        tester.set(13);
        tester.set(8);
        assertEquals(24832, tester.bitsetToInteger());

    }

    @Test
    public void asciiStringToBitset(){
        String test = " ";//00100000
        BetterBitSet bbs = new BetterBitSet();
        bbs.set(2);
        BetterBitSet returned = BetterBitSet.asciiStringToBitset(test);
        System.out.println(returned.bitsetToInteger());
        bbs.xor(returned);
        assertTrue((bbs.isEmpty()));

        bbs.clear();
        returned.clear();

        test = "a bc";//01100001001000000110001001100011
        //set's toIndex is index AFTER last set
        bbs.set(1,3,true);
        bbs.set(7);
        bbs.set(10);
        bbs.set(17,19,true);
        bbs.set(22);
        bbs.set(25,27, true);
        bbs.set(30,32,true);
        returned = BetterBitSet.asciiStringToBitset(test);
        bbs.xor(returned);
        assertTrue(bbs.isEmpty());

    }
}
