package ee;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BetterBitSetTest {
    @Test
    public void concatenate() {
        BetterBitSet first = new BetterBitSet(4); //1101
        first.set(0);
        first.set(1);
        first.set(3);
        BetterBitSet second = new BetterBitSet(4); //0001
        second.set(0);
        BetterBitSet betterBitSet =  BetterBitSet.concatenate(first, second, 4);
        assertEquals(0b10110001, betterBitSet.bitsetToInteger());
    }

    @Test
    public void bitsetToInteger(){
        BetterBitSet tester = BetterBitSet.asciiStringToBitset("w");
        int W = 'w'; //119
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
        System.out.println(returned);
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

    //non static
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


    @Test
    public void deepOrShallowClone(){
        BetterBitSet a = new BetterBitSet(128);
        a.set(0);
        BetterBitSet b = (BetterBitSet) a.clone();
        b.clear(0);
        assertTrue(a.get(0));//if true, deep clone. else, shallow clone
    }

    @Test
    public void longToBitset(){
        long test = 0L;
        BetterBitSet shouldBe = new BetterBitSet(8);
        assertEquals(shouldBe, BetterBitSet.longToBitset(test,8));

        test = 1L;
        shouldBe.set(0);
        assertEquals(shouldBe, BetterBitSet.longToBitset(test,8));

        shouldBe.set(2);
        test = 5L;
        assertEquals(shouldBe, BetterBitSet.longToBitset(test,8));

        shouldBe.clear();
        shouldBe.setProperLength(32);
        test = 0xFFFFFFFFL;
        shouldBe.set(0, 32);
        assertEquals(shouldBe, BetterBitSet.longToBitset(test,32));

        shouldBe.clear();
        shouldBe.setProperLength(64);
        test = 0x100000000L;
        shouldBe.set(32);
        assertEquals(shouldBe, BetterBitSet.longToBitset(test,64));

        shouldBe.clear();
        test = Long.MAX_VALUE;
        shouldBe.set(0,63,true);
        assertEquals(shouldBe,BetterBitSet.longToBitset(test,64));

    }

    @Test
    public void bitsetToLongTest(){
        BetterBitSet test = new BetterBitSet(8);
        long shouldBe = 0L;
        assertEquals(shouldBe, test.bitsetToLong());

        shouldBe = 1L;
        test.set(0);
        assertEquals(shouldBe, test.bitsetToLong());

        shouldBe = 5L;
        test.set(2);
        assertEquals(shouldBe, test.bitsetToLong());

        test.clear();
        test.setProperLength(32);
        shouldBe = 0xFFFFFFFFL;
        test.set(0, 32);
        assertEquals(shouldBe, test.bitsetToLong());

        test.clear();
        test.setProperLength(64);
        shouldBe = 0x100000000L;
        test.set(32);
        assertEquals(shouldBe, test.bitsetToLong());

        test.clear();
        shouldBe = Long.MAX_VALUE;
        test.set(0,63,true);
        assertEquals(shouldBe, test.bitsetToLong());
    }

    @Test
    public void integerToBitsetTest(){
        int in = 0;
        BetterBitSet shouldBe = new BetterBitSet(1);
        assertEquals(shouldBe, BetterBitSet.intToBitset(in));

        in = 1;
        shouldBe.set(0);
        assertEquals(shouldBe, BetterBitSet.intToBitset(in));

        in = 2;
        shouldBe.clear();
        shouldBe.setProperLength(2);
        shouldBe.set(1);
        assertEquals(shouldBe, BetterBitSet.intToBitset(in));

        in = 7;
        shouldBe.setProperLength(3);
        shouldBe.clear();
        shouldBe.set(0, 3);
        assertEquals(shouldBe, BetterBitSet.intToBitset(in));

        in = 1024;
        shouldBe.clear();
        shouldBe.setProperLength(11);
        shouldBe.set(10);
        assertEquals(shouldBe, BetterBitSet.intToBitset(in));

        in = Integer.MAX_VALUE;
        shouldBe.clear();
        shouldBe.setProperLength(31);
        shouldBe.set(0, 31);
        assertEquals(shouldBe, BetterBitSet.intToBitset(in));

    }
}
