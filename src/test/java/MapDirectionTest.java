import agh.ics.oop.MapDirection;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MapDirectionTest {

    @Test
    void testRotate() {
        assertEquals(MapDirection.SOUTH, MapDirection.NORTH.rotate(4));
        assertEquals(MapDirection.SOUTH_EAST, MapDirection.SOUTH.rotate(7));
        assertEquals(MapDirection.WEST, MapDirection.SOUTH_WEST.rotate(1));
    }
}
