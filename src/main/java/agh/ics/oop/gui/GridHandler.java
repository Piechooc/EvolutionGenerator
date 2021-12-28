package agh.ics.oop.gui;

import agh.ics.oop.AbstractWorldMap;
import agh.ics.oop.Animal;
import agh.ics.oop.Grass;
import agh.ics.oop.Vector2d;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;

public class GridHandler {
    private final GridPane grid = new GridPane();
    private final AbstractWorldMap map;
    private final int width;
    private final int height;
    private final int cellSize;
    private Animal trackedAnimal;
    private final AlertHandler alertHandler;

    public GridHandler(AbstractWorldMap map, int width, int height) {
        this.map = map;
        this.width = width;
        this.height = height;
        this.cellSize = 15;
        setGrid();
        this.alertHandler = new AlertHandler(map);
    }

    public GridPane getGrid() {
        return this.grid;
    }

    public Animal setGrid() {
        Platform.runLater(() -> {
            refreshGrid();
            if(this.trackedAnimal != null && this.trackedAnimal.isDead()){
                this.trackedAnimal = null;
            }

            for (int x = 0; x <= width; x++) {
                for (int y = 0; y <= height; y++) {
                    Vector2d position = new Vector2d(x, y);
                    if (this.map.objectAt(position) instanceof Grass) {
                        Label newLabel = new Label();
                        customiseLabelSize(newLabel);
                        newLabel.setStyle("-fx-background-color: limegreen ;");
                        this.grid.add(newLabel, x, y);
                    } else if (this.map.objectAt(position) instanceof Animal){
                        Button animalButton = new Button();
                        customiseButton(animalButton);
                        String color = chooseColor((Animal) this.map.objectAt(position));
                        animalButton.setStyle("-fx-background-color: " + color);

                        animalButton.setOnAction(click -> {
                            this.alertHandler.handleAnimalGenotype((Animal) this.map.objectAt(position));
                            animalButton.setStyle("-fx-background-color: crimson");
                            ((Animal) this.map.objectAt(position)).changeTracker();
                            if (((Animal) this.map.objectAt(position)).getTracker()) {
                                if (this.trackedAnimal != null) {
                                    this.trackedAnimal.changeTracker();
                                }
                                this.trackedAnimal = (Animal) this.map.objectAt(position);
                            } else {
                                this.trackedAnimal = null;
                            }
                        });
                        this.grid.add(animalButton, x, y);
                    } else {
                        Label newLabel = new Label();
                        customiseLabelSize(newLabel);
                        if (this.map.isInJungle(position)) {
                            newLabel.setStyle("-fx-background-color: forestgreen;");
                        } else {
                            newLabel.setStyle("-fx-background-color: greenyellow;");
                        }
                        this.grid.add(newLabel, x, y);
                    }
                }
            }
        });

        return this.trackedAnimal;
    }

    public void customiseLabelSize(Label label) {
        label.setMaxSize(this.cellSize, this.cellSize);
        label.setMinSize(this.cellSize, this.cellSize);
    }

    public void refreshGrid() {
        this.grid.setGridLinesVisible(false);
        this.grid.setGridLinesVisible(true);
        this.grid.getRowConstraints().clear();
        this.grid.getColumnConstraints().clear();
    }

    public void customiseButton(Button button) {
        button.setPadding(new Insets(0, 0, 0, 0));
        button.setMaxSize(this.cellSize, this.cellSize);
        button.setMinSize(this.cellSize, this.cellSize);
        GridPane.setHalignment(button, HPos.CENTER);
        GridPane.setValignment(button, VPos.CENTER);
        button.setStyle("-fx-border-color: transparent ");
    }

    public String chooseColor(Animal animal) {
        int animalEnergy = animal.getEnergy();
        int startEnergy = this.map.getStartEnergy();
        if (0 <= animalEnergy && animalEnergy < startEnergy * 0.2) {
            return "black";
        } else if (startEnergy * 0.2 <= animalEnergy && animalEnergy < startEnergy * 0.4) {
            return "dimgray";
        } else if (startEnergy * 0.4 <= animalEnergy && animalEnergy < startEnergy * 0.6) {
            return "darkgrey";
        } else if (startEnergy * 0.6 <= animalEnergy && animalEnergy < startEnergy * 0.8) {
            return "lightgray";
        } else if (startEnergy * 0.8 <= animalEnergy) {
            return "white";
        }
        return "transparent";
    }
}
