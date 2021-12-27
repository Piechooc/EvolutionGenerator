import agh.ics.oop.UnboundedMap;
import agh.ics.oop.Vector2d;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UnboundedMapTest {

    @Test
    void testBetterModulo() {
        UnboundedMap testMap = new UnboundedMap(11, 11, 1,1,
                1,1, 1, false);
        assertEquals(10, testMap.betterModulo(-1, 11));
        assertEquals(0, testMap.betterModulo(11, 11));
    }

    @Test
    void testChangePositionToUnbounded() {
        UnboundedMap testMap = new UnboundedMap(11, 11, 1,1,
                1,1, 1, false);
        assertEquals(new Vector2d(10, 8), testMap.changePositionToUnbounded(new Vector2d(-1, 8)));
        assertEquals(new Vector2d(7, 9), testMap.changePositionToUnbounded(new Vector2d(7, -2)));
        assertEquals(new Vector2d(0, 5), testMap.changePositionToUnbounded(new Vector2d(11, 5)));
        assertEquals(new Vector2d(4, 0), testMap.changePositionToUnbounded(new Vector2d(4, 11)));
        assertEquals(new Vector2d(10, 10), testMap.changePositionToUnbounded(new Vector2d(-1, -1)));
        assertEquals(new Vector2d(0, 10), testMap.changePositionToUnbounded(new Vector2d(11, -1)));
        assertEquals(new Vector2d(0, 0), testMap.changePositionToUnbounded(new Vector2d(11, 11)));
        assertEquals(new Vector2d(10, 0), testMap.changePositionToUnbounded(new Vector2d(-1, 11)));
        assertEquals(new Vector2d(4, 8), testMap.changePositionToUnbounded(new Vector2d(4, 8)));
    }
}
