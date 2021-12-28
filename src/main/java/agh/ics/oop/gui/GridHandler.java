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

public class GridHandler {
    private final GridPane grid = new GridPane();
    private final AbstractWorldMap map;
    private final int width;
    private final int height;
    private final int cellSize;
    private Animal trackedAnimal;
    private AlertHandler alertHandler;

    public GridHandler(AbstractWorldMap map, int width, int height) {
        this.map = map;
        this.width = width;
        this.height = height;
        this.cellSize = 20;
        setGrid();
        this.alertHandler = new AlertHandler(map);
    }

    public GridPane getGrid() {
        return this.grid;
    }

    public void setGrid() {
        Platform.runLater(() -> {
            this.grid.setGridLinesVisible(false);
            this.grid.setGridLinesVisible(true);
            this.grid.getRowConstraints().clear();
            this.grid.getColumnConstraints().clear();

            if(this.trackedAnimal != null && this.trackedAnimal.isDead()){
                this.trackedAnimal = null;
            }

            for (int x = 0; x <= width; x++) {
                for (int y = 0; y <= height; y++) {
                    Vector2d position = new Vector2d(x, y);
                    if (this.map.objectAt(position) instanceof Grass) {
                        Label newLabel = new Label();
                        newLabel.setMinWidth(this.cellSize);
                        newLabel.setMinHeight(this.cellSize);
                        newLabel.setStyle("-fx-background-color: limegreen ;");
                        grid.add(newLabel, x, y);
                    } else if (this.map.objectAt(position) instanceof Animal){
                        Button animalButton = new Button();
                        animalButton.setPadding(new Insets(0, 0, 0, 0));
                        animalButton.setMaxWidth(this.cellSize);
                        animalButton.setMaxHeight(this.cellSize);
                        GridPane.setHalignment(animalButton, HPos.CENTER);
                        GridPane.setValignment(animalButton, VPos.CENTER);
                        animalButton.setStyle("-fx-border-color: transparent ");
                        if (this.map.isInJungle(position)){
                            animalButton.setStyle("-fx-background-color: forestgreen ;");
                        } else {
                            animalButton.setStyle("-fx-background-color: greenyellow;");
                        }

                        int animalEnergy = ((Animal) this.map.objectAt(position)).getEnergy();
                        int startEnergy = this.map.getStartEnergy();
                        if (0 < animalEnergy && animalEnergy < startEnergy * 0.2) {
                            animalButton.setStyle("-fx-background-color: black");
                        } else if (startEnergy * 0.2 <= animalEnergy && animalEnergy < startEnergy * 0.4) {
                            animalButton.setStyle("-fx-background-color: dimgray");
                        } else if (startEnergy * 0.4 <= animalEnergy && animalEnergy < startEnergy * 0.6) {
                            animalButton.setStyle("-fx-background-color: darkgrey");
                        } else if (startEnergy * 0.6 <= animalEnergy && animalEnergy < startEnergy * 0.8) {
                            animalButton.setStyle("-fx-background-color: lightgray");
                        } else if (startEnergy * 0.8 <= animalEnergy) {
                            animalButton.setStyle("-fx-background-color: white");
                        } else if (0 == animalEnergy) {
                            animalButton.setStyle("-fx-background-color: lightsalmon");
                        }

                        animalButton.setOnAction(click -> {
                            this.alertHandler.handleAnimalGenotype((Animal) this.map.objectAt(position));

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

                        if(((Animal) map.objectAt(position)).getTracker()){
                            animalButton.setStyle("-fx-background-color: crimson");
                        }

                        grid.add(animalButton, x, y);
                    } else {
                        Label newLabel = new Label();
                        newLabel.setMinWidth(this.cellSize);
                        newLabel.setMinHeight(this.cellSize);
                        if (map.isInJungle(position)) {
                            newLabel.setStyle("-fx-background-color: forestgreen;");
                        } else {
                            newLabel.setStyle("-fx-background-color: greenyellow;");
                        }
                        grid.add(newLabel, x, y);
                    }
                }
            }
        });
    }
}
