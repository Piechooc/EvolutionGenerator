package agh.ics.oop.gui;

import agh.ics.oop.AbstractWorldMap;
import agh.ics.oop.Animal;
import agh.ics.oop.Vector2d;
import javafx.application.Platform;
import javafx.scene.control.Alert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class AlertHandler {
    private final AbstractWorldMap map;
    private final Alert alert = new Alert(Alert.AlertType.INFORMATION);

    public AlertHandler(AbstractWorldMap map) {
        this.map = map;
    }

    public void handleMagic() {
        this.alert.setTitle("MAGIC!");
        this.alert.setContentText("MAGIC! Chances left: " + this.map.getMagicLeft());
        Platform.runLater(this.alert::show);
    }

    public void handleAnimalGenotype(Animal animal) {
        this.alert.setTitle("Animal genotype");
        this.alert.setContentText(Arrays.toString(animal.getAnimalGenotype().getGenotype()));
        Platform.runLater(this.alert::show);
    }

    public void handleAllAnimalsWithDominantGenotype(ArrayList<Animal> list) {
        this.alert.setTitle("Animals with dominant genotype");
        ArrayList<Vector2d> positions = new ArrayList<>();
        Set<Animal> setFromList = new HashSet<Animal>(list);

        for (Animal animal : setFromList) {
            positions.add(animal.getPosition());
        }
        this.alert.setContentText("Positions of animals with dominant genotype: " + positions.toString());
        Platform.runLater(this.alert::show);
    }
}
