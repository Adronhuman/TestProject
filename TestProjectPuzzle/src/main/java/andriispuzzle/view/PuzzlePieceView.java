package andriispuzzle.view;

import andriispuzzle.PuzzleApp;
import andriispuzzle.functions.PuzzleActions;
import andriispuzzle.puzzle.PuzzlePieceGroup;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.util.Objects;


public class PuzzlePieceView extends PuzzlepieceGroupView {

    private final PuzzleArea puzzlearea;

    public PuzzlePieceView(PuzzleArea puzzlearea, PuzzlePieceGroup group) {
        super(group);
        this.puzzlearea = puzzlearea;

        MouseAdapter motionListener = new PieceMoveListener(this);

        this.addMouseListener(motionListener);
        this.addMouseMotionListener(motionListener);

        this.updateViewLocation();
        this.updateViewSize();
    }

    @Override
    protected Dimension getPuzzleAreaSize() {
        return puzzlearea.getSize();
    }

    @Override
    protected Point getPuzzleAreaStart() {
        if (puzzlearea == null) {
            return new Point(0, 0);
        } else {
            return puzzlearea.getPuzzleAreaStart();
        }
    }

    @Override
    protected PuzzlePieceGroup getPuzzlePieceGroup() {
        return super.getPuzzlePieceGroup();
    }

    @Override
    protected int getPuzzlePieceHeight() {
        return Objects.isNull(puzzlearea)?0:puzzlearea.getPuzzlePieceHeight();
    }

    @Override
    protected int getPuzzlePieceWidth() {
        if (puzzlearea == null) {
            return 0;
        } else {
            return puzzlearea.getPuzzlePieceWidth();
        }
    }

    void setPuzzlePieceGroupPosition(Point position) {
        position.x += puzzlearea.getPuzzleAreaStart().x;
        position.y += puzzlearea.getPuzzleAreaStart().y;
        getPuzzlePieceGroup().setPosition(position.x, position.y);
    }

    void moveToFront() {
        PuzzleApp.getInstance().getPuzzleWindow().bringToFront(getPuzzlePieceGroup());
    }

    void tryConnectWithOtherGroups() {
        PuzzleActions.getInstance().ConnectPuzzlePieceGroup(getPuzzlePieceGroup());
    }


}
