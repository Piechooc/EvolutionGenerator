package agh.ics.oop;

import agh.ics.oop.gui.*;

public class ThreadedSimulationEngine  implements IEngine, Runnable{
    private final AbstractWorldMap map;
    private boolean end;
    private final AlertHandler alertHandler;
    private final GridHandler gridHandler;
    private final DominantGenotypeHandler dominantGenotypeHandler;
    private final GraphsHandler graphsHandler;
    private final Tracker tracker;

    public ThreadedSimulationEngine(AbstractWorldMap map, GridHandler gridHandler,
                                    DominantGenotypeHandler dominantGenotypeHandler,
                                    GraphsHandler graphsHandler, Tracker tracker) {
        this.map = map;
        this.end = false;
        this.map.placeAnimals();
        this.map.plantGrass();
        this.map.updateStatistics();
        this.alertHandler = new AlertHandler(map);
        this.gridHandler = gridHandler;
        this.dominantGenotypeHandler = dominantGenotypeHandler;
        this.graphsHandler = graphsHandler;
        this.tracker = tracker;
    }

    @Override
    public void run() {
        while(true) {
            if (this.map.isRunning() && !end) {
                this.map.removeTheDead();
                if (this.map.isMagic() && this.map.getMagicLeft() > 0 && this.map.getAllAnimals().size() == 5) {
                    this.map.letsDoMagic();
                    this.alertHandler.handleMagic();
                }
                this.map.moveAllAnimals();
                this.map.eat();
                this.map.findParents();
                this.map.plantGrass();
                this.map.updateStatistics();
                Animal trackedAnimal = this.gridHandler.setGrid();
                if (trackedAnimal != null) {
                    this.tracker.takeTrackedAnimal(trackedAnimal);
                }
                this.dominantGenotypeHandler.setGenotypeLabel();
                this.graphsHandler.setGraphs();
                this.tracker.setTracker();
                this.map.updateDataLines();
                this.map.nextEra();

                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (this.map.getAllAnimals().size() == 0) {
                    this.end = true;
                }
            } else {
                Thread.onSpinWait();
                }
            }
        }
    }


