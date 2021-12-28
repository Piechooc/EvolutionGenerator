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
    protected Vector2d jungleUpperRight;
    protected Vector2d jungleLowerLeft;

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
        if (position != null) {
            return (position.precedes(this.upperRight) && position.follows(this.lowerLeft));
        }
        return false;
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
            this.animals.get(position).remove(animal);
            if (list.size() == 0) {
                this.animals.remove(position);
            }
        }
    }

    @Override
    public boolean place(AbstractWorldMapElement element, Vector2d position){
        if (canMoveTo(position)) {
            if (element instanceof Animal) {
                addAnimal((Animal) element, position);
                this.animalList.add((Animal) element);
                ((Animal) element).addObserver(this);
            } else {
                this.wholeGrass.computeIfAbsent(position, k -> (Grass) element);
                this.grassList.add((Grass) element);
            }
            return true;
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
            if (list.get(0).getEnergy() == 0) {
                return null;
            } else {
                return list.get(0);
            }
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
            ArrayList<Animal> list = this.animals.get(grass.getPosition());
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
            place(baby, firstParent.getPosition());
            updateAverageNumberOfBabiesWhenBabyMade();
        }
    }

    public void findParents() {
        Map<Vector2d, ArrayList<Animal>> allParents = new LinkedHashMap<>();
        for (ArrayList<Animal> list : this.animals.values()) {
            if (list != null && list.size() > 1) {
                if (list.size() == 2) {
                    ArrayList<Animal> parents = new ArrayList<>();
                    parents.add(list.get(0));
                    parents.add(list.get(1));
                    allParents.put(list.get(0).getPosition(), parents);
                } else {
                    ArrayList<Integer> indexesOfStrongestAnimals = findIndexesOfStrongestAnimals(list, -1);
                    ArrayList<Animal> parents = new ArrayList<>();
                    int firstIndex = indexesOfStrongestAnimals.get(0);
                    parents.add(list.get(firstIndex));
                    if (indexesOfStrongestAnimals.size() > 1) {
                        int secondIndex = indexesOfStrongestAnimals.get(1);
                        parents.add(list.get(secondIndex));
                    } else {
                        ArrayList<Integer> indexesOfSecondParents = findIndexesOfStrongestAnimals(list,
                                list.get(firstIndex).getEnergy());
                        int secondIndex = indexesOfSecondParents.get(0);
                        parents.add(list.get(secondIndex));
                        }

                    allParents.put(list.get(firstIndex).getPosition(), parents);
                    }
                }
            }

        for (ArrayList<Animal> parents : allParents.values()) {
            makeBaby(parents.get(0), parents.get(1));
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
        ArrayList<Animal> deadAnimals = new ArrayList<>();
        if (list != null && list.size() > 0) {
            for (Animal animal : list) {
                if (animal.isDead()) {
                    deadAnimals.add(animal);
                }
            }

            for (Animal animal : deadAnimals) {
                removeAnimal(animal, animal.getPosition());
                animal.removeObserver(this);
                this.animalList.remove(animal);
                updateAverageLifespan(this.era - animal.getEraOfBirth());
                updateAverageNumberOfBabiesWhenAnimalDied(animal);
            }
        }
    }

    public boolean isInJungle(Vector2d position) {
        return jungleLowerLeft.x <= position.x && position.x <= jungleUpperRight.x
                && jungleLowerLeft.y <= position.y && position.y <= jungleUpperRight.y;
    }

    public void plantGrass() {
        ArrayList<Vector2d> emptyPlacesInJungle = new ArrayList<>();
        ArrayList<Vector2d> emptyPlacesOnSteppe = new ArrayList<>();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Vector2d tempPosition = new Vector2d(x, y);
                if (objectAt(tempPosition) == null) {
                    if (isInJungle(tempPosition)){
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
            place(new Grass(positionForNewGrass), positionForNewGrass);
        }

        if (numberOfEmptyPlacesOnSteppe > 0) {
            Vector2d positionForNewGrass = emptyPlacesOnSteppe.get((int) (Math.random() * numberOfEmptyPlacesOnSteppe));
            place(new Grass(positionForNewGrass), positionForNewGrass);
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
                place(new Animal(this, position, this.startEnergy), position);
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

    public int getNumberOfAnimals() {
        return this.numberOfAnimals;
    }

    public int getNumberOfPlants() {
        return this.numberOfPlants;
    }

    public int getAverageEnergy() {
        return this.averageEnergy;
    }

    public int getAverageLifespan() {
        return this.averageLifespan;
    }

    public int getAverageNumberOfBabies() {
        return this.averageNumberOfBabies;
    }

    public Genotype getDominantGenotype() {
        return this.dominantGenotype;
    }

    public int getNumberOfAnimalsWithDominantGenotype() {
        return this.numberOfAnimalsWithDominantGenotype;
    }

    public int getStartEnergy() {
        return this.startEnergy;
    }

    public int getMagicLeft() {
        return this.magicLeft;
    }

    public boolean isMagic() {
        return this.magic;
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
                place(copyAnimal, position);
                this.numberOfAnimals++;
            }
        }

        this.magicLeft--;
    }

    public void updateNumberOfPlants() {
        this.numberOfPlants = this.grassList.size();
    }

    public void updateAverageEnergy() {
        if (this.animalList.size() > 0) {
            int allEnergy = 0;
            for (Animal animal : this.animalList) {
                allEnergy += animal.getEnergy();
            }
            this.averageEnergy = allEnergy / animalList.size();
        } else {
            this.averageEnergy = 0;
        }
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
