import agh.ics.oop.Animal;
import agh.ics.oop.Genotype;
import agh.ics.oop.UnboundedMap;
import agh.ics.oop.Vector2d;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AbstractWorldMapTest {

    @Test
    void testFindIndexesOfStrongestAnimals() {
        UnboundedMap testMap = new UnboundedMap(11, 11, 1,1,
                1,1, 1, false);
        Vector2d position = new Vector2d(3, 3);
        Animal animal1 = new Animal(testMap, position, 100);
        Animal animal2 = new Animal(testMap, position, 50);
        Animal animal3 = new Animal(testMap, position, 150);
        Animal animal4 = new Animal(testMap, position, 100);
        testMap.place(animal1, position);
        testMap.place(animal2, position);
        testMap.place(animal3, position);
        testMap.place(animal4, position);
        ArrayList<Animal> animals = testMap.getAllAnimals();
        ArrayList<Integer> indexes1 = new ArrayList<>();
        indexes1.add(2);
        ArrayList<Integer> indexes2 = new ArrayList<>();
        indexes2.add(0);
        indexes2.add(3);
        assertEquals(indexes1, testMap.findIndexesOfStrongestAnimals(animals, -1));
        assertEquals(indexes2, testMap.findIndexesOfStrongestAnimals(animals, 150));
    }

    @Test
    void testUpdateDominantGenotype() {
        UnboundedMap testMap = new UnboundedMap(11, 11, 1,1,
                1,1, 1, false);
        Vector2d position = new Vector2d(3, 3);
        Animal animal1 = new Animal(testMap, position, 100);
        Animal animal2 = new Animal(testMap, position, 50);
        animal2.setGenotype(animal1.getAnimalGenotype());
        Animal animal3 = new Animal(testMap, position, 150);
        animal3.setGenotype(animal1.getAnimalGenotype());
        Animal animal4 = new Animal(testMap, position, 100);
        testMap.place(animal1, position);
        testMap.place(animal2, position);
        testMap.place(animal3, position);
        testMap.place(animal4, position);
        testMap.updateDominantGenotype();
        Genotype testGenotype = testMap.getDominantGenotype();
        int testNumberOfAnimalsWithDominantGenotype = testMap.getNumberOfAnimalsWithDominantGenotype();
        assertEquals(testGenotype, animal1.getAnimalGenotype());
        assertEquals(testNumberOfAnimalsWithDominantGenotype, 3);
    }
}
