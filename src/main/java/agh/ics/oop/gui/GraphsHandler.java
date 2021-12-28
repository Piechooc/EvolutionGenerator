package agh.ics.oop.gui;

import agh.ics.oop.AbstractWorldMap;
import javafx.application.Platform;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

public class GraphsHandler {
    private final AbstractWorldMap map;
    private LineChart<Number, Number> numberOfElementsGraph;
    private LineChart<Number, Number> averageEnergyGraph;
    private LineChart<Number, Number> averageLifespanGraph;
    private LineChart<Number, Number> averageBabiesGraph;
    private XYChart.Series<Number, Number> numberOfAnimalSeries;
    private XYChart.Series<Number, Number> numberOfGrassSeries;
    private XYChart.Series<Number, Number> averageEnergySeries;
    private XYChart.Series<Number, Number> averageLifespanSeries;
    private XYChart.Series<Number, Number> averageBabiesSeries;


    public GraphsHandler(AbstractWorldMap map) {
        this.map = map;

        makeNumberOfElementsGraph();
        makeAverageEnergyGraph();
        makeAverageLifespanGraph();
        makeAverageBabiesGraph();
    }

    public void makeNumberOfElementsGraph() {
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setAnimated(false);
        yAxis.setAnimated(true);
        this.numberOfElementsGraph = new LineChart<>(xAxis, yAxis);
        this.numberOfElementsGraph.setAnimated(true);
        this.numberOfAnimalSeries = new XYChart.Series<>();
        this.numberOfAnimalSeries.setName("Number of animals");
        this.numberOfGrassSeries = new XYChart.Series<>();
        this.numberOfGrassSeries.setName("Number of grass");
    }

    public void makeAverageEnergyGraph() {
        NumberAxis xAxis2 = new NumberAxis();
        NumberAxis yAxis2 = new NumberAxis();
        xAxis2.setAnimated(false);
        yAxis2.setAnimated(true);
        this.averageEnergyGraph = new LineChart<>(xAxis2, yAxis2);
        this.averageEnergyGraph.setAnimated(true);
        this.averageEnergySeries = new XYChart.Series<>();
        this.averageEnergySeries.setName("Average energy");
    }

    public void makeAverageLifespanGraph() {
        NumberAxis xAxis3 = new NumberAxis();
        NumberAxis yAxis3 = new NumberAxis();
        xAxis3.setAnimated(false);
        yAxis3.setAnimated(true);
        this.averageLifespanGraph = new LineChart<>(xAxis3, yAxis3);
        this.averageLifespanGraph.setAnimated(true);
        this.averageLifespanSeries = new XYChart.Series<>();
        this.averageLifespanSeries.setName("Average lifespan");
    }

    public void makeAverageBabiesGraph() {
        NumberAxis xAxis4 = new NumberAxis();
        NumberAxis yAxis4 = new NumberAxis();
        xAxis4.setAnimated(false);
        yAxis4.setAnimated(true);
        this.averageBabiesGraph = new LineChart<>(xAxis4, yAxis4);
        this.averageBabiesGraph.setAnimated(true);
        this.averageBabiesSeries = new XYChart.Series<>();
        this.averageBabiesSeries.setName("Average number of babies");
    }

    public LineChart<Number, Number> getFirstGraph() {
        this.numberOfElementsGraph.getData().add(this.numberOfAnimalSeries);
        this.numberOfElementsGraph.getData().add(this.numberOfGrassSeries);
        this.numberOfElementsGraph.setCreateSymbols(false);
        this.numberOfElementsGraph.setMaxSize(350, 200);
        this.numberOfElementsGraph.setMinSize(150, 75);
        return this.numberOfElementsGraph;
    }

    public LineChart<Number, Number> getSecondGraph() {
        this.averageEnergyGraph.getData().add(this.averageEnergySeries);
        this.averageEnergyGraph.setCreateSymbols(false);
        this.averageEnergyGraph.setMaxSize(350, 200);
        this.averageEnergyGraph.setMinSize(150, 75);
        return this.averageEnergyGraph;
    }

    public LineChart<Number, Number> getThirdGraph() {
        this.averageLifespanGraph.getData().add(this.averageLifespanSeries);
        this.averageLifespanGraph.setCreateSymbols(false);
        this.averageLifespanGraph.setMaxSize(350, 200);
        this.averageLifespanGraph.setMinSize(150, 75);
        return this.averageLifespanGraph;
    }

    public LineChart<Number, Number> getForthGraph() {
        this.averageBabiesGraph.getData().add(this.averageBabiesSeries);
        this.averageBabiesGraph.setCreateSymbols(false);
        this.averageBabiesGraph.setMaxSize(350, 200);
        this.averageBabiesGraph.setMinSize(150, 75);
        return this.averageBabiesGraph;
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
