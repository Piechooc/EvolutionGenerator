package agh.ics.oop;

public class UnboundedMap extends AbstractWorldMap implements IWorldMap, IPositionChangeObserver {

    public UnboundedMap(int height, int width, float jungleRatio,
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

    public int betterModulo(int number, int modulo) {
        int result = number % modulo;
        if (result < 0) {
            return result + modulo;
        }
        return result;
    }

    public Vector2d changePositionToUnbounded(Vector2d position) {
        if (position != null){
            return new Vector2d(betterModulo(position.x, this.width), betterModulo(position.y, this.height));
        }
        return null;
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        return true;
    }

    @Override
    public void addAnimal(Animal animal, Vector2d position) {
        Vector2d unboundedPosition = changePositionToUnbounded(position);
        super.addAnimal(animal, unboundedPosition);
    }

    @Override
    public void removeAnimal(Animal animal, Vector2d position) {
        Vector2d unboundedPosition = changePositionToUnbounded(position);
        super.removeAnimal(animal, unboundedPosition);
    }

    @Override
    public boolean place(AbstractWorldMapElement element, Vector2d position){
        Vector2d unboundedPosition = changePositionToUnbounded(position);
        return super.place(element, unboundedPosition);
    }

    @Override
    public boolean isOccupied(Vector2d position) {
        Vector2d unboundedPosition = changePositionToUnbounded(position);
        return super.isOccupied(unboundedPosition);
    }

    @Override
    public AbstractWorldMapElement objectAt(Vector2d position) {
        Vector2d unboundedPosition = changePositionToUnbounded(position);
        return super.objectAt(unboundedPosition);
    }

    @Override
    public void positionChanged(Vector2d oldPosition, Vector2d newPosition, Animal animal) {
        Vector2d oldUnboundedPosition = changePositionToUnbounded(oldPosition);
        Vector2d newUnboundedPosition = changePositionToUnbounded(newPosition);
        super.positionChanged(oldUnboundedPosition, newUnboundedPosition, animal);
    }
}
