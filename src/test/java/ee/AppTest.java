package ee;

import org.junit.jupiter.api.Test;

import static ee.Utils.linearIntArrayToString;
import static ee.Utils.twoDimensionalToLinearArray;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit test for simple App.
 */
public class AppTest {

    @Test
    public void testXTimes() {
        assertEquals(0x47, Utils.xTimes(0xae));
        assertEquals(0x8e, Utils.xTimes(0x47));
        assertEquals(0x07, Utils.xTimes(0x8e));
        assertEquals(0x0e, Utils.xTimes(0x07));

    }

    @Test
    public void testMultiply() {
        assertEquals(0, Utils.multiplicationB(0, 19));
        assertEquals(87, Utils.multiplicationB(87, 1));
        assertEquals(87, Utils.multiplicationB(1, 87));
        assertEquals(174, Utils.multiplicationB(87, 2));
        assertEquals(0xfe, Utils.multiplicationB(0x57, 0x13));
    }

    @Test
    public void testFindHighestPowerOf2() {
        assertEquals(4, Utils.findHighestPowerOf2(16));
        assertEquals(3, Utils.findHighestPowerOf2(15));
        assertEquals(4, Utils.findHighestPowerOf2(17));
        assertEquals(0, Utils.findHighestPowerOf2(1));
        assertEquals(-1, Utils.findHighestPowerOf2(0));
    }

    @Test
    public void testLinearIntArrayToString() {
        assertEquals("a", linearIntArrayToString(new int[]{97}));
        assertEquals("aaaaaaa", linearIntArrayToString(new int[]{97, 97, 97, 97, 97, 97, 97}));
        assertEquals("hello there", linearIntArrayToString(
                new int[]{
                        104, 101, 108, 108, 111, 32, 116, 104, 101, 114, 101
                }));
    }

    @Test
    public void testTwoDimensionalToLinearArray() {
        assertArrayEquals(new int[]{1, 3, 2, 4}, twoDimensionalToLinearArray(new int[][]{
                {1, 2},
                {3, 4}
        }));
        assertArrayEquals(new int[]{14}, twoDimensionalToLinearArray(new int[][]{
                {14}
        }));

        assertArrayEquals(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16}, twoDimensionalToLinearArray(new int[][]{
                {1, 5, 9, 13},
                {2, 6, 10, 14},
                {3, 7, 11, 15},
                {4, 8, 12, 16}
        }));
    }

}
