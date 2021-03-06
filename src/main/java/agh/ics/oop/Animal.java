package agh.ics.oop;

import agh.ics.oop.gui.Tracker;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Animal extends AbstractWorldMapElement implements IMapElement{
    private MapDirection orientation = MapDirection.NORTH;
    private AbstractWorldMap map;
    private final List<IPositionChangeObserver> observersList = new ArrayList<>();
    private Vector2d position;
    private Genotype genotype;
    private int energy;
    private int amountOfBabies;
    private int eraOfBirth;
    private boolean tracker;
    private Tracker animalTracker;

    public Animal() {
        this.position = new Vector2d(2, 2);
        this.genotype = new Genotype(8, 32);
        this.tracker = false;
        this.amountOfBabies = 0;
    }

    public Animal(AbstractWorldMap map) {
        this();
        this.map = map;
        this.eraOfBirth = map.getEra();
    }

    public Animal(AbstractWorldMap map, Vector2d position) {
        this(map);
        this.position = position;
    }

    public Animal(AbstractWorldMap map, Vector2d position, int energy) {
        this(map, position);
        this.energy = energy;
    }

    @Override
    public String toString() {
        return switch (this.orientation) {
            case NORTH -> "N";
            case NORTH_EAST -> "NE";
            case EAST -> "E";
            case SOUTH_EAST -> "SE";
            case SOUTH -> "S";
            case SOUTH_WEST -> "SW";
            case WEST -> "W";
            case NORTH_WEST -> "NW";
        };
    }

    public void move(int degree) {
        switch (degree) {
            case 0 -> {
                if (map.canMoveTo(this.position.add(this.orientation.toUnitVector()))) {
                    positionChanged(this.position, this.position.add(this.orientation.toUnitVector()));
                    this.position = this.position.add(this.orientation.toUnitVector());
                }
            }
            case 4 -> {
                if (map.canMoveTo(this.position.subtract(this.orientation.toUnitVector()))) {
                    positionChanged(this.position, this.position.subtract(this.orientation.toUnitVector()));
                    this.position = this.position.subtract(this.orientation.toUnitVector());
                }
            }
            case 1, 2, 3, 5, 6, 7 -> this.orientation = this.orientation.rotate(degree);
        }
    }

    public void addObserver(IPositionChangeObserver observer) {
        this.observersList.add(observer);
    }

    public void removeObserver(IPositionChangeObserver observer) {
        this.observersList.remove(observer);
    }

    private void positionChanged(Vector2d oldPosition, Vector2d newPosition) {
        for (IPositionChangeObserver observer : this.observersList)
            observer.positionChanged(oldPosition, newPosition, this);
    }

    public void changeEnergy(int energy) {
        this.energy += energy;
        if (this.energy < 0) {
            this.energy = 0;
        }
    }

    public void changeTracker() {
        this.tracker = !getTracker();
    }

    public Animal makeBaby(Animal partner) {
        int babyEnergy = (int) (0.25 * partner.energy) + (int) (0.25 * this.energy);
        partner.changeEnergy((int) (-0.25 * partner.energy));
        this.changeEnergy((int) (-0.25 * this.energy));
        Animal baby = new Animal(this.map, this.position, babyEnergy);
        if (this.energy >= partner.energy) {
            baby.genotype = new Genotype(this.genotype, this.energy, partner.genotype, partner.energy);
        } else {
            baby.genotype = new Genotype(partner.genotype, partner.energy, this.genotype, this.energy);
        }

        if (this.getTracker() || partner.getTracker()) {
            if (this.getTracker()) {
                Animal tempAnimal = this.animalTracker.getTrackedAnimal();
                if (tempAnimal.equals(this) || tempAnimal.equals(partner)) {
                    this.animalTracker.increaseNumberOfChildren();
                }
                this.animalTracker.increaseNumberOfAllFamily();
                baby.changeTracker();
                baby.setAnimalTracker(this.animalTracker);
            } else {
                Animal tempAnimal = partner.animalTracker.getTrackedAnimal();
                if (tempAnimal.equals(this) || tempAnimal.equals(partner)) {
                    partner.animalTracker.increaseNumberOfChildren();
                }
                partner.animalTracker.increaseNumberOfAllFamily();
                baby.changeTracker();
                baby.setAnimalTracker(partner.animalTracker);
            }
        }

        this.amountOfBabies++;
        partner.amountOfBabies++;

        return baby;
    }

    @Override
    public Vector2d getPosition() {
        return this.position;
    }

    public boolean isDead() {
        return this.energy == 0;
    }

    public void setGenotype(Genotype genotype) {
        this.genotype = genotype;
    }

    public int getEnergy() {
        return this.energy;
    }

    public int getAmountOfBabies() {
        return this.amountOfBabies;
    }

    public int getEraOfBirth() {
        return this.eraOfBirth;
    }

    public boolean getTracker() {
        return this.tracker;
    }

    public Genotype getAnimalGenotype() {
        return this.genotype;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Animal animal = (Animal) o;
        return amountOfBabies == animal.amountOfBabies && eraOfBirth == animal.eraOfBirth && tracker == animal.tracker && map.equals(animal.map) && Objects.equals(observersList, animal.observersList) && genotype.equals(animal.genotype) && Objects.equals(animalTracker, animal.animalTracker);
    }

    @Override
    public int hashCode() {
        return Objects.hash(map, observersList, genotype, amountOfBabies, eraOfBirth, tracker, animalTracker);
    }

    public void setAnimalTracker(Tracker animalTracker) {
        this.animalTracker = animalTracker;
    }
}
