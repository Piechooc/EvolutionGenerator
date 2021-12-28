import agh.ics.oop.Animal;
import agh.ics.oop.BoundedMap;
import agh.ics.oop.Vector2d;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BoundedMapTest {

    @Test
    void testPlaceAnimals() {
        BoundedMap testMap = new BoundedMap(11, 11, 1,1,
                1,1, 3, false);
        testMap.placeAnimals();
        assertEquals(3, testMap.getAllAnimals().size());
        Vector2d position = new Vector2d(3, 4);
        testMap.place(new Animal(testMap, position,1), position);
        assertEquals(4, testMap.getAllAnimals().size());
    }
}
