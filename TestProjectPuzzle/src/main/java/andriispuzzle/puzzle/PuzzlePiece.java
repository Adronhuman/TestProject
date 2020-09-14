package andriispuzzle.puzzle;

import andriispuzzle.functions.ImageFunc;

import java.awt.image.BufferedImage;

public class PuzzlePiece {

    private final BufferedImage image;

    private PuzzlePieceGroup group;

    private final PuzzlePieceConnection[] connectors = new PuzzlePieceConnection[4];

    public PuzzlePiece(BufferedImage img) {
        this.image = img;
    }


    boolean createConnectorToPiece(PuzzlePiece otherPiece, Direction position) {
        if (connectors[position.intValue()] != null
                || connectors[position.getOpposite().intValue()] != null) {
            return false;
        }

        PuzzlePieceConnection connection = new PuzzlePieceConnection(new PuzzlePiece[]{this, otherPiece});

        this.connectors[position.intValue()] = connection;
        otherPiece.connectors[position.getOpposite().intValue()] = connection;

        return false;
    }

    public PuzzlePieceConnection getConnectorForDirection(Direction direction) {
        return connectors[direction.intValue()];
    }

    public BufferedImage getImage() {
        return image;
    }

    public boolean isInPieceInDirection(Direction direction) {
        if (connectors[direction.intValue()] == null) {
            return false;
        }
        return connectors[direction.intValue()].getInPuzzlePiece() == this;
    }

    public boolean isOutPieceInDirection(Direction direction) {
        if (connectors[direction.intValue()] == null) {
            return false;
        }
        return connectors[direction.intValue()].getOutPuzzlePiece() == this;
    }

    public PuzzlePieceGroup getPuzzlePieceGroup() {
        return group;
    }

    void setPuzzlePieceGroup(PuzzlePieceGroup group) {
        this.group = group;
    }

}
