package ee;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static ee.Utils.twoDimensionalIntArrayToBitset;
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
        BetterBitSet checker = new BetterBitSet(48);
        checker.set(0);
        checker.set(17);
        checker.set(32,34,true);
        assertEquals(checker, Utils.intLinearArrayToBitset(tester));
    }

    @Test
    public void divCeil(){
        int a = 774;
        int b = 128;
        int res = Utils.divCeil(a,b);
        assertEquals(7, res);
    }

    @Test
    public void twoDimensionalIntArrayToBitsetTest(){
        int[][] testerInput = new int[][]{
                {1,2,3,4},
                {5,6,7,8},
                {9,10,11,12},
                {13,14,15,16}
        };
        BetterBitSet returnedOfTested = twoDimensionalIntArrayToBitset(testerInput);
        int[][] testAgainst = Utils.bitsetToTwoDimensionalIntArray(returnedOfTested);
        assertTrue(Arrays.deepEquals(testAgainst, testAgainst));
    }
}
