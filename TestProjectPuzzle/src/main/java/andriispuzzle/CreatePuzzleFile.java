package andriispuzzle;

import andriispuzzle.puzzle.Puzzle;
import andriispuzzle.puzzle.PuzzlePiece;
import org.codehaus.plexus.util.FileUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CreatePuzzleFile {

    public static void saveToFile(Puzzle puzzle) {
        PuzzlePiece[][] puzzlePieces = puzzle.getPuzzlePieces();
        Random random = new Random();
        int imageIndex =0;
        List<Integer> indexes = new ArrayList<>();
        try {
            FileUtils.cleanDirectory("puzzleRepo");
            System.out.println("cleaning repo");
        }catch (Exception exception){
            System.out.println(exception.getLocalizedMessage());
        }
        for(int i =0;i<puzzlePieces.length;i++){
            for(int j=0;j<puzzlePieces[i].length;j++){
                while(indexes.contains(Integer.valueOf(imageIndex))){
                    imageIndex = random.nextInt();
                }
                indexes.add(Integer.valueOf(imageIndex));
                BufferedImage pieceImage =  puzzlePieces[i][j].getImage();
                try {
                    ImageIO.write(pieceImage, puzzle.getImageType(), new File("puzzleRepo\\" + imageIndex + "." + puzzle.getImageType()));
                }catch (IOException exception){
                    System.out.println(exception.getLocalizedMessage());
                }
            }
        }
    }
}
