package agh.ics.oop.gui;

import agh.ics.oop.AbstractWorldMap;
import agh.ics.oop.Animal;
import javafx.application.Platform;
import javafx.scene.control.Alert;

import java.util.Arrays;

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
}
