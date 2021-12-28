package agh.ics.oop.gui;

import agh.ics.oop.BoundedMap;
import agh.ics.oop.ThreadedSimulationEngine;
import agh.ics.oop.UnboundedMap;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class App extends Application {
    private Stage window;

    //parameters
    private int paramWidth;
    private int paramHeight;
    private float paramJungleRatio;
    private int paramStartEnergy;
    private int paramMoveEnergy;
    private int paramPlantEnergy;
    private int paramNumberOfAnimals;
    private boolean magicForUnboundedMap;
    private boolean magicForBoundedMap;

    //maps
    private UnboundedMap unboundedMap;
    private BoundedMap boundedMap;

    public int getInt(String input) {
        try {
            return Integer.parseInt(input);
        }catch(NumberFormatException exception) {
            System.out.println(input + " is not a number");
            System.exit(-1);
            return -1;
        }
    }

    public float getFloat(String input) {
        try {
            return Float.parseFloat(input);
        }catch(NumberFormatException exception) {
            System.out.println(input + " is not a number");
            System.exit(-1);
            return -1;
        }
    }

    public boolean checkParameter(int parameter) {
        return parameter < 1;
    }

    public void checkAllParameters() {
        if(checkParameter(this.paramWidth)) {
            this.paramWidth = 40;
        }
        if(checkParameter(this.paramHeight)) {
            this.paramHeight = 40;
        }
        if(this.paramJungleRatio < 0) {
            this.paramJungleRatio = 0.25f;
        }
        if(checkParameter(this.paramStartEnergy)) {
            this.paramStartEnergy = 100;
        }
        if(checkParameter(this.paramMoveEnergy)) {
            this.paramMoveEnergy = 3;
        }
        if(checkParameter(this.paramPlantEnergy)) {
            this.paramPlantEnergy = 100;
        }
        if(checkParameter(this.paramNumberOfAnimals)) {
            this.paramNumberOfAnimals = 50;
        }
    }

    public void handleCheckBoxes(CheckBox unboundedMap, CheckBox boundedMap) {
        this.magicForUnboundedMap = unboundedMap.isSelected();
        this.magicForBoundedMap = boundedMap.isSelected();
    }

    public void letsStart() {
        this.unboundedMap = new UnboundedMap(this.paramHeight, this.paramWidth, this.paramJungleRatio,
                this.paramStartEnergy, this.paramMoveEnergy, this.paramPlantEnergy,
                this.paramNumberOfAnimals, this.magicForUnboundedMap);
        this.boundedMap = new BoundedMap(this.paramHeight, this.paramWidth, this.paramJungleRatio,
                this.paramStartEnergy, this.paramMoveEnergy, this.paramPlantEnergy,
                this.paramNumberOfAnimals, this.magicForBoundedMap);


        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(8);
        grid.setHgap(200);

        GridHandler unboundedMapGrid = new GridHandler(this.unboundedMap, this.paramWidth, this.paramHeight);
        DominantGenotypeHandler unboundedMapDominantGenotypeHandler = new DominantGenotypeHandler(this.unboundedMap);
        GraphsHandler unboundedMapGraphsHandler = new GraphsHandler(this.unboundedMap);
        Tracker unboundedMapTracker = new Tracker(this.unboundedMap);

        GridHandler boundedMapGrid = new GridHandler(this.boundedMap, this.paramWidth, this.paramHeight);
        DominantGenotypeHandler boundedMapDominantGenotypeHandler = new DominantGenotypeHandler(this.boundedMap);
        GraphsHandler boundedMapGraphsHandler = new GraphsHandler(this.boundedMap);
        Tracker boundedMapTracker = new Tracker(this.boundedMap);

        Label unboundedMapLabel = new Label("Unbounded Map");
        GridPane.setConstraints(unboundedMapLabel, 0, 0, 2, 1);
        GridPane.setHalignment(unboundedMapLabel, HPos.CENTER);
        GridPane unboundedMapGridPane = unboundedMapGrid.getGrid();
        GridPane.setConstraints(unboundedMapGridPane, 0, 1, 2, 1);
        GridPane.setHalignment(unboundedMapGridPane, HPos.CENTER);
        GridPane.setValignment(unboundedMapGridPane, VPos.CENTER);

        Label boundedMapLabel = new Label("Bounded Map");
        GridPane.setConstraints(boundedMapLabel, 2, 0, 2, 1);
        GridPane.setHalignment(boundedMapLabel, HPos.CENTER);
        GridPane boundedMapGridPane = boundedMapGrid.getGrid();
        GridPane.setConstraints(boundedMapGridPane, 2, 1, 2, 1);
        GridPane.setHalignment(boundedMapGridPane, HPos.CENTER);
        GridPane.setValignment(boundedMapGridPane, VPos.CENTER);

        Label unboundedMapDominantGenotypeLabel = new Label("Dominant genotype");
        GridPane.setConstraints(unboundedMapDominantGenotypeLabel, 0, 2, 2, 1);
        GridPane.setHalignment(unboundedMapDominantGenotypeLabel, HPos.CENTER);
        Label unboundedMapDominantGenotype = unboundedMapDominantGenotypeHandler.getGenotypeLabel();
        GridPane.setConstraints(unboundedMapDominantGenotype, 0, 3, 2, 1);
        GridPane.setHalignment(unboundedMapDominantGenotype, HPos.CENTER);
        GridPane.setValignment(unboundedMapDominantGenotype, VPos.CENTER);

        Label boundedMapDominantGenotypeLabel = new Label("Dominant genotype");
        GridPane.setConstraints(boundedMapDominantGenotypeLabel, 2, 2, 2, 1);
        GridPane.setHalignment(boundedMapDominantGenotypeLabel, HPos.CENTER);
        Label boundedMapDominantGenotype = boundedMapDominantGenotypeHandler.getGenotypeLabel();
        GridPane.setConstraints(boundedMapDominantGenotype, 2, 3, 2, 1);
        GridPane.setHalignment(boundedMapDominantGenotype, HPos.CENTER);
        GridPane.setValignment(boundedMapDominantGenotype, VPos.CENTER);

        Label unboundedMapGraphsLabel = new Label("Graphs");
        GridPane.setConstraints(unboundedMapGraphsLabel, 0, 4, 2, 1);
        GridPane.setHalignment(unboundedMapGraphsLabel, HPos.CENTER);
        GridPane unboundedMapGraphs = unboundedMapGraphsHandler.getGraphs();
        GridPane.setConstraints(unboundedMapGraphs, 0, 5, 2, 2);
        GridPane.setHalignment(unboundedMapGraphs, HPos.CENTER);
        GridPane.setValignment(unboundedMapGraphs, VPos.CENTER);

        Label boundedMapGraphsLabel = new Label("Graphs");
        GridPane.setConstraints(boundedMapGraphsLabel, 2, 4, 2, 1);
        GridPane.setHalignment(boundedMapGraphsLabel, HPos.CENTER);
        GridPane boundedMapGraphs = boundedMapGraphsHandler.getGraphs();
        GridPane.setConstraints(boundedMapGraphs, 2, 5, 2, 2);
        GridPane.setHalignment(boundedMapGraphs, HPos.CENTER);
        GridPane.setValignment(boundedMapGraphs, VPos.CENTER);

        Button unboundedMapStartStopButton = new Button("Pause");
        GridPane.setConstraints(unboundedMapStartStopButton, 0, 7, 2, 1);
        GridPane.setHalignment(unboundedMapStartStopButton, HPos.CENTER);
        unboundedMapStartStopButton.setMinSize(70, 30);
        unboundedMapStartStopButton.setOnAction(click -> {
            if (this.unboundedMap.isRunning()) {
                unboundedMapStartStopButton.setText("Resume");
                this.unboundedMap.changeRunning();

            } else {
                unboundedMapStartStopButton.setText("Pause");
                this.unboundedMap.changeRunning();
            }
        });

        Button boundedMapStartStopButton = new Button("Pause");
        GridPane.setConstraints(boundedMapStartStopButton, 2, 7, 2, 1);
        GridPane.setHalignment(boundedMapStartStopButton, HPos.CENTER);
        boundedMapStartStopButton.setMinSize(70, 30);
        boundedMapStartStopButton.setOnAction(click -> {
            if (this.boundedMap.isRunning()) {
                this.boundedMap.changeRunning();
                boundedMapStartStopButton.setText("Resume");
            } else {
                this.boundedMap.changeRunning();
                boundedMapStartStopButton.setText("Pause");
            }
        });

        Label unboundedMapWhenPausedLabel = new Label("Things you can do when simulation paused");
        GridPane.setConstraints(unboundedMapWhenPausedLabel, 0, 8, 2, 1);
        GridPane.setHalignment(unboundedMapWhenPausedLabel, HPos.CENTER);
        Button unboundedMapSaveButton = new Button("Save");
        GridPane.setConstraints(unboundedMapSaveButton, 0, 9, 2,1);
        unboundedMapSaveButton.setMinSize(70, 30);
        GridPane.setHalignment(unboundedMapSaveButton, HPos.CENTER);
        Button unboundedMapShowAnimalsWithDominantGenotypeButton = new Button("Show Animals With Dominant Genotype");
        GridPane.setConstraints(unboundedMapShowAnimalsWithDominantGenotypeButton, 0, 10,2,1);
        unboundedMapShowAnimalsWithDominantGenotypeButton.setMinSize(70, 30);
        GridPane.setHalignment(unboundedMapShowAnimalsWithDominantGenotypeButton, HPos.CENTER);

        Label boundedMapWhenPausedLabel = new Label("Things you can do when simulation paused");
        GridPane.setConstraints(boundedMapWhenPausedLabel, 2, 8, 2, 1);
        GridPane.setHalignment(boundedMapWhenPausedLabel, HPos.CENTER);
        Button boundedMapSaveButton = new Button("Save");
        GridPane.setConstraints(boundedMapSaveButton, 2, 9,2,1);
        boundedMapSaveButton.setMinSize(70, 30);
        GridPane.setHalignment(boundedMapSaveButton, HPos.CENTER);
        Button boundedMapShowAnimalsWithDominantGenotypeButton = new Button("Show Animals With Dominant Genotype");
        GridPane.setConstraints(boundedMapShowAnimalsWithDominantGenotypeButton, 2, 10,2,1);
        boundedMapShowAnimalsWithDominantGenotypeButton.setMinSize(70, 30);
        GridPane.setHalignment(boundedMapShowAnimalsWithDominantGenotypeButton, HPos.CENTER);



        grid.getChildren().addAll(unboundedMapLabel, unboundedMapGridPane,
                boundedMapLabel, boundedMapGridPane,
                unboundedMapDominantGenotypeLabel, unboundedMapDominantGenotype,
                boundedMapDominantGenotypeLabel, boundedMapDominantGenotype,
                unboundedMapGraphsLabel, unboundedMapGraphs,
                boundedMapGraphsLabel, boundedMapGraphs,
                unboundedMapStartStopButton, boundedMapStartStopButton,
                unboundedMapWhenPausedLabel, unboundedMapSaveButton,
                unboundedMapShowAnimalsWithDominantGenotypeButton,
                boundedMapWhenPausedLabel, boundedMapSaveButton,
                boundedMapShowAnimalsWithDominantGenotypeButton);

        grid.setAlignment(Pos.CENTER);

        Scene animationScene = new Scene(grid, 1200, 1500);
        this.window.setScene(animationScene);
        this.window.setMaximized(true);

        Thread unboundedMapThread = new Thread(new ThreadedSimulationEngine(
                this.unboundedMap, unboundedMapGrid,
                unboundedMapDominantGenotypeHandler, unboundedMapGraphsHandler)
        );

        Thread boundedMapThread = new Thread(new ThreadedSimulationEngine(
                this.boundedMap, boundedMapGrid,
                boundedMapDominantGenotypeHandler, boundedMapGraphsHandler)
        );

        unboundedMapThread.start();
        boundedMapThread.start();
    }

    public Scene createUserInterface() {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(8);
        grid.setHgap(10);

        Label widthLabel = new Label("width:");
        GridPane.setConstraints(widthLabel, 0, 0);
        TextField widthInput = new TextField("20");
        GridPane.setConstraints(widthInput, 1, 0);

        Label heightLabel = new Label("height:");
        GridPane.setConstraints(heightLabel, 0, 1);
        TextField heightInput = new TextField("20");
        GridPane.setConstraints(heightInput, 1, 1);

        Label jungleRatioLabel = new Label("jungleRatio:");
        GridPane.setConstraints(jungleRatioLabel, 0, 2);
        TextField jungleRatioInput = new TextField("0.25");
        GridPane.setConstraints(jungleRatioInput, 1, 2);

        Label startEnergyLabel = new Label("startEnergy:");
        GridPane.setConstraints(startEnergyLabel, 0, 3);
        TextField startEnergyInput = new TextField("100");
        GridPane.setConstraints(startEnergyInput, 1, 3);

        Label moveEnergyLabel = new Label("moveEnergy:");
        GridPane.setConstraints(moveEnergyLabel, 0, 4);
        TextField moveEnergyInput = new TextField("3");
        GridPane.setConstraints(moveEnergyInput, 1, 4);

        Label plantEnergyILabel = new Label("plantEnergy:");
        GridPane.setConstraints(plantEnergyILabel, 0, 5);
        TextField plantEnergyInput = new TextField("100");
        GridPane.setConstraints(plantEnergyInput, 1, 5);

        Label numberOfAnimalsLabel = new Label("numberOfAnimals:");
        GridPane.setConstraints(numberOfAnimalsLabel, 0, 6);
        TextField numberOfAnimalsInput = new TextField("20");
        GridPane.setConstraints(numberOfAnimalsInput, 1, 6);

        Label magicUnboundedMapLabel = new Label("magicForUnboundedMap:");
        GridPane.setConstraints(magicUnboundedMapLabel, 0, 7);
        CheckBox magicUnboundedMapCheckBox = new CheckBox();
        GridPane.setConstraints(magicUnboundedMapCheckBox, 1, 7);

        Label magicBoundedMapLabel = new Label("magicForBoundedMap:");
        GridPane.setConstraints(magicBoundedMapLabel, 0, 8);
        CheckBox magicBoundedMapCheckBox = new CheckBox();
        GridPane.setConstraints(magicBoundedMapCheckBox, 1, 8);

        Button startButton = new Button("Start");
        GridPane.setConstraints(startButton,0, 9, 2, 1);
        GridPane.setHalignment(startButton, HPos.CENTER);
        startButton.setMaxWidth(100);
        startButton.setOnAction(click -> {
            this.paramWidth = getInt(widthInput.getText());
            this.paramHeight = getInt(heightInput.getText());
            this.paramJungleRatio = getFloat(jungleRatioInput.getText());
            this.paramStartEnergy = getInt(startEnergyInput.getText());
            this.paramMoveEnergy = getInt(moveEnergyInput.getText());
            this.paramPlantEnergy = getInt(plantEnergyInput.getText());
            this.paramNumberOfAnimals = getInt(numberOfAnimalsInput.getText());
            checkAllParameters();
            handleCheckBoxes(magicUnboundedMapCheckBox, magicBoundedMapCheckBox);
            letsStart();
        });

        grid.getChildren().addAll(widthLabel, widthInput,
                heightLabel, heightInput,
                jungleRatioLabel, jungleRatioInput,
                startEnergyLabel, startEnergyInput,
                moveEnergyLabel, moveEnergyInput,
                plantEnergyILabel, plantEnergyInput,
                numberOfAnimalsLabel, numberOfAnimalsInput,
                magicUnboundedMapLabel, magicUnboundedMapCheckBox,
                magicBoundedMapLabel, magicBoundedMapCheckBox,
                startButton);



        grid.setAlignment(Pos.CENTER);

        return new Scene(grid, 320, 350);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.window = primaryStage;
        this.window.setOnCloseRequest(event -> {
            Platform.exit();
            System.exit(0);
        });
        this.window.setTitle("Evolution Generator");
        this.window.setScene(createUserInterface());
        this.window.show();
    }
}
