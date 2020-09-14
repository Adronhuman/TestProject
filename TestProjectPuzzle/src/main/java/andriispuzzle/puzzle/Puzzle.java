package andriispuzzle.puzzle;

import andriispuzzle.PuzzleApp;
import andriispuzzle.settings.SettingsActions;
import andriispuzzle.functions.ImageFunc;

import java.awt.*;
import java.awt.geom.Area;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.*;

public class Puzzle {

    Map<Integer, PuzzlePieceConnection> puzzlePieceConnections;
    private final ArrayList<PuzzlePieceGroup> puzzlePiecesGroups;
    private final Image image;
    private String imageType;
    private int rowCount;
    private int columnCount;
    private final PuzzlePiece[][] puzzlePieces;

    public Puzzle(BufferedImage image, int rowCount, int columnCount, int pieceWidth, int pieceHeight) {
        this.image = image;
        this.rowCount = rowCount;
        this.columnCount = columnCount;
        puzzlePieces = new PuzzlePiece[rowCount][columnCount];

        puzzlePieceConnections = new HashMap<>();
        puzzlePiecesGroups = new ArrayList<>(rowCount * columnCount);
        for (int x = 0; x < rowCount; x++) {
            for (int y = 0; y < columnCount; y++) {
                BufferedImage img = new BufferedImage(image.getWidth() / columnCount, image.getHeight() / rowCount, image.getType());

                PuzzlePiece newPiece;
                Graphics2D gr = img.createGraphics();
                gr.drawImage(image,
                        0, 0,
                        img.getWidth(), img.getHeight(),
                        img.getWidth() * y, img.getHeight() * x,
                        img.getWidth() * y + img.getWidth(), img.getHeight() * x + img.getHeight(),
                        null);
                gr.dispose();

                newPiece = new PuzzlePiece(img);
                puzzlePieces[x][y] = newPiece;
                puzzlePiecesGroups.add(x * columnCount + y, new PuzzlePieceGroup(this, newPiece, pieceWidth * y, pieceHeight * x));

                PuzzlePieceConnection newConnection;

                if (x > 0) {
                    puzzlePieces[x][y].createConnectorToPiece(puzzlePieces[x - 1][y], Direction.TOP);
                    newConnection = puzzlePieces[x][y].getConnectorForDirection(Direction.TOP);
                    puzzlePieceConnections.put(newConnection.getId(), newConnection);
                }
                if (y > 0) {
                    puzzlePieces[x][y].createConnectorToPiece(puzzlePieces[x][y - 1], Direction.LEFT);
                    newConnection = puzzlePieces[x][y].getConnectorForDirection(Direction.LEFT);
                    puzzlePieceConnections.put(newConnection.getId(), newConnection);
                }
            }
        }
    }

    public void destroy() {
        puzzlePieceConnections.clear();
        puzzlePiecesGroups.clear();
    }


    void removePuzzlePieceGroup(PuzzlePieceGroup group) {
        puzzlePiecesGroups.remove(group);
    }

    public void shufflePuzzlePieces() {
        Rectangle screenBounds;
        Area screenArea = new Area(PuzzleApp.getInstance().getPuzzleWindow().getPuzzleAreaBounds());
        screenBounds = screenArea.getBounds();
        Random r = new Random();
        Dimension pieceSize = SettingsActions.getInstance().getPuzzlePieceSize();

        for (int i = 0; i < puzzlePiecesGroups.size(); i++) {
            PuzzlePieceGroup group = puzzlePiecesGroups.get(i);
            int newX, newY;
            do {
                newX = r.nextInt(screenBounds.width) + screenBounds.x;
                newY = r.nextInt(screenBounds.height) + screenBounds.y;
            } while (!screenArea.contains(newX, newY, pieceSize.getWidth() * group.getMaxPuzzlePiecesInXDirection(), pieceSize.height * group.getMaxPuzzlePiecesInYDirection()));
            group.setX(newX);
            group.setY(newY);
        }
    }

    public int getColumnCount() {
        return columnCount;
    }

    public void setColumnCount(int columnCount) {
        this.columnCount = columnCount;
    }

    public Image getImage() {
        return image;
    }

    PuzzlePieceConnection getPuzzlePieceConnectionWithId(int id) {
        return puzzlePieceConnections.get(id);
    }

    public int getRowCount() {
        return rowCount;
    }

    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
    }

    public List<PuzzlePieceGroup> getPuzzlePieceGroups() {
        return new ArrayList<>(puzzlePiecesGroups);
    }

    public PuzzlePiece[][] getPuzzlePieces() {
        return puzzlePieces;
    }

    public String getImageType() {
        return imageType;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }
}
