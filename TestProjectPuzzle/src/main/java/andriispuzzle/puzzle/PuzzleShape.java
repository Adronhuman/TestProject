package andriispuzzle.puzzle;

import java.awt.geom.GeneralPath;
import java.awt.geom.Path2D;

public class PuzzleShape {

    private Path2D shape;

    public Path2D constructShape(int width,int height) {
        GeneralPath shape = new GeneralPath();
        shape.moveTo(0,0);
        shape.lineTo(0, height);
//        shape.moveTo(0,100);

        shape.lineTo(height,width);
//        shape.moveTo(100,100);

        shape.lineTo(width,0);
//        shape.moveTo(100,0);

        shape.lineTo(0,0);
//        shape.moveTo(0,0);

        return shape;
    }

    public void setShape(Path2D shape) {
        this.shape = shape;
    }
}
