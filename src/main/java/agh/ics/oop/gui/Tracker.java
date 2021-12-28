package agh.ics.oop.gui;

import agh.ics.oop.AbstractWorldMap;
import agh.ics.oop.Animal;
import javafx.application.Platform;
import javafx.scene.control.Label;

import java.util.Objects;

public class Tracker {
    private AbstractWorldMap map;
    private int numberOfChildren = 0;
    private int numberOfDescendants = 0;
    private int eraOfDeath = -1;
    private int numberOfAllFamily = 0;
    private final Label numberOfChildrenLabel = new Label();
    private final Label numberOfDescendantsLabel = new Label();
    private final Label eraOfDeathLabel = new Label();
    private final Label trackingStatusLabel = new Label();
    private Animal trackedAnimal = null;
    private boolean dead = false;

    public Tracker(AbstractWorldMap map) {
        this.map = map;
    }

    private void setEraOfDeath() {
        this.eraOfDeath = this.map.getEra();
        this.dead = true;
    }

    private String getStatus() {
        if (this.trackedAnimal == null) {
            return "null";
        } else {
            if (this.trackedAnimal.isDead()) {
                if (!this.dead) {
                    setEraOfDeath();
                }
                return "Dead animal";
            } else {
                return "Living animal";
            }
        }
    }

    public void setTracker() {
        Platform.runLater(() -> {
            this.numberOfDescendants = this.numberOfAllFamily - this.numberOfChildren;
            this.trackingStatusLabel.setText("Tracking status: " + getStatus());
            this.numberOfChildrenLabel.setText("Number of children: " + this.numberOfChildren);
            this.numberOfDescendantsLabel.setText("Number of descendants: " + this.numberOfDescendants);
            this.eraOfDeathLabel.setText("Era of death: " + this.eraOfDeath);
        });
    }

    public void takeTrackedAnimal(Animal newAnimal) {
        if (!newAnimal.equals(this.trackedAnimal)) {
            setTrackedAnimal(newAnimal);
        }
    }

    public void setTrackedAnimal(Animal animal) {
        this.trackedAnimal = animal;
        this.trackedAnimal.setAnimalTracker(this);
        this.numberOfChildren = 0;
        this.numberOfDescendants = 0;
        this.numberOfAllFamily = 0;
        this.eraOfDeath = -1;
    }

    public Label getTrackingStatusLabel() {
        return this.trackingStatusLabel;
    }

    public Label getNumberOfChildrenLabel() {
        return this.numberOfChildrenLabel;
    }

    public Label getNumberOfDescendantsLabel() {
        return this.numberOfDescendantsLabel;
    }

    public Label getEraOfDeathLabel() {
        return this.eraOfDeathLabel;
    }

    public Animal getTrackedAnimal() {
        return this.trackedAnimal;
    }

    public void increaseNumberOfChildren() {
        this.numberOfChildren++;
    }

    public void increaseNumberOfAllFamily() {
        this.numberOfAllFamily++;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tracker tracker = (Tracker) o;
        return map.equals(tracker.map) && Objects.equals(trackedAnimal, tracker.trackedAnimal);
    }

    @Override
    public int hashCode() {
        return Objects.hash(map, trackedAnimal);
    }
}
