package agh.ics.oop;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public abstract class AbstractWorldMap implements IWorldMap, IPositionChangeObserver {

    //map
    protected int height;
    protected int width;
    protected Vector2d lowerLeft = new Vector2d(Integer.MAX_VALUE, Integer.MAX_VALUE);
    protected Vector2d upperRight = new Vector2d(Integer.MIN_VALUE, Integer.MIN_VALUE);
    protected float jungleRatio;
    protected boolean running;
    protected int numberOfAnimalToPlace;
    protected boolean magic;
    protected int magicLeft;

    //animal
    protected int startEnergy;
    protected int moveEnergy;
    protected int plantEnergy;

    //elements
    protected Map<Vector2d, Grass> wholeGrass = new LinkedHashMap<>();
    protected Map<Vector2d, ArrayList<Animal>> animals = new LinkedHashMap<>();
    protected ArrayList<Animal> animalList = new ArrayList<>();
    protected ArrayList<Grass> grassList = new ArrayList<>();

    //statistics
    protected int era;
    protected int numberOfAnimals;
    protected int numberOfPlants;
    protected int averageEnergy;
    protected int numberOfDeadAnimals;
    protected int averageLifespan;
    protected int averageNumberOfBabies;
    protected Map<Genotype, Integer> allGenotype = new LinkedHashMap<>();
    protected Genotype dominantGenotype;
    protected int numberOfAnimalsWithDominantGenotype;

    @Override
    public boolean canMoveTo(Vector2d position) {
        return (position.precedes(this.upperRight) && position.follows(this.lowerLeft));
    }

    public void addAnimal(Animal animal, Vector2d position) {
        ArrayList<Animal> list = this.animals.get(position);
        if (list == null) {
            ArrayList<Animal> temp = new ArrayList<>();
            temp.add(animal);
            this.animals.put(position, temp);
        } else {
            list.add(animal);
        }
    }

    public void removeAnimal(Animal animal, Vector2d position) {
        ArrayList<Animal> list = this.animals.get(position);
        if (list != null && list.size() > 0) {
            list.remove(animal);
            if (list.size() == 0) {
                this.animals.remove(position);
            }
        }
    }

    @Override
    public boolean place(AbstractWorldMapElement element){
        if (canMoveTo(element.getPosition())) {
            if (element instanceof Animal) {
                addAnimal((Animal) element, element.getPosition());
                this.animalList.add((Animal) element);
                ((Animal) element).addObserver(this);
            } else {
                this.wholeGrass.computeIfAbsent(element.getPosition(), k -> (Grass) element);
                this.grassList.add((Grass) element);
            }
        }
        return false;
    }

    @Override
    public boolean isOccupied(Vector2d position) {
        return objectAt(position) != null;
    }

    @Override
    public AbstractWorldMapElement objectAt(Vector2d position) {
        ArrayList<Animal> list = this.animals.get(position);
        if (list == null || list.size() == 0) {
            return this.wholeGrass.get(position);
        } else {
            return list.get(0);
        }
    }

    public ArrayList<Integer> findIndexesOfStrongestAnimals(ArrayList<Animal> list, int notWantedEnergy) {
        ArrayList<Integer> indexes = new ArrayList<>();
        int maxEnergy = -1;
        for (Animal animal : list) {
            if (animal.getEnergy() > maxEnergy && animal.getEnergy() != notWantedEnergy) {
                maxEnergy = animal.getEnergy();
            }
        }

        for (int index = 0; index < list.size(); index++) {
            if (list.get(index).getEnergy() == maxEnergy) {
                indexes.add(index);
            }
        }

        return indexes;
    }

    public void eat() {
        ArrayList<Grass> eatenGrass = new ArrayList<>();
        for (Grass grass : this.wholeGrass.values()) {
            ArrayList<Animal> list = animals.get(grass.getPosition());
            if (list != null && list.size() > 0) {
                ArrayList<Integer> indexesOfStrongestAnimals = findIndexesOfStrongestAnimals(list, -1);
                for (int index : indexesOfStrongestAnimals) {
                    Animal animal = list.get(index);
                    animal.changeEnergy(this.plantEnergy / list.size());
                    eatenGrass.add(grass);
                }
            }
        }

        for (Grass grass : eatenGrass) {
            this.wholeGrass.remove(grass.getPosition());
            this.grassList.remove(grass);
        }
    }

    public int findMoveForAnimal(Animal animal) {
        int[] genes = animal.getAnimalGenotype().getGenotype();
        return genes[(int) (Math.random() * 32)];
    }

    public void moveAllAnimals() {
        ArrayList<Animal> list = getAllAnimals();
        for (int i = 0; i < list.size(); i++) {
            Animal animal = this.animalList.get(i);
            animal.move(findMoveForAnimal(animal));
        }
    }

    public void makeBaby(Animal firstParent, Animal secondParent) {
        if (firstParent.getEnergy() >= this.startEnergy / 2 && secondParent.getEnergy() >= this.startEnergy / 2) {
            Animal baby = firstParent.makeBaby(secondParent);
            place(baby);
            updateAverageNumberOfBabiesWhenBabyMade();
        }
    }

    public void findParents() {
        for (ArrayList<Animal> list : this.animals.values()) {
            if (list != null && list.size() > 1) {
                if (list.size() == 2) {
                    makeBaby(list.get(0), list.get(1));
                } else {
                    ArrayList<Integer> indexesOfStrongestAnimals = findIndexesOfStrongestAnimals(list, -1);
                    int firstIndex = indexesOfStrongestAnimals.get(0);
                    if (indexesOfStrongestAnimals.size() > 1) {
                        int secondIndex = indexesOfStrongestAnimals.get(1);
                        makeBaby(list.get(firstIndex), list.get(secondIndex));
                    } else {
                        ArrayList<Integer> indexesOfSecondParents = findIndexesOfStrongestAnimals(list,
                                list.get(firstIndex).getEnergy());
                        int secondIndex = indexesOfSecondParents.get(0);
                        makeBaby(list.get(firstIndex), list.get(secondIndex));
                        }
                    }
                }
            }
    }

    public void nextEra() {
        for (ArrayList<Animal> list : this.animals.values()) {
            if (list != null && list.size() > 0) {
                for (Animal animal : list) {
                    animal.changeEnergy(-1 * this.moveEnergy);
                }
            }
        }

        this.era++;
    }

    public void removeTheDead() {
        ArrayList<Animal> list = getAllAnimals();
        for (Animal animal : list) {
            if (animal.isDead()) {
                removeAnimal(animal, animal.getPosition());
                animal.removeObserver(this);
                this.animalList.remove(animal);
                updateAverageLifespan(this.era - animal.getEraOfBirth());
                updateAverageNumberOfBabiesWhenAnimalDied(animal);
            }
        }
    }

    public void plantGrass() {
        Vector2d middle = new Vector2d(this.width / 2, this.height / 2);
        int jungleHeight = (int) (this.height * this.jungleRatio);
        int jungleWidth = (int) (this.width * this.jungleRatio);
        Vector2d jungleUpperRight = new Vector2d(middle.x + jungleWidth / 2, middle.y + jungleHeight / 2);
        Vector2d jungleLowerLeft = new Vector2d(middle.x - jungleWidth / 2, middle.y - jungleHeight / 2);

        ArrayList<Vector2d> emptyPlacesInJungle = new ArrayList<>();
        ArrayList<Vector2d> emptyPlacesOnSteppe = new ArrayList<>();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Vector2d tempPosition = new Vector2d(x, y);
                if (objectAt(tempPosition) == null) {
                    if (jungleLowerLeft.x <= x && x <= jungleUpperRight.x
                            && jungleLowerLeft.y <= y && y <= jungleUpperRight.y){
                        emptyPlacesInJungle.add(tempPosition);
                    } else {
                        emptyPlacesOnSteppe.add(tempPosition);
                    }
                }
            }
        }

        int numberOfEmptyPlacesInJungle = emptyPlacesInJungle.size();
        int numberOfEmptyPlacesOnSteppe = emptyPlacesOnSteppe.size();

        if (numberOfEmptyPlacesInJungle > 0) {
            Vector2d positionForNewGrass = emptyPlacesInJungle.get((int) (Math.random() * numberOfEmptyPlacesInJungle));
            place(new Grass(positionForNewGrass));
        }

        if (numberOfEmptyPlacesOnSteppe > 0) {
            Vector2d positionForNewGrass = emptyPlacesOnSteppe.get((int) (Math.random() * numberOfEmptyPlacesOnSteppe));
            place(new Grass(positionForNewGrass));
        }
    }

    public Vector2d findPlaceToAddAnimal() {
        ArrayList<Vector2d> emptyPlaces = new ArrayList<>();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Vector2d tempPosition = new Vector2d(x, y);
                if (objectAt(tempPosition) == null) {
                    emptyPlaces.add(tempPosition);
                    }
                }
            }

        int numberOfEmptyPlaces = emptyPlaces.size();
        if (numberOfEmptyPlaces > 0) {
            return emptyPlaces.get((int) (Math.random() * numberOfEmptyPlaces));
        }

        return null;
    }

    public void placeAnimals() {
        for (int i = 0; i < this.numberOfAnimalToPlace; i++) {
            Vector2d position = findPlaceToAddAnimal();
            if (position != null) {
                place(new Animal(this, position, this.startEnergy));
            }
        }
    }

    @Override
    public void positionChanged(Vector2d oldPosition, Vector2d newPosition, Animal animal) {
        if (canMoveTo(newPosition)) {
            removeAnimal(animal, oldPosition);
            addAnimal(animal, newPosition);
        }
    }

    public ArrayList<Animal> getAllAnimals() {
        return this.animalList;
    }

    public int getEra() {
        return this.era;
    }

    public Genotype getDominantGenotype() {
        return this.dominantGenotype;
    }

    public int getNumberOfAnimalsWithDominantGenotype() {
        return this.numberOfAnimalsWithDominantGenotype;
    }

    public boolean isRunning() {
        return this.running;
    }

    public void changeRunning() {
        this.running = !isRunning();
    }

    public void letsDoMagic() {
        for (Animal animal : animalList) {
            Vector2d position = findPlaceToAddAnimal();
            if (position != null) {
                Animal copyAnimal = new Animal(this, position, this.startEnergy);
                copyAnimal.setGenotype(animal.getAnimalGenotype());
                place(copyAnimal);
                this.numberOfAnimals++;
            }
        }

        this.magicLeft--;
    }

    public void updateNumberOfPlants() {
        this.numberOfPlants = this.grassList.size();
    }

    public void updateAverageEnergy() {
        int allEnergy = 0;
        for (Animal animal : this.animalList) {
            allEnergy += animal.getEnergy();
        }
        this.averageEnergy = allEnergy / animalList.size();
    }

    public void updateAverageLifespan(int age) {
        this.averageLifespan *= this.numberOfDeadAnimals;
        this.averageLifespan += age;
        this.numberOfDeadAnimals++;
        this.averageLifespan /= this.numberOfDeadAnimals;
    }

    public void updateAverageNumberOfBabiesWhenBabyMade() {
        this.averageNumberOfBabies *= this.numberOfAnimals;
        this.averageNumberOfBabies += 2;
        this.numberOfAnimals++;
        this.averageNumberOfBabies /= numberOfAnimals;
    }

    public void updateAverageNumberOfBabiesWhenAnimalDied(Animal animal) {
        this.averageNumberOfBabies *= this.numberOfAnimals;
        this.averageNumberOfBabies -= animal.getAmountOfBabies();
        this.numberOfAnimals--;
        this.averageNumberOfBabies /= this.numberOfAnimals;
    }

    public void updateDominantGenotype() {
        for (Animal animal : this.animalList) {
            if (this.allGenotype.get(animal.getAnimalGenotype()) != null) {
                int numberOfAnimalsWithThisGenotype = this.allGenotype.get(animal.getAnimalGenotype());
                numberOfAnimalsWithThisGenotype++;
                this.allGenotype.replace(animal.getAnimalGenotype(), numberOfAnimalsWithThisGenotype);
            } else {
                this.allGenotype.put(animal.getAnimalGenotype(), 1);
            }
        }

        for (Genotype genotype : this.allGenotype.keySet()) {
            int numberOfAnimalsWithThisGenotype = this.allGenotype.get(genotype);
            if (numberOfAnimalsWithThisGenotype > this.numberOfAnimalsWithDominantGenotype) {
                this.numberOfAnimalsWithDominantGenotype = numberOfAnimalsWithThisGenotype;
                this.dominantGenotype = genotype;
            }
        }
    }

    public void updateStatistics() {
        updateNumberOfPlants();
        updateAverageEnergy();
        updateDominantGenotype();
    }
}
