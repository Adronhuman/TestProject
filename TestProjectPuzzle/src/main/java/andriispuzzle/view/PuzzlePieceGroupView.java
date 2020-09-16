package andriispuzzle.view;

import andriispuzzle.puzzle.*;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Area;
import java.awt.image.BufferedImage;
import java.util.Observable;

public abstract class PuzzlePieceGroupView extends JPanel {

    private final PuzzlePieceGroup piecegroup;

    public PuzzlePieceGroupView(PuzzlePieceGroup piecegroup) {
        this.piecegroup = piecegroup;

//        this.setOpaque(false);

        piecegroup.addObserver((Observable o, Object arg) -> {
            if (piecegroup.isInPuzzle()) {
                updateViewLocation();
                this.updateViewSize();
            }
        });
        updateViewLocation();

    }

    public boolean belongsToPuzzlePieceGroup(PuzzlePieceGroup group) {
        return this.piecegroup == group;
    }

    @Override
    public boolean contains(Point p) {
        return contains(p.x, p.y);
    }

    @Override
    public boolean contains(int x, int y) {

        if (x < 0 || this.getWidth() < x
                || y < 0 || this.getHeight() < y) {
            return false;
        }

        boolean puzzlePieceHit = false;

        for (PuzzlePiece puzzlepiece : piecegroup.getPuzzlePieces()) {
            int pieceX = getXStartPositionOfPuzzlepiece(puzzlepiece) ;
            int pieceY =getYStartPositionOfPuzzlepiece(puzzlepiece);

            if (pieceX < x && x < pieceX + getPuzzlePieceWidth()
                    && pieceY < y || y < pieceY + getPuzzlePieceHeight()) {
            } else {
                continue;
            }

            Rectangle imgRect = new Rectangle(getXStartPositionOfPuzzlepiece(puzzlepiece),
                    getYStartPositionOfPuzzlepiece(puzzlepiece),
                    getPuzzlePieceWidth(),
                    getPuzzlePieceHeight());
            Area area = new Area(imgRect);

            if (area.contains(x, y)) {
                puzzlePieceHit = true;
                break;
            }
        }

        return puzzlePieceHit;
    }

    public int getHeightOfThisGroup() {
        return getPuzzlePieceHeight() * piecegroup.getMaxPuzzlePiecesColumn();
    }

    protected abstract Dimension getPuzzleAreaSize();

    protected abstract Point getPuzzleAreaStart();

    protected PuzzlePieceGroup getPuzzlePieceGroup() {
        return piecegroup;
    }

    protected abstract int getPuzzlePieceHeight();

    protected abstract int getPuzzlePieceWidth();

    public int getWidthOfThisGroup() {
        return getPuzzlePieceWidth() * piecegroup.getMaxPuzzlePiecesRow() ;
    }

    protected int getXStartPositionOfPuzzlepiece(PuzzlePiece piece) {
        return piecegroup.getXPositionOfPieceInGroup(piece) * getPuzzlePieceWidth();
    }

    protected int getYStartPositionOfPuzzlepiece(PuzzlePiece piece) {
        return piecegroup.getYPositionOfPieceInGroup(piece) * getPuzzlePieceHeight();
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        for (PuzzlePiece puzzlepiece : piecegroup.getPuzzlePieces()) {
            paintPiece(puzzlepiece, g2);
            updateViewLocation();
        }
    }

    protected void updateViewSize() {
        setSize(getWidthOfThisGroup(), getHeightOfThisGroup());
    }

    void updateViewLocation() {
        int x = piecegroup.getX() - getPuzzleAreaStart().x;
        int y = piecegroup.getY() - getPuzzleAreaStart().y;

        setLocation(x, y);
    }


    private void paintPiece(PuzzlePiece puzzlepiece, Graphics2D g2) {
        BufferedImage img = puzzlepiece.getImage();
        int puzzlepieceWidth = getPuzzlePieceWidth();
        int puzzlepieceHeight = getPuzzlePieceHeight();

        int xStart = piecegroup.getXPositionOfPieceInGroup(puzzlepiece) * puzzlepieceWidth;
        int yStart = piecegroup.getYPositionOfPieceInGroup(puzzlepiece) * puzzlepieceHeight;

        if (xStart < -puzzlepieceWidth || yStart < -puzzlepieceHeight) {
            return;
        }
        if (xStart > getPuzzleAreaSize().width + puzzlepieceWidth || yStart > getPuzzleAreaSize().height + puzzlepieceHeight) {
            return;
        }

        Rectangle imgRect = new Rectangle(xStart , yStart, puzzlepieceWidth, puzzlepieceHeight);;
        Area area = new Area(imgRect);
        g2.setPaint(new TexturePaint(img, imgRect));
        g2.fill(area);
    }

}
