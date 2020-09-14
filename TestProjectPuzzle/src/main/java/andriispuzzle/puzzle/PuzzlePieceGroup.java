package andriispuzzle.puzzle;

import andriispuzzle.PuzzleApp;
import andriispuzzle.settings.SettingsActions;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class PuzzlePieceGroup extends Observable {

    private ArrayList<ArrayList<PuzzlePiece>> puzzlePiecesList;

    private int x;

    private int y;

    private Puzzle puzzle;

    private int PuzzlePieceCount = 1;

    PuzzlePieceGroup(Puzzle puzzle, PuzzlePiece puzzlepiece, int x, int y) {
        this.puzzle = puzzle;
        this.x = x;
        this.y = y;
        this.puzzlePiecesList = new ArrayList<>();
        this.addPuzzlePieceAtPosition(puzzlepiece, 0, 0);
        puzzlepiece.setPuzzlePieceGroup(this);
    }

    public void addFromPuzzlePieceGroup(PuzzlePieceGroup otherGroup, PuzzlePieceConnection connection) {

        PuzzlePiece pieceThis = null;
        PuzzlePiece pieceOther = null;

        for (PuzzlePiece piece : this.getPuzzlePieces()) {
            if (piece == connection.getInPuzzlePiece() || piece == connection.getOutPuzzlePiece()) {
                pieceThis = piece;
            }
        }
        for (PuzzlePiece piece : otherGroup.getPuzzlePieces()) {
            if (piece == connection.getInPuzzlePiece()
                    || piece == connection.getOutPuzzlePiece()) {
                pieceOther = piece;
            }
        }

        Direction directionOfOtherGroup = null;
        for (Direction direction : Direction.values()) {
            if (pieceThis.getConnectorForDirection(direction) == connection) {
                directionOfOtherGroup = direction;
                break;
            }
        }

        int verticalPositionThis, horizontalPositionThis;
        int verticalPositionInOtherGroup, horizontalPositionInOtherGroup;

        verticalPositionThis = getYPositionOfPieceInGroup(pieceThis);
        horizontalPositionThis = getXPositionOfPieceInGroup(pieceThis);

        verticalPositionInOtherGroup = otherGroup.getYPositionOfPieceInGroup(pieceOther);
        horizontalPositionInOtherGroup = otherGroup.getXPositionOfPieceInGroup(pieceOther);

        int verticalPositionOther = verticalPositionThis;
        int horizontalPositionOther = horizontalPositionThis;

        switch (directionOfOtherGroup) {
            case LEFT:
                horizontalPositionOther -= 1;
                break;
            case RIGHT:
                horizontalPositionOther += 1;
                break;
            case TOP:
                verticalPositionOther -= 1;
                break;
            case BOTTOM:
                verticalPositionOther += 1;
                break;
        }

        int puzzlePieceHeight = SettingsActions.getInstance().getPuzzlePieceSize().height;
        int puzzlePieceWidth = SettingsActions.getInstance().getPuzzlePieceSize().width;

        if (verticalPositionOther - verticalPositionInOtherGroup < verticalPositionThis) {
            int toAdd = verticalPositionThis + verticalPositionInOtherGroup - verticalPositionOther;
            for (int i = 0; i < toAdd; i++) {
                puzzlePiecesList.add(0, new ArrayList<>());
            }
            y -= puzzlePieceHeight * toAdd;
            verticalPositionOther += toAdd;
        }
        if (horizontalPositionOther - horizontalPositionInOtherGroup < horizontalPositionThis) {
            int toAdd = horizontalPositionThis + horizontalPositionInOtherGroup - horizontalPositionOther;
            for (ArrayList<PuzzlePiece> row : puzzlePiecesList) {
                for (int i = 0; i < toAdd; i++) {
                    row.add(0, null);
                }
            }
            x -= puzzlePieceWidth * toAdd;
            horizontalPositionOther += toAdd;
        }

        for (int rIndex = otherGroup.puzzlePiecesList.size() - 1; rIndex >= 0; rIndex--) {
            ArrayList<PuzzlePiece> row = otherGroup.puzzlePiecesList.get(rIndex);
            for (int cIndex = 0; cIndex < row.size(); cIndex++) {
                PuzzlePiece piece = row.get(cIndex);
                if (piece != null) {
                    addPuzzlePieceAtPosition(piece, verticalPositionOther + rIndex - verticalPositionInOtherGroup, horizontalPositionOther + cIndex - horizontalPositionInOtherGroup);
                }
            }
        }


        boolean firstRowOnlyNullValues = true;
        ArrayList<PuzzlePiece> firstRow;

        try {
            while (firstRowOnlyNullValues) {
                firstRow = puzzlePiecesList.get(0);
                for (PuzzlePiece piece : firstRow) {
                    if (piece != null) {
                        firstRowOnlyNullValues = false;
                        break;
                    }
                }
                if (firstRowOnlyNullValues) {
                    puzzlePiecesList.remove(0);
                    y += puzzlePieceHeight;
                }
            }
        } catch (IndexOutOfBoundsException ex) {
        }

        boolean firstColumnOnlyNullValues = true;

        try {
            while (firstColumnOnlyNullValues) {
                for (ArrayList<PuzzlePiece> row : puzzlePiecesList) {
                    if (row.get(0) != null) {
                        firstColumnOnlyNullValues = false;
                        break;
                    }
                }
                if (firstColumnOnlyNullValues) {
                    for (ArrayList<PuzzlePiece> row : puzzlePiecesList) {
                        row.remove(0);
                    }
                    x += puzzlePieceWidth;
                }
            }
        } catch (IndexOutOfBoundsException ex) {
        }

        setChanged();
        notifyObservers();
        PuzzlePieceCount+=otherGroup.PuzzlePieceCount;
        if (PuzzlePieceCount==puzzle.getColumnCount()*puzzle.getRowCount()){
            PuzzleApp.getInstance().getPuzzleWindow().gameEnd();
        }
    }

    public void destroy() {
        puzzlePiecesList.clear();
        puzzle.removePuzzlePieceGroup(this);
        setChanged();
        notifyObservers();
    }


    public List<PuzzlePiece> getPuzzlePieces() {
        List<PuzzlePiece> list = new ArrayList<>();

        for (ArrayList<PuzzlePiece> row : puzzlePiecesList) {
            for (PuzzlePiece piece : row) {
                if (piece != null) {
                    list.add(piece);
                }
            }
        }
        return list;
    }


    public List<PuzzlePieceConnection> getPuzzlePieceConnectionsInPosition(Direction direction) {
        List<PuzzlePieceConnection> connections = new ArrayList<>();

        for (int rIndex = puzzlePiecesList.size() - 1; rIndex >= 0; rIndex--) {
            ArrayList<PuzzlePiece> row = puzzlePiecesList.get(rIndex);
            for (int cIndex = 0; cIndex < row.size(); cIndex++) {
                PuzzlePiece piece = row.get(cIndex);

                if (piece == null) {
                    continue;
                }
                try {
                    int otherRow = rIndex;
                    int otherColumn = cIndex;

                    switch (direction) {
                        case LEFT:
                            otherColumn -= 1;
                            break;
                        case RIGHT:
                            otherColumn += 1;
                            break;
                        case TOP:
                            otherRow -= 1;
                            break;
                        case BOTTOM:
                            otherRow += 1;
                            break;
                    }
                    if (puzzlePiecesList.get(otherRow).get(otherColumn) != null) {
                        continue;
                    }
                } catch (IndexOutOfBoundsException ex) {
                }

                PuzzlePieceConnection newConnection = piece.getConnectorForDirection(direction);
                if (newConnection != null) {
                    connections.add(newConnection);
                }
            }
        }

        return connections;
    }

    public int getMaxPuzzlePiecesInXDirection() {
        int maxNumber = 0;

        for (List<PuzzlePiece> row : puzzlePiecesList) {
            if (row.size() > maxNumber) {
                maxNumber = row.size();
            }
        }
        return maxNumber;
    }

    public int getMaxPuzzlePiecesInYDirection() {
        return puzzlePiecesList.size();
    }

    public int getXPositionOfPieceInGroup(PuzzlePiece puzzlepiece) {
        for (int rIndex = puzzlePiecesList.size() - 1; rIndex >= 0; rIndex--) {
            ArrayList<PuzzlePiece> row = puzzlePiecesList.get(rIndex);
            for (int cIndex = 0; cIndex < row.size(); cIndex++) {
                PuzzlePiece piece = row.get(cIndex);

                if (piece == puzzlepiece) {
                    return cIndex;
                }
            }
        }

        return -1;
    }

    public int getYPositionOfPieceInGroup(PuzzlePiece puzzlepiece) {
        for (int rIndex = puzzlePiecesList.size() - 1; rIndex >= 0; rIndex--) {
            ArrayList<PuzzlePiece> row = puzzlePiecesList.get(rIndex);
            for (int cIndex = 0; cIndex < row.size(); cIndex++) {
                PuzzlePiece piece = row.get(cIndex);

                if (piece == puzzlepiece) {
                    return rIndex;
                }
            }
        }

        return -1;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        setPosition(x, getY());
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
        setChanged();
        notifyObservers();
    }


    public Puzzle getPuzzle() {
        return puzzle;
    }

    void setPuzzle(Puzzle puzzle) {
        this.puzzle = puzzle;
    }


    public int getY() {
        return y;
    }

    public void setY(int y) {
        setPosition(getX(), y);
    }

    public boolean isInPuzzle() {
        return !puzzlePiecesList.isEmpty();
    }

    public boolean isPuzzlePieceContained(PuzzlePiece puzzlepiece) {
        boolean contained = false;

        for (PuzzlePiece tempPuzzlePiece : getPuzzlePieces()) {
            if (tempPuzzlePiece == puzzlepiece) {
                contained = true;
                break;
            }
        }

        return contained;
    }

    private PuzzlePiece addPuzzlePieceAtPosition(PuzzlePiece puzzlepiece, int x, int y) {
        ArrayList<PuzzlePiece> row;

        try {
            row = puzzlePiecesList.get(x);
        } catch (IndexOutOfBoundsException ex) {
            for (int i = puzzlePiecesList.size(); i <= x; i++) {
                row = new ArrayList<>();
                puzzlePiecesList.add(row);
            }
            row = puzzlePiecesList.get(x);
        }

        try {
            row.get(y);
        } catch (IndexOutOfBoundsException ex) {
            for (int i = row.size(); i <= y; i++) {
                row.add(null);
            }
        }

        puzzlepiece.setPuzzlePieceGroup(this);
        return row.set(y, puzzlepiece);
    }
}
