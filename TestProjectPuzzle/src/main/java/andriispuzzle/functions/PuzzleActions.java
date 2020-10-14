package andriispuzzle.functions;

import andriispuzzle.Algo.SolveFromFile;
import andriispuzzle.Algo.Solver;
import andriispuzzle.CreatePuzzleFile;
import andriispuzzle.PuzzleApp;
import andriispuzzle.Algo.SolveFromFileRecursive;
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

    public void solve(){
        Puzzle puzzle = this.getPuzzle();
        Solver solver = new SolveFromFileRecursive();
        try {
            ImageIO.write(solver.solve("puzzleRepo", puzzle.getRowCount(), puzzle.getColumnCount()), puzzle.getImageType(), new File("final\\kit." + puzzle.getImageType()));
        } catch (Exception ec) {
            System.out.println(ec.getLocalizedMessage());
        }
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

                PuzzlePiece piece;
                PuzzlePiece otherPiece;

                if (PuzzlePieceGroup.isPuzzlePieceContained(connection.getInPuzzlePiece())) {
                    piece = connection.getInPuzzlePiece();
                } else {
                    piece = connection.getOutPuzzlePiece();
                }
                if (otherGroup.isPuzzlePieceContained(connection.getInPuzzlePiece())) {
                    otherPiece = connection.getInPuzzlePiece();
                } else {
                    otherPiece = connection.getOutPuzzlePiece();
                }

                Direction direction = null;

                for (Direction positionToTest : Direction.values()) {
                    if (connection == piece.getConnectorForDirection(positionToTest)) {
                        direction = positionToTest;
                        break;
                    }
                }

                if (!isPuzzlePieceNearOtherPieceInDirection(piece, otherPiece, direction)) {
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

        int possibleMistakeX = pieceWidth * SettingsActions.getInstance().getPuzzlePieceSnapDistancePercent() / 100;
        int possibleMistakeY = pieceHeight * SettingsActions.getInstance().getPuzzlePieceSnapDistancePercent() / 100;

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

        if (Math.abs(xOtherPiece-xOtherPieceExpected) < possibleMistakeX
            && Math.abs(yOtherPiece-yOtherPieceExpected) < possibleMistakeY) {
            return true;
        }
        return false;
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
