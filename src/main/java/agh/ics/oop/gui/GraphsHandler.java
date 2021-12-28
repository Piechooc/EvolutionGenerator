package agh.ics.oop.gui;

import agh.ics.oop.AbstractWorldMap;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.GridPane;

public class GraphsHandler {
    private final AbstractWorldMap map;
    private final GridPane grid = new GridPane();
    private final LineChart<Number, Number> numberOfElementsGraph;
    private final LineChart<Number, Number> averageEnergyGraph;
    private final LineChart<Number, Number> averageLifespanGraph;
    private final LineChart<Number, Number> averageBabiesGraph;
    private final XYChart.Series<Number, Number> numberOfAnimalSeries;
    private final XYChart.Series<Number, Number> numberOfGrassSeries;
    private final XYChart.Series<Number, Number> averageEnergySeries;
    private final XYChart.Series<Number, Number> averageLifespanSeries;
    private final XYChart.Series<Number, Number> averageBabiesSeries;


    public GraphsHandler(AbstractWorldMap map) {
        this.map = map;

        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();

        this.numberOfElementsGraph = new LineChart<>(xAxis, yAxis);
        this.averageEnergyGraph = new LineChart<>(xAxis, yAxis);
        this.averageLifespanGraph = new LineChart<>(xAxis, yAxis);
        this.averageBabiesGraph = new LineChart<>(xAxis, yAxis);

        this.numberOfAnimalSeries = new XYChart.Series<>();
        this.numberOfAnimalSeries.setName("Number of animals");
        this.numberOfGrassSeries = new XYChart.Series<>();
        this.numberOfGrassSeries.setName("Number of grass");
        this.averageEnergySeries = new XYChart.Series<>();
        this.averageEnergySeries.setName("Average energy");
        this.averageLifespanSeries = new XYChart.Series<>();
        this.averageLifespanSeries.setName("Average lifespan");
        this.averageBabiesSeries = new XYChart.Series<>();
        this.averageBabiesSeries.setName("Average number of babies");
    }

    public GridPane getGraphs() {
        this.numberOfElementsGraph.getData().add(this.numberOfAnimalSeries);
        this.numberOfElementsGraph.getData().add(this.numberOfGrassSeries);
        this.numberOfElementsGraph.setCreateSymbols(false);
        this.numberOfElementsGraph.setMaxSize(50, 50);
        GridPane.setConstraints(this.numberOfElementsGraph, 0, 0);
        GridPane.setHalignment(this.numberOfElementsGraph, HPos.CENTER);
        GridPane.setValignment(this.numberOfElementsGraph, VPos.CENTER);

        this.averageEnergyGraph.getData().add(this.averageEnergySeries);
        this.averageEnergyGraph.setCreateSymbols(false);
        this.averageEnergyGraph.setMaxSize(50, 50);
        GridPane.setConstraints(this.averageEnergyGraph, 1, 0);
        GridPane.setHalignment(this.averageEnergyGraph, HPos.CENTER);
        GridPane.setValignment(this.averageEnergyGraph, VPos.CENTER);

        this.averageLifespanGraph.getData().add(this.averageLifespanSeries);
        this.averageLifespanGraph.setCreateSymbols(false);
        this.averageLifespanGraph.setMaxSize(100, 100);
        GridPane.setConstraints(this.averageLifespanGraph, 0, 1);
        GridPane.setHalignment(this.averageLifespanGraph, HPos.CENTER);
        GridPane.setValignment(this.averageLifespanGraph, VPos.CENTER);

        this.averageBabiesGraph.getData().add(this.averageBabiesSeries);
        this.averageBabiesGraph.setCreateSymbols(false);
        this.averageBabiesGraph.setMaxSize(100, 100);
        GridPane.setConstraints(this.averageBabiesGraph, 1, 1);
        GridPane.setHalignment(this.averageBabiesGraph, HPos.CENTER);
        GridPane.setValignment(this.averageBabiesGraph, VPos.CENTER);

        this.grid.setPadding(new Insets(10, 10, 10, 10));
        this.grid.setVgap(10);
        this.grid.setHgap(10);
        this.grid.getChildren().addAll(this.numberOfElementsGraph,
                this.averageEnergyGraph, this.averageLifespanGraph, this.averageBabiesGraph);

        return this.grid;
    }

    public void setGraphs() {
        Platform.runLater(() -> {
            this.numberOfAnimalSeries.getData().add(
                    new XYChart.Data<>(this.map.getEra(), this.map.getNumberOfAnimals()));
            this.numberOfGrassSeries.getData().add(
                    new XYChart.Data<>(this.map.getEra(), this.map.getNumberOfPlants()));
            this.averageEnergySeries.getData().add(
                    new XYChart.Data<>(this.map.getEra(), this.map.getAverageEnergy()));
            this.averageLifespanSeries.getData().add(
                    new XYChart.Data<>(this.map.getEra(), this.map.getAverageLifespan()));
            this.averageBabiesSeries.getData().add(
                    new XYChart.Data<>(this.map.getEra(), this.map.getAverageNumberOfBabies()));
        });
    }
}
