package andriispuzzle.puzzle;

import java.awt.geom.GeneralPath;
import java.awt.geom.Path2D;

public class Shape {

    private Path2D shape;

    public Shape() {
        this.shape = constructShape();
    }

    public Path2D constructShape() {
        GeneralPath shape = new GeneralPath();
        shape.moveTo(0, 37);
        shape.curveTo(2.5, 40, 5.5, 40, 8, 37);
        shape.curveTo(14.9, 24, 30, 31, 30, 48);
        shape.lineTo(30, 52);
        shape.curveTo(30, 69, 14.9, 76, 8, 63);
        shape.curveTo(5.5, 60, 2.5, 60, 0, 63);
        return shape;
    }

    public Path2D getShape() {
        return shape;
    }

    public void setShape(Path2D shape) {
        this.shape = shape;
    }
}
