package ee;

import static ee.App.findHighestPowerOf2;
import static ee.App.xTimes;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Unit test for simple App.
 */
public class AppTest {

  @Test
  public void testXTimes(){
      assertEquals(0x47, xTimes(0xae));
      assertEquals(0x8e, xTimes(0x47));
      assertEquals(0x07, xTimes(0x8e));
      assertEquals(0x0e, xTimes(0x07));

  }
    @Test
    public void testMultiply() {
        assertEquals(0, App.multiplicationB(0, 19));
        assertEquals(87, App.multiplicationB(87,1));
        assertEquals(87, App.multiplicationB(1,87));
        assertEquals(174, App.multiplicationB(87,2));
        assertEquals( 0xfe, App.multiplicationB(0x57,0x13));
    }

    @Test
    public void testFindHighestPowerOf2(){
        assertEquals(4, findHighestPowerOf2(16));
        assertEquals(3, findHighestPowerOf2(15));
        assertEquals(4, findHighestPowerOf2(17));
        assertEquals(0, findHighestPowerOf2(1));
        assertEquals(-1, findHighestPowerOf2(0));
    }
}
