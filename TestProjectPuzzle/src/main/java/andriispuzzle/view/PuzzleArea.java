package andriispuzzle.view;

import andriispuzzle.settings.SettingsActions;
import andriispuzzle.puzzle.Puzzle;
import andriispuzzle.puzzle.PuzzlePieceGroup;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PuzzleArea extends JLayeredPane {

    private Puzzle puzzle;
    private Point puzzleAreaStart = new Point(0, 0);

    private PuzzlePreview preview;

    public PuzzleArea() {
        this.setLayout(null);
        this.setOpaque(true);
        this.setName("puzzlearea");
        this.addPuzzlePreview();
        this.setBackground(SettingsActions.getInstance().getPuzzleAreaBackgroundColor());
    }

    public void bringToFront(PuzzlePieceView puzzlepieceView) {
        moveToFront(puzzlepieceView);
    }

    public void bringToFront(PuzzlePieceGroup puzzlepieceGroup) {
        PuzzlePieceView groupView = null;

        for (Component comp : this.getComponents()) {
            if (comp instanceof PuzzlePieceView) {
                PuzzlePieceView group = (PuzzlePieceView) comp;

                if (group.belongsToPuzzlePieceGroup(puzzlepieceGroup)) {
                    groupView = group;
                    break;
                }
            }
        }
        if (groupView == null) {
            return;
        }

        bringToFront(groupView);
    }

    public void deletePuzzle() {
        removeAll();
        addPuzzlePreview();
        repaint();
    }

    Point getPuzzleAreaStart() {
        return new Point(puzzleAreaStart);
    }

    void setPuzzleAreaStart(Point p) {
        puzzleAreaStart = new Point(p);
    }

    public int getPuzzlePieceHeight() {
        return SettingsActions.getInstance().getPuzzlePieceSize().height;
    }

    public int getPuzzlePieceWidth() {
        return SettingsActions.getInstance().getPuzzlePieceSize().width;
    }

    public void setNewPuzzle(Puzzle puzzle) {

        this.puzzle = puzzle;
        this.deletePuzzle();
        if (puzzle == null) {
            return;
        }

        List<PuzzlePieceGroup> pieceGroups = puzzle.getPuzzlePieceGroups();
        List<PuzzlePieceView> pieceGroupsViews = new ArrayList<>();


        for (PuzzlePieceGroup pieceGroup : pieceGroups) {
            pieceGroupsViews.add(null);
        }

        for (int x = 0; x < puzzle.getRowCount(); x++) {
            for (int y = 0; y < puzzle.getColumnCount(); y++) {
                int listIndex = x * puzzle.getColumnCount() + y;
                PuzzlePieceGroup group;
                PuzzlePieceView newView;

                group = pieceGroups.get(listIndex);
                newView = new PuzzlePieceView(this, group);

                newView.setName("puzzlepiece-group-" + x + "-" + y); // name is needed for tests
                pieceGroupsViews.set(listIndex, newView);

            }
        }

        Dimension size = getSize();

        setVisible(false);
        for (PuzzlePieceView view : pieceGroupsViews) {
            add(view);
        }
        setVisible(true);
        setSize(size);

        this.moveToBack(preview);
    }

    private void addPuzzlePreview() {
        if (this.getComponentCount() != 0) {
            return;
        }

        preview = new PuzzlePreview();
        this.add(preview);
    }

    public Puzzle getPuzzle() {
        return puzzle;
    }
}
