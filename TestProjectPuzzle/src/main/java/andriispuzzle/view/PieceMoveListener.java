package andriispuzzle.view;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

class PieceMoveListener extends MouseAdapter {

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
        int newX = puzzlepieceView.getX() + e.getX() - initX ;
        int newY = puzzlepieceView.getY() + e.getY() - initY ;
        Point p = new Point(newX, newY);

        puzzlepieceView.setPuzzlePieceGroupPosition(p);
    }

    @Override
    public void mouseReleased(MouseEvent event) {
        if (event.getButton() == MouseEvent.BUTTON1) {
            puzzlepieceView.tryConnectWithOtherGroups();
        }
    }

}