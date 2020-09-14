package andriispuzzle.view;

import andriispuzzle.puzzle.Shape;
import andriispuzzle.puzzle.*;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;
import java.util.Observable;

public abstract class PuzzlepieceGroupView extends JPanel {

    private final PuzzlePieceGroup piecegroup;

    public PuzzlepieceGroupView(PuzzlePieceGroup piecegroup) {
        this.piecegroup = piecegroup;

        this.setOpaque(false);

        piecegroup.addObserver((Observable o, Object arg) -> {
            if (piecegroup.isInPuzzle()) {
                updateViewLocation();
                this.updateViewSize();
            }
        });
        EventQueue.invokeLater(() -> {
            updateViewLocation();
        });

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

        boolean puzzlepieceHit = false;

        for (PuzzlePiece puzzlepiece : piecegroup.getPuzzlePieces()) {
            int xStart = getXStartPositionOfPuzzlepiece(puzzlepiece);
            int yStart = getYStartPositionOfPuzzlepiece(puzzlepiece);
            int pieceX = xStart - getConnectionsSizeLeftRight();
            int pieceY = yStart - getConnectionsSizeTopButtom();

            if (0 < pieceX && pieceX < getPuzzlePieceWidth() + 2 * getConnectionsSizeLeftRight()
                    || 0 < pieceY || pieceY < getPuzzlePieceHeight() + 2 * getConnectionsSizeTopButtom()) {
                puzzlepieceHit = true;
                break;
            }
        }
        if (!puzzlepieceHit) {
            return false;
        }

        puzzlepieceHit = false;

        for (PuzzlePiece puzzlepiece : piecegroup.getPuzzlePieces()) {
            int xStart = getXStartPositionOfPuzzlepiece(puzzlepiece);
            int yStart = getYStartPositionOfPuzzlepiece(puzzlepiece);
            int pieceX = xStart - getConnectionsSizeLeftRight();
            int pieceY = yStart - getConnectionsSizeTopButtom();

            if (pieceX < x && x < pieceX + getPuzzlePieceWidth() + 2 * getConnectionsSizeLeftRight()
                    && pieceY < y || y < pieceY + getPuzzlePieceHeight() + 2 * getConnectionsSizeTopButtom()) {
            } else {
                continue;
            }

            Rectangle imgRect = new Rectangle(getXStartPositionOfPuzzlepiece(puzzlepiece),
                    getYStartPositionOfPuzzlepiece(puzzlepiece),
                    getPuzzlePieceWidth(),
                    getPuzzlePieceHeight());
            Area area = new Area(imgRect);

            if (area.contains(x, y)) {
                puzzlepieceHit = true;
                break;
            }
        }

        return puzzlepieceHit;
    }

    protected int getConnectionsSizeLeftRight() {
        return getPuzzlePieceWidth() / 2;
    }

    protected int getConnectionsSizeTopButtom() {
        return getPuzzlePieceHeight() / 2;
    }

    public int getHeightOfThisGroup() {
        return getPuzzlePieceHeight() * piecegroup.getMaxPuzzlePiecesInYDirection() + 2 * getConnectionsSizeTopButtom();
    }

    protected abstract Dimension getPuzzleAreaSize();

    protected abstract Point getPuzzleAreaStart();

    protected PuzzlePieceGroup getPuzzlePieceGroup() {
        return piecegroup;
    }

    protected abstract int getPuzzlePieceHeight();

    protected abstract int getPuzzlePieceWidth();

    private GeneralPath getTransformedShapeOnConnector(Direction position, PuzzlePiece puzzlepiece) {
        int shapeSize = 100;

        GeneralPath gp = new GeneralPath(new Shape().constructShape());
        AffineTransform af;

        int xStart = piecegroup.getXPositionOfPieceInGroup(puzzlepiece) * getPuzzlePieceWidth();
        int yStart = piecegroup.getYPositionOfPieceInGroup(puzzlepiece) * getPuzzlePieceHeight();

        af = AffineTransform.getTranslateInstance(getConnectionsSizeLeftRight(), getConnectionsSizeTopButtom());

        af.translate(xStart, yStart);

        if (position.equals(Direction.RIGHT)) {
            double x = getPuzzlePieceWidth() / 2.0;

            af.translate(x, 0);
            af.scale(-1, 1);
            af.translate(-x, 0);
        }

        if (position.equals(Direction.TOP)
                || position.equals(Direction.BOTTOM)) {
            double x = 3 - (getPuzzlePieceWidth() + shapeSize / 2) / ((double) getPuzzlePieceHeight() + shapeSize / 2);
            af.translate(getPuzzlePieceHeight() / x, getPuzzlePieceWidth() / x);
            af.rotate(Math.PI / 2);
            af.translate(-getPuzzlePieceWidth() / x, -getPuzzlePieceHeight() / x);
        }

        if (position.equals(Direction.BOTTOM)) {
            double x = getPuzzlePieceHeight() / 2.0;

            af.translate(x, 0);
            af.scale(-1, 1);
            af.translate(-x, 0);
        }

        if (puzzlepiece.isOutPieceInDirection(position)) {
            af.scale(-1, 1);
        }

        af.scale(getPuzzlePieceWidth() / (double) shapeSize, getPuzzlePieceHeight() / (double) shapeSize);
        gp.transform(af);

        return gp;
    }

    public int getWidthOfThisGroup() {
        return getPuzzlePieceWidth() * piecegroup.getMaxPuzzlePiecesInXDirection() + 2 * getConnectionsSizeLeftRight();
    }

    protected int getXStartPositionOfPuzzlepiece(PuzzlePiece piece) {
        return getConnectionsSizeLeftRight() + piecegroup.getXPositionOfPieceInGroup(piece) * getPuzzlePieceWidth();
    }

    protected int getYStartPositionOfPuzzlepiece(PuzzlePiece piece) {
        return getConnectionsSizeTopButtom() + piecegroup.getYPositionOfPieceInGroup(piece) * getPuzzlePieceHeight();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        g2.setStroke(new BasicStroke((float) 1.1));
        for (PuzzlePiece puzzlepiece : piecegroup.getPuzzlePieces()) {
            paintPiece(puzzlepiece, g2);
        }
    }

    protected void updateViewSize() {
        setSize(getWidthOfThisGroup(), getHeightOfThisGroup());
    }

    protected void updateViewLocation() {
        int x = piecegroup.getX() - getPuzzleAreaStart().x;
        int y = piecegroup.getY() - getPuzzleAreaStart().y;

        setLocation(x - getConnectionsSizeLeftRight(), y - getConnectionsSizeTopButtom());
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
        PuzzlePieceConnection connection;
        Rectangle imgRect;
        Area area;
        GeneralPath gp;

        imgRect = new Rectangle(xStart + getConnectionsSizeLeftRight(), yStart + getConnectionsSizeTopButtom(), puzzlepieceWidth, puzzlepieceHeight);
        area = new Area(imgRect);
        for (Direction position : Direction.values()) {
            connection = puzzlepiece.getConnectorForDirection(position);
            if (connection == null) {
                continue;
            }
            if ((puzzlepiece.isInPieceInDirection(position) && piecegroup.isPuzzlePieceContained(connection.getOutPuzzlePiece()))
                    || (puzzlepiece.isOutPieceInDirection(position) && piecegroup.isPuzzlePieceContained(connection.getInPuzzlePiece()))) {
                continue;
            }

            gp = getTransformedShapeOnConnector(position, puzzlepiece);
            if (puzzlepiece.isInPieceInDirection(position)) {
                area.subtract(new Area(gp));
            } else {
                BufferedImage conImg = connection.getInPuzzlePiece().getImage();
                java.awt.Shape oldClip = g2.getClip();
                Area outConn = new Area(gp);

                outConn.intersect(new Area(oldClip)); // don't draw outside the visible shape of this puzzlepiece
                g2.setClip(outConn);
                switch (position) {
                    case LEFT:
                        g2.drawImage(conImg,
                                xStart - getConnectionsSizeLeftRight(), yStart + getConnectionsSizeTopButtom(),
                                xStart + puzzlepieceWidth - getConnectionsSizeLeftRight(), yStart + puzzlepieceHeight + getConnectionsSizeTopButtom(),
                                0, 0,
                                conImg.getWidth(), conImg.getHeight(),
                                null);
                        break;
                    case RIGHT:
                        g2.drawImage(conImg,
                                xStart + puzzlepieceWidth + getConnectionsSizeLeftRight(), yStart + getConnectionsSizeTopButtom(),
                                xStart + 2 * puzzlepieceWidth + getConnectionsSizeLeftRight() + 1, yStart + puzzlepieceHeight + getConnectionsSizeTopButtom(),
                                0, 0,
                                conImg.getWidth(), conImg.getHeight(),
                                null);
                        break;
                    case TOP:
                        g2.drawImage(conImg,
                                xStart + getConnectionsSizeLeftRight(), yStart - getConnectionsSizeTopButtom(),
                                xStart + puzzlepieceWidth + getConnectionsSizeLeftRight(), yStart + puzzlepieceHeight - getConnectionsSizeTopButtom(),
                                0, 0,
                                conImg.getWidth(), conImg.getHeight(),
                                null);
                        break;
                    case BOTTOM:
                        g2.drawImage(conImg,
                                xStart + getConnectionsSizeLeftRight(), yStart + puzzlepieceHeight + getConnectionsSizeTopButtom(),
                                xStart + puzzlepieceWidth + getConnectionsSizeLeftRight(), yStart + 2 * puzzlepieceHeight + getConnectionsSizeTopButtom() + 1,
                                0, 0,
                                conImg.getWidth(), conImg.getHeight(),
                                null);
                        break;
                }

                g2.setClip(oldClip);
            }
        }

        g2.setPaint(new TexturePaint(img, imgRect));
        g2.fill(area);
    }

}
