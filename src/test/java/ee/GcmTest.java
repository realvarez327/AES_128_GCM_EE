package ee;

import org.junit.jupiter.api.Test;

import static ee.Utils.linearIntArrayToString;
import static ee.Utils.twoDimensionalToLinearArray;
import static org.junit.jupiter.api.Assertions.*;


public class GcmTest {
    @Test
    public void MSB(){
        //111011010
        BetterBitSet bbs = new BetterBitSet();
        bbs.set(1);
        bbs.set(3);
        bbs.set(4);
        bbs.set(6);
        bbs.set(7);
        bbs.set(8);
        BetterBitSet shouldBe = new BetterBitSet();
        shouldBe.set(1,4,true);
        System.out.println(shouldBe);
        BetterBitSet returned = ee.GCM.MSB(4, bbs);
        System.out.println(returned);
        shouldBe.xor(returned);
        System.out.println(shouldBe);
        assertTrue(shouldBe.isEmpty());
    }
}
