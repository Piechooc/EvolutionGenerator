package agh.ics.oop;

public class BoundedMap extends AbstractWorldMap implements IWorldMap, IPositionChangeObserver {

    public BoundedMap(int height, int width, float jungleRatio,
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
        Vector2d middle = new Vector2d(this.width / 2, this.height / 2);
        int jungleHeight = (int) (this.height * this.jungleRatio);
        int jungleWidth = (int) (this.width * this.jungleRatio);
        this.jungleUpperRight = new Vector2d(middle.x + (jungleWidth / 2), middle.y + (jungleHeight / 2));
        this.jungleLowerLeft = new Vector2d(middle.x - (jungleWidth / 2), middle.y - (jungleHeight / 2));
        this.allNumberOfAnimals = 0;
        this.allNumberOfPlants = 0;
        this.allAverageEnergy = 0;
        this.allAverageLifespan = 0;
        this.allAverageBabies = 0;
    }
}
