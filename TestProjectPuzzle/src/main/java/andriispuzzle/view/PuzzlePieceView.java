package andriispuzzle.view;

import andriispuzzle.PuzzleApp;
import andriispuzzle.functions.PuzzleActions;
import andriispuzzle.puzzle.PuzzlePieceGroup;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


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
        if (puzzlearea == null) {
            return 0;
        } else {
            return puzzlearea.getPuzzlePieceHeight();
        }
    }

    @Override
    protected int getPuzzlePieceWidth() {
        if (puzzlearea == null) {
            return 0;
        } else {
            return puzzlearea.getPuzzlePieceWidth();
        }
    }

    private void setPuzzlePieceGroupPosition(Point position) {
        position.x += puzzlearea.getPuzzleAreaStart().x;
        position.y += puzzlearea.getPuzzleAreaStart().y;
        getPuzzlePieceGroup().setPosition(position.x, position.y);
    }

    private void moveToFront() {
        PuzzleApp.getInstance().getPuzzleWindow().bringToFront(getPuzzlePieceGroup());
    }

    private void tryConnectWithOtherGroups() {
        PuzzleActions.getInstance().ConnectPuzzlePieceGroup(getPuzzlePieceGroup());
    }


    private class PieceMoveListener extends MouseAdapter {

        private int initX;

        private int initY;

        private final PuzzlePieceView puzzlepieceView;

        public PieceMoveListener(PuzzlePieceView puzzlepieceView) {
            this.puzzlepieceView = puzzlepieceView;
        }

        @Override
        public void mousePressed(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                puzzlepieceView.moveToFront();
                initX = e.getX();
                initY = e.getY();
            }
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            int newX = puzzlepieceView.getX() + e.getX() - initX + getConnectionsSizeLeftRight();
            int newY = puzzlepieceView.getY() + e.getY() - initY + getConnectionsSizeTopButtom();
            Point p = new Point(newX, newY);

            puzzlepieceView.setPuzzlePieceGroupPosition(p);
        }

        @Override
        public void mouseReleased(MouseEvent event) {
            if (event.getButton() == MouseEvent.BUTTON1) {
                tryConnectWithOtherGroups();
            }
        }

    }

}
