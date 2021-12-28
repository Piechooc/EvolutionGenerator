package agh.ics.oop;

import agh.ics.oop.gui.AlertHandler;
import agh.ics.oop.gui.DominantGenotypeHandler;
import agh.ics.oop.gui.GraphsHandler;
import agh.ics.oop.gui.GridHandler;

public class ThreadedSimulationEngine  implements IEngine, Runnable{
    private final AbstractWorldMap map;
    private boolean end;
    private final AlertHandler alertHandler;
    private final GridHandler gridHandler;
    private final DominantGenotypeHandler dominantGenotypeHandler;
    private final GraphsHandler graphsHandler;

    public ThreadedSimulationEngine(AbstractWorldMap map, GridHandler gridHandler,
                                    DominantGenotypeHandler dominantGenotypeHandler,
                                    GraphsHandler graphsHandler) {
        this.map = map;
        this.end = false;
        this.map.placeAnimals();
        this.map.plantGrass();
        this.map.updateStatistics();
        this.alertHandler = new AlertHandler(map);
        this.gridHandler = gridHandler;
        this.dominantGenotypeHandler = dominantGenotypeHandler;
        this.graphsHandler = graphsHandler;
    }

    @Override
    public void run() {
        int loop = 0;
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
                this.gridHandler.setGrid();
                this.dominantGenotypeHandler.setGenotypeLabel();
                this.graphsHandler.setGraphs();
                this.map.nextEra();

                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (this.map.getAllAnimals().size() == 0) {
                    this.end = true;
                }
                System.out.println(loop);
                loop++;
            } else {
                Thread.onSpinWait();
                }
            }
        }
    }


