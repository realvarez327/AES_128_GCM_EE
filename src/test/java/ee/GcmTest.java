package ee;

import org.junit.jupiter.api.Test;

import static ee.GCM.prepLength64Variables;
import static ee.Utils.*;
import static org.junit.jupiter.api.Assertions.*;


public class GcmTest {

    @Test
    public void MSB(){
        //111011010
        BetterBitSet bbs = new BetterBitSet(9);
        bbs.set(6);
        bbs.set(5);
        bbs.set(3);
        bbs.set(0);
        BetterBitSet msb2 = GCM.MSB(2, bbs);
        BetterBitSet testAgainst = new BetterBitSet(2);
        testAgainst.set(0);
        testAgainst.set(1);
        System.out.println(bitsetToBinaryString(msb2)+" should be "+bitsetToBinaryString(testAgainst));
        System.out.println("testAgainst = " + testAgainst);
        System.out.println("msb2 = " + msb2);
        msb2.xor(testAgainst);
        assertTrue(msb2.isEmpty());
    }

    @Test
    public void inc32(){
        BetterBitSet tester = new BetterBitSet(64);
        //base case, from 0 to 1
        tester = GCM.inc32(tester);
        assertEquals(1, tester.bitsetToInteger());

        tester = GCM.inc32(tester);
        assertEquals(2, tester.bitsetToInteger());
        tester.clear();

        tester.set(0,4,true);
        tester = GCM.inc32(tester);
        assertEquals(16, tester.bitsetToInteger());

        tester.set(0,32);
        tester = GCM.inc32(tester);
        assertEquals(0, tester.bitsetToInteger());
    }

    @Test
    public void prepLength64VariablesTest(){
        BetterBitSet test = new BetterBitSet(16);
        BetterBitSet shouldBe = new BetterBitSet(64);
        shouldBe.set(4);
        BetterBitSet res = prepLength64Variables(test);
        System.out.println(res.length());
        assertEquals(64, res.length());
        assertEquals(shouldBe, res);
    }
}
