package andriispuzzle.puzzle;

import java.util.Random;

public class PuzzlePieceConnection {

    private final PuzzlePiece inPuzzlePiece;

    private final PuzzlePiece outPuzzlePiece;

    private int id;

    public PuzzlePieceConnection(PuzzlePiece[] pieces) {
        Random rn = new Random();
        int inPieceIndex;
        int outPieceIndex;

        inPieceIndex = rn.nextInt(2);
        outPieceIndex = inPieceIndex == 0 ? 1 : 0;
        this.inPuzzlePiece = pieces[inPieceIndex];
        this.outPuzzlePiece = pieces[outPieceIndex];
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
