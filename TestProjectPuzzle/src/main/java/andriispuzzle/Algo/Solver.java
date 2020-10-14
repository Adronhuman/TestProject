package andriispuzzle.Algo;

import java.awt.image.BufferedImage;

public interface Solver {
    BufferedImage solve(String filePath,int rowCount,int columnCount);
}
