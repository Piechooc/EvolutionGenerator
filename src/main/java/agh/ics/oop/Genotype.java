package agh.ics.oop;

import java.util.Arrays;

public class Genotype {

    private final int[] genotype;
    private final int size;
    private final int numberOfDifferentGenes;

    public Genotype(int numberOfDifferentGenes, int size) {
        this.genotype = new int[size];
        this.size = size;
        this.numberOfDifferentGenes = numberOfDifferentGenes;
        fillGenotype();

    }

    public Genotype(Genotype strongerParent, int energyOfStrongerParent,
                    Genotype weakerParent, int energyOfWeakerParent) {
        this.size = strongerParent.getSize();
        this.numberOfDifferentGenes = strongerParent.getNumberOfDifferentGenes();
        this.genotype = new int[this.size];

        int[] genotypeOfStrongerParent = strongerParent.getGenotype();
        int[] genotypeOfWeakerParent = weakerParent.getGenotype();
        int wholeEnergy = energyOfStrongerParent + energyOfWeakerParent;
        float percentageOfGenotypeFromStrongerParent = (float) energyOfStrongerParent / wholeEnergy;
        int placeToDivideIfStrongerOnLeftSide = (int) (this.size * percentageOfGenotypeFromStrongerParent);
        int placeToDivideIfStrongerOnRightSide = (int) (this.size * (1 - percentageOfGenotypeFromStrongerParent));

        int strongerOnTheLeft = (int) (Math.random() * 2);
        if (strongerOnTheLeft == 1) {
            System.arraycopy(genotypeOfStrongerParent, 0,
                    this.genotype, 0, placeToDivideIfStrongerOnLeftSide);
            System.arraycopy(genotypeOfWeakerParent, placeToDivideIfStrongerOnLeftSide,
                    this.genotype, placeToDivideIfStrongerOnLeftSide,
                    this.size - placeToDivideIfStrongerOnLeftSide);
        } else {
            System.arraycopy(genotypeOfWeakerParent, 0,
                    this.genotype, 0, placeToDivideIfStrongerOnRightSide);
            System.arraycopy(genotypeOfStrongerParent, placeToDivideIfStrongerOnRightSide,
                    this.genotype, placeToDivideIfStrongerOnRightSide,
                    this.size - placeToDivideIfStrongerOnRightSide);
        }
        Arrays.sort(this.genotype);
    }

    public void fillGenotype() {
        for (int i = 0; i < this.size; i++) {
            this.genotype[i] = (int) (Math.random() * (this.numberOfDifferentGenes));
        }
        Arrays.sort(this.genotype);
    }

    public int getSize() {
        return this.size;
    }

    public int getNumberOfDifferentGenes() {
        return this.numberOfDifferentGenes;
    }

    public int[] getGenotype() {
        return this.genotype;
    }
}
