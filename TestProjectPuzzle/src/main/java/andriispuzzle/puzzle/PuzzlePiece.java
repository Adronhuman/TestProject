package andriispuzzle.puzzle;

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

        PuzzlePieceConnection connection = new PuzzlePieceConnection(this, otherPiece);

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


    public PuzzlePieceGroup getPuzzlePieceGroup() {
        return group;
    }

    void setPuzzlePieceGroup(PuzzlePieceGroup group) {
        this.group = group;
    }

}
