package andriispuzzle.puzzle;

import java.util.Random;

public class PuzzlePieceConnection {

    private final PuzzlePiece inPuzzlePiece;

    private final PuzzlePiece outPuzzlePiece;

    private int id;

    public PuzzlePieceConnection(PuzzlePiece thisPiece,PuzzlePiece other) {
        this.inPuzzlePiece = thisPiece;
        this.outPuzzlePiece = other;
    }


    public PuzzlePiece getInPuzzlePiece() {
        return inPuzzlePiece;
    }

    public PuzzlePiece getOutPuzzlePiece() {
        return outPuzzlePiece;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
