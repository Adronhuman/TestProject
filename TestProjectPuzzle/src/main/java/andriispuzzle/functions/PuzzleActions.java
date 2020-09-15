package andriispuzzle.functions;

import andriispuzzle.CreatePuzzleFile;
import andriispuzzle.PuzzleApp;
import andriispuzzle.puzzle.*;
import andriispuzzle.settings.SettingsActions;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class PuzzleActions {

    private static PuzzleActions instance;
    private Puzzle puzzle;

    private PuzzleActions() {
    }

    public static PuzzleActions getInstance() {
        if (instance == null) {
            instance = new PuzzleActions();
        }
        return instance;
    }

    public void newPuzzle(Image img, String imageName) {
        BufferedImage image = ImageFunc.BufferedImageOf(img);
        int rowCount, columnCount;
        int numberOfPieces = SettingsActions.getInstance().getPuzzlePieceNumber();

        int puzzleAreaSize = image.getWidth() * image.getHeight();
        int PuzzlePieceHeight = (int) (Math.sqrt(puzzleAreaSize / (double) numberOfPieces));
        int PuzzlePieceWidth = PuzzlePieceHeight;

        rowCount = image.getHeight() / PuzzlePieceHeight;
        columnCount = image.getWidth() / PuzzlePieceWidth;

        Dimension pieceSize = SettingsActions.getInstance().getPuzzlePieceSize(
                image.getHeight(), image.getWidth(),
                rowCount, columnCount);

        if (puzzle != null) {
            puzzle.destroy();
        }
        puzzle = new Puzzle(image, rowCount, columnCount, pieceSize.width, pieceSize.height);
        puzzle.setImageType(imageName.split("\\.")[1]);

        PuzzleApp.getInstance().getPuzzleWindow().setNewPuzzle(puzzle);
        shufflePuzzlePieces();
        CreatePuzzleFile.saveToFile(puzzle);
    }

    public void newPuzzle(File imageFile) throws IOException {
        newPuzzle(ImageIO.read(imageFile), imageFile.getName());
    }

    public void restartPuzzle() {
        if (puzzle == null) {
            return;
        }
        newPuzzle(puzzle.getImage(), "filename." + puzzle.getImageType());
    }

    public void shufflePuzzlePieces() {
        if (puzzle != null) {
            puzzle.shufflePuzzlePieces();
        }
    }

    public void ConnectPuzzlePieceGroup(PuzzlePieceGroup PuzzlePieceGroup) {
        for (PuzzlePieceGroup otherGroup : puzzle.getPuzzlePieceGroups()) {
            if (!otherGroup.equals(PuzzlePieceGroup)) {
                PuzzlePieceConnection connection = null;

                for (Direction direction : Direction.values()) {
                    List<PuzzlePieceConnection> connectionsThisGroup = PuzzlePieceGroup.getPuzzlePieceConnectionsInPosition(direction);
                    List<PuzzlePieceConnection> connectionsOtherGroup = otherGroup.getPuzzlePieceConnectionsInPosition(direction.getOpposite());

                    for (PuzzlePieceConnection thisConnection : connectionsThisGroup) {
                        for (PuzzlePieceConnection otherConnection : connectionsOtherGroup) {
                            if (thisConnection != null && thisConnection == otherConnection) {
                                connection = thisConnection;
                                break;
                            }
                        }
                        if (connection != null) {
                            break;
                        }
                    }
                    if (connection != null) {
                        break;
                    }
                }

                if (connection == null) {
                    continue;
                }

                PuzzlePiece piece1;
                PuzzlePiece otherPuzzlePiece;

                if (PuzzlePieceGroup.isPuzzlePieceContained(connection.getInPuzzlePiece())) {
                    piece1 = connection.getInPuzzlePiece();
                } else {
                    piece1 = connection.getOutPuzzlePiece();
                }
                if (otherGroup.isPuzzlePieceContained(connection.getInPuzzlePiece())) {
                    otherPuzzlePiece = connection.getInPuzzlePiece();
                } else {
                    otherPuzzlePiece = connection.getOutPuzzlePiece();
                }

                Direction direction = null;

                for (Direction positionToTest : Direction.values()) {
                    if (connection == piece1.getConnectorForDirection(positionToTest)) {
                        direction = positionToTest;
                        break;
                    }
                }

                if (!isPuzzlePieceNearOtherPieceInDirection(piece1, otherPuzzlePiece, direction)) {
                    continue;
                }

                otherGroup.addFromPuzzlePieceGroup(PuzzlePieceGroup, connection);
                PuzzlePieceGroup.destroy();
                PuzzlePieceGroup = otherGroup;

                PuzzleApp.getInstance().getPuzzleWindow().bringToFront(otherGroup);
            }
        }
    }


    private boolean isPuzzlePieceNearOtherPieceInDirection(PuzzlePiece piece1, PuzzlePiece otherPiece, Direction direction) {
        int pieceWidth = SettingsActions.getInstance().getPuzzlePieceSize().width;
        int pieceHeight = SettingsActions.getInstance().getPuzzlePieceSize().height;

        int possibleGroupOffsetX = pieceWidth * SettingsActions.getInstance().getPuzzlePieceSnapDistancePercent() / 100;
        int possibleGroupOffsetY = pieceHeight * SettingsActions.getInstance().getPuzzlePieceSnapDistancePercent() / 100;

        PuzzlePieceGroup PuzzlePieceGroup = piece1.getPuzzlePieceGroup();
        PuzzlePieceGroup otherGroup = otherPiece.getPuzzlePieceGroup();

        int xPiece1, yPiece1, xOtherPiece, yOtherPiece;

        xPiece1 = PuzzlePieceGroup.getX() + PuzzlePieceGroup.getXPositionOfPieceInGroup(piece1) * pieceWidth;
        yPiece1 = PuzzlePieceGroup.getY() + PuzzlePieceGroup.getYPositionOfPieceInGroup(piece1) * pieceHeight;
        xOtherPiece = otherGroup.getX() + otherGroup.getXPositionOfPieceInGroup(otherPiece) * pieceWidth;
        yOtherPiece = otherGroup.getY() + otherGroup.getYPositionOfPieceInGroup(otherPiece) * pieceHeight;

        int xOtherPieceExpected = xPiece1;
        int yOtherPieceExpected = yPiece1;

        switch (direction) {
            case LEFT:
                xOtherPieceExpected -= pieceWidth;
                break;
            case RIGHT:
                xOtherPieceExpected += pieceWidth;
                break;
            case TOP:
                yOtherPieceExpected -= pieceHeight;
                break;
            case BOTTOM:
                yOtherPieceExpected += pieceHeight;
                break;
        }

        boolean isNear = false;

        if (xOtherPiece - possibleGroupOffsetX < xOtherPieceExpected
                && xOtherPieceExpected < xOtherPiece + possibleGroupOffsetX
                && yOtherPiece - possibleGroupOffsetY < yOtherPieceExpected
                && yOtherPieceExpected < yOtherPiece + possibleGroupOffsetY) {
            isNear = true;
        }
        return isNear;
    }

    public Puzzle getPuzzle() {
        return puzzle;
    }

    public void setPuzzle(Puzzle puzzle) {
        this.puzzle = puzzle;
        PuzzleApp.getInstance().getPuzzleWindow().setNewPuzzle(puzzle);
    }

    public int getPuzzleHeight() {
        return puzzle.getImage().getHeight(null);
    }

    public int getPuzzleWidth() {
        return puzzle.getImage().getWidth(null);
    }

    public int getPuzzlePieceColumnCount() {
        return puzzle.getColumnCount();
    }

    public Image getPuzzlePieceImage() {
        return puzzle.getImage();
    }

    public int getPuzzlePieceRowCount() {
        return puzzle.getRowCount();
    }

    public boolean isPuzzleActive() {
        return puzzle != null;
    }

}
