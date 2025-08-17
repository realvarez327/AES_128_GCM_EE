package ee;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UtilsTest {
    @Test
    public void bitsetToTwoDimensionalIntArrayTest() {
        BetterBitSet tester = BetterBitSet.asciiStringToBitset("wazzocks");
        int[][] shouldBe = new int[][]{
                {119, 111, 0, 0},
                {97, 99, 0, 0},
                {122, 107, 0, 0},
                {122, 115, 0, 0}
        };
        assertArrayEquals(shouldBe, Utils.bitsetToTwoDimensionalIntArray(tester));

    }

    @Test
    public void intLinearArrayToBitset(){
        int[] tester =  {1,2,3};
        System.out.println(Utils.intLinearArrayToBitset(tester));
        BetterBitSet checker = new BetterBitSet();
        checker.set(0);
        checker.set(17);
        checker.set(32,34,true);
        assertEquals(checker, Utils.intLinearArrayToBitset(tester));
    }
}
