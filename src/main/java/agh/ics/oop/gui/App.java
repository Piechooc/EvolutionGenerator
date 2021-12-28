package agh.ics.oop.gui;

import agh.ics.oop.BoundedMap;
import agh.ics.oop.ThreadedSimulationEngine;
import agh.ics.oop.UnboundedMap;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;

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
        grid.setVgap(10);
        grid.setHgap(10);

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
        GridPane unboundedMapGridPane = unboundedMapGrid.getGrid();
        GridPane.setConstraints(unboundedMapGridPane, 0, 1, 2, 1);

        Label boundedMapLabel = new Label("Bounded Map");
        GridPane.setConstraints(boundedMapLabel, 2, 0, 2, 1);
        GridPane boundedMapGridPane = boundedMapGrid.getGrid();
        GridPane.setConstraints(boundedMapGridPane, 2, 1, 2, 1);

        Label unboundedMapDominantGenotypeLabel = new Label("Dominant genotype");
        GridPane.setConstraints(unboundedMapDominantGenotypeLabel, 0, 2, 2, 1);
        Label unboundedMapDominantGenotype = unboundedMapDominantGenotypeHandler.getGenotypeLabel();
        GridPane.setConstraints(unboundedMapDominantGenotype, 0, 3, 2, 1);

        Label boundedMapDominantGenotypeLabel = new Label("Dominant genotype");
        GridPane.setConstraints(boundedMapDominantGenotypeLabel, 2, 2, 2, 1);
        Label boundedMapDominantGenotype = boundedMapDominantGenotypeHandler.getGenotypeLabel();
        GridPane.setConstraints(boundedMapDominantGenotype, 2, 3, 2, 1);

        Label unboundedMapGraphsLabel = new Label("Graphs");
        GridPane.setConstraints(unboundedMapGraphsLabel, 0, 4, 2, 1);
        LineChart<Number, Number> unboundedMapFirstGraph = unboundedMapGraphsHandler.getFirstGraph();
        GridPane.setConstraints(unboundedMapFirstGraph, 0,5);
        LineChart<Number, Number> unboundedMapSecondGraph = unboundedMapGraphsHandler.getSecondGraph();
        GridPane.setConstraints(unboundedMapSecondGraph, 1,5);
        LineChart<Number, Number> unboundedMapThirdGraph = unboundedMapGraphsHandler.getThirdGraph();
        GridPane.setConstraints(unboundedMapThirdGraph, 0,6);
        LineChart<Number, Number> unboundedMapForthGraph = unboundedMapGraphsHandler.getForthGraph();
        GridPane.setConstraints(unboundedMapForthGraph, 1,6);

        Label boundedMapGraphsLabel = new Label("Graphs");
        GridPane.setConstraints(boundedMapGraphsLabel, 2, 4, 2, 1);
        LineChart<Number, Number> boundedMapFirstGraph = boundedMapGraphsHandler.getFirstGraph();
        GridPane.setConstraints(boundedMapFirstGraph, 2,5);
        LineChart<Number, Number> boundedMapSecondGraph = boundedMapGraphsHandler.getSecondGraph();
        GridPane.setConstraints(boundedMapSecondGraph, 3,5);
        LineChart<Number, Number> boundedMapThirdGraph = boundedMapGraphsHandler.getThirdGraph();
        GridPane.setConstraints(boundedMapThirdGraph, 2,6);
        LineChart<Number, Number> boundedMapForthGraph = boundedMapGraphsHandler.getForthGraph();
        GridPane.setConstraints(boundedMapForthGraph, 3,6);

        Button unboundedMapStartStopButton = new Button("Pause");
        GridPane.setConstraints(unboundedMapStartStopButton, 0, 7, 2, 1);
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

        Label unboundedMapWhenPausedLabel = new Label("Things you can do when simulation's paused:");
        GridPane.setConstraints(unboundedMapWhenPausedLabel, 0, 8, 2, 1);
        Button unboundedMapSaveButton = new Button("Save");
        GridPane.setConstraints(unboundedMapSaveButton, 0, 9, 2,1);
        unboundedMapSaveButton.setMinSize(70, 30);
        unboundedMapSaveButton.setOnAction(click -> {
            if (!this.unboundedMap.isRunning()){
                AlertHandler alertHandler = new AlertHandler(this.unboundedMap);
                alertHandler.handleSave();
                try {
                    this.unboundedMap.saveFile("src/main/resources/UnboundedMapData.csv");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        Button unboundedMapShowAnimalsWithDominantGenotypeButton = new Button("Show Animals With Dominant Genotype");
        GridPane.setConstraints(unboundedMapShowAnimalsWithDominantGenotypeButton, 0, 10,2,1);
        unboundedMapShowAnimalsWithDominantGenotypeButton.setMinSize(70, 30);
        unboundedMapShowAnimalsWithDominantGenotypeButton.setOnAction(click -> {
            if (!this.unboundedMap.isRunning()){
                AlertHandler alertHandler = new AlertHandler(this.unboundedMap);
                alertHandler.handleAllAnimalsWithDominantGenotype(this.unboundedMap.getAnimalsWithDominantGenotype());
            }
        });

        Label boundedMapWhenPausedLabel = new Label("Things you can do when simulation's paused:");
        GridPane.setConstraints(boundedMapWhenPausedLabel, 2, 8, 2, 1);
        Button boundedMapSaveButton = new Button("Save");
        GridPane.setConstraints(boundedMapSaveButton, 2, 9,2,1);
        boundedMapSaveButton.setMinSize(70, 30);
        boundedMapSaveButton.setOnAction(click -> {
            if (!this.boundedMap.isRunning()) {
                AlertHandler alertHandler = new AlertHandler(this.boundedMap);
                alertHandler.handleSave();
                try {
                    this.boundedMap.saveFile("src/main/resources/BoundedMapData.csv");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        Button boundedMapShowAnimalsWithDominantGenotypeButton = new Button("Show Animals With Dominant Genotype");
        GridPane.setConstraints(boundedMapShowAnimalsWithDominantGenotypeButton, 2, 10,2,1);
        boundedMapShowAnimalsWithDominantGenotypeButton.setMinSize(70, 30);
        boundedMapShowAnimalsWithDominantGenotypeButton.setOnAction(click -> {
            if (!this.boundedMap.isRunning()){
                AlertHandler alertHandler = new AlertHandler(this.boundedMap);
                alertHandler.handleAllAnimalsWithDominantGenotype(this.boundedMap.getAnimalsWithDominantGenotype());
            }
        });

        Label unboundedMapTrackerStatusLabel = unboundedMapTracker.getTrackingStatusLabel();
        GridPane.setConstraints(unboundedMapTrackerStatusLabel, 1,7);
        Label unboundedMapTrackerNumberOfChildrenLabel = unboundedMapTracker.getNumberOfChildrenLabel();
        GridPane.setConstraints(unboundedMapTrackerNumberOfChildrenLabel, 1,8);
        Label unboundedMapTrackerNumberOfDescendantsLabel = unboundedMapTracker.getNumberOfDescendantsLabel();
        GridPane.setConstraints(unboundedMapTrackerNumberOfDescendantsLabel, 1,9);
        Label unboundedMapEraOfDeathLabel = unboundedMapTracker.getEraOfDeathLabel();
        GridPane.setConstraints(unboundedMapEraOfDeathLabel, 1,10);


        Label boundedMapTrackerStatusLabel = boundedMapTracker.getTrackingStatusLabel();
        GridPane.setConstraints(boundedMapTrackerStatusLabel, 3,7);
        Label boundedMapTrackerNumberOfChildrenLabel = boundedMapTracker.getNumberOfChildrenLabel();
        GridPane.setConstraints(boundedMapTrackerNumberOfChildrenLabel, 3,8);
        Label boundedMapTrackerNumberOfDescendantsLabel = boundedMapTracker.getNumberOfDescendantsLabel();
        GridPane.setConstraints(boundedMapTrackerNumberOfDescendantsLabel, 3,9);
        Label boundedMapEraOfDeathLabel = boundedMapTracker.getEraOfDeathLabel();
        GridPane.setConstraints(boundedMapEraOfDeathLabel, 3,10);



        grid.getChildren().addAll(unboundedMapLabel, unboundedMapGridPane,
                boundedMapLabel, boundedMapGridPane,
                unboundedMapDominantGenotypeLabel, unboundedMapDominantGenotype,
                boundedMapDominantGenotypeLabel, boundedMapDominantGenotype,
                unboundedMapGraphsLabel, unboundedMapFirstGraph, unboundedMapSecondGraph,
                unboundedMapThirdGraph, unboundedMapForthGraph,
                boundedMapGraphsLabel, boundedMapFirstGraph, boundedMapSecondGraph,
                boundedMapThirdGraph, boundedMapForthGraph,
                unboundedMapStartStopButton, boundedMapStartStopButton,
                unboundedMapWhenPausedLabel, unboundedMapSaveButton,
                unboundedMapShowAnimalsWithDominantGenotypeButton,
                boundedMapWhenPausedLabel, boundedMapSaveButton,
                boundedMapShowAnimalsWithDominantGenotypeButton,
                unboundedMapTrackerStatusLabel, unboundedMapTrackerNumberOfChildrenLabel,
                unboundedMapTrackerNumberOfDescendantsLabel, unboundedMapEraOfDeathLabel,
                boundedMapTrackerStatusLabel, boundedMapTrackerNumberOfChildrenLabel,
                boundedMapTrackerNumberOfDescendantsLabel, boundedMapEraOfDeathLabel);

        grid.setAlignment(Pos.CENTER);

        Scene animationScene = new Scene(grid, 1200, 1500);
        this.window.setScene(animationScene);
        this.window.setMaximized(true);

        Thread unboundedMapThread = new Thread(new ThreadedSimulationEngine(
                this.unboundedMap, unboundedMapGrid,
                unboundedMapDominantGenotypeHandler, unboundedMapGraphsHandler, unboundedMapTracker)
        );

        Thread boundedMapThread = new Thread(new ThreadedSimulationEngine(
                this.boundedMap, boundedMapGrid,
                boundedMapDominantGenotypeHandler, boundedMapGraphsHandler, boundedMapTracker)
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
