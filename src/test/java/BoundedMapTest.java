import agh.ics.oop.BoundedMap;
import agh.ics.oop.UnboundedMap;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BoundedMapTest {

    @Test
    void testPlaceAnimals() {
        BoundedMap testMap = new BoundedMap(11, 11, 1,1,
                1,1, 3, false);
        testMap.placeAnimals();
        assertEquals(3, testMap.getAllAnimals().size());
    }
}
