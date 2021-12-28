package agh.ics.oop;

public class Grass extends AbstractWorldMapElement implements IMapElement{
    public Grass(Vector2d position) {
        this.position = position;
    }

    @Override
    public Vector2d getPosition() {
        return this.position;
    }

    @Override
    public String toString() {
        return "*";
    }
}
