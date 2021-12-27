package agh.ics.oop;

public class BoundedMap extends AbstractWorldMap implements IWorldMap, IPositionChangeObserver {

    public BoundedMap(int height, int width, int jungleRatio,
                      int startEnergy, int moveEnergy, int plantEnergy, int numberOfAnimalToPlace, boolean magic) {
        this.height = height;
        this.width = width;
        this.jungleRatio = jungleRatio;
        this.startEnergy = startEnergy;
        this.moveEnergy = moveEnergy;
        this.plantEnergy = plantEnergy;
        this.upperRight = new Vector2d(width - 1, height - 1);
        this.lowerLeft = new Vector2d(0, 0);
        this.running = true;
        this.numberOfAnimals = numberOfAnimalToPlace;
        this.numberOfAnimalToPlace = numberOfAnimalToPlace;
        this.era = 0;
        this.numberOfPlants = 0;
        this.averageEnergy = 0;
        this.numberOfDeadAnimals = 0;
        this.averageLifespan = 0;
        this.averageNumberOfBabies = 0;
        this.numberOfAnimalsWithDominantGenotype = 0;
        this.magic = magic;
        this.magicLeft = 3;
    }
}
