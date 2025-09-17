package ee;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static ee.Utils.binaryStringToBetterBitSet;
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
        //res should be 00000001 00000010 00000011
        BetterBitSet res = Utils.intLinearArrayToBitset(tester);
        System.out.println(res);
        BetterBitSet checker = new BetterBitSet(24);
        checker.set(0);
        checker.set(1);
        checker.set(9);
        checker.set(16);

        assertEquals(checker, Utils.intLinearArrayToBitset(tester));
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

    @Test
    public void binaryStringToBetterBitSetTest(){
        int testInt = 5;
        BetterBitSet shouldBe = new BetterBitSet(20);
        shouldBe.set(0);
        shouldBe.set(2);
        assertEquals(shouldBe, binaryStringToBetterBitSet(Integer.toBinaryString(testInt)));

        shouldBe.clear();

        testInt = 32;
        shouldBe.set(5);
        assertEquals(shouldBe, binaryStringToBetterBitSet(Integer.toBinaryString(testInt)));

        shouldBe.clear();

        testInt = 0xf0f0;
        shouldBe.set(4);
        shouldBe.set(5);
        shouldBe.set(6);
        shouldBe.set(7);
        shouldBe.set(12);
        shouldBe.set(13);
        shouldBe.set(14);
        shouldBe.set(15);
        assertEquals(shouldBe, binaryStringToBetterBitSet(Integer.toBinaryString(testInt)));
    }

    @Test
    public void intToBitset(){
        int tester = 5;
        BetterBitSet shouldBe = new BetterBitSet(8);
        shouldBe.set(0);
        shouldBe.set(2);
        assertEquals(shouldBe, BetterBitSet.intToBitset(tester));
    }

    @Test
    public void multiplicationBlock(){
        //1*1
        BetterBitSet X = new BetterBitSet(128);
        X.set(0);
        BetterBitSet Y = new BetterBitSet(128);
        Y.set(0);
        Y.set(1);
        assertEquals(Utils.multiplicationBlock(X,Y), Y);

        X.clear();
        Y.clear();

        //commutative property
        X.set(1);
        assertEquals(Utils.multiplicationBlock(X,Y), Utils.multiplicationBlock(Y,X));

        X.clear();
        Y.clear();
        //distributive property
        BetterBitSet a = new BetterBitSet(128);
        a.set(0);
        Y.set(0);
        Y.set(1);
        X.set(1);
        // a =1, X =2, Y = 3
        //resOne = a*X + a*Y, resTwo = a*(X+Y)
        BetterBitSet resOne = Utils.multiplicationBlock(a,X);
        resOne.xor(Utils.multiplicationBlock(a,Y));

        X.xor(Y);
        BetterBitSet resTwo = Utils.multiplicationBlock(a,X);
        X.xor(Y);
        assertEquals(resOne,resTwo);

        X.clear();
        Y.clear();
        //squaring
        X.set(1);
        Y.set(1);
        BetterBitSet shouldBe = new BetterBitSet(128);
        shouldBe.set(2);
        assertEquals(shouldBe, Utils.multiplicationBlock(X,Y));
    }
}
