package agh.ics.oop.gui;

import agh.ics.oop.AbstractWorldMap;
import javafx.application.Platform;
import javafx.scene.control.Label;

import java.util.Arrays;

public class DominantGenotypeHandler {
    private final Label genotypeLabel = new Label();
    private final AbstractWorldMap map;

    public DominantGenotypeHandler(AbstractWorldMap map) {
        this.map = map;
    }

    public Label getGenotypeLabel() {
        return this.genotypeLabel;
    }

    public void setGenotypeLabel() {
        Platform.runLater(() -> genotypeLabel.setText(Arrays.toString(this.map.getDominantGenotype().getGenotype())));
    }
}
