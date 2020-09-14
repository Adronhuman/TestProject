package andriispuzzle.view;

import andriispuzzle.functions.PuzzleActions;
import andriispuzzle.settings.SettingsActions;

import javax.swing.*;
import java.awt.*;

public class PuzzlePreview extends JPanel {


    private final int SIZE = 300;

    public PuzzlePreview() {
        this.setName("puzzle-preview");
        this.setOpaque(false);
        this.setLocation(0, 0);
        this.setSize(SIZE, SIZE);

    }

    @Override
    public void paintComponent(Graphics g) {
        if (SettingsActions.getInstance().getShowPuzzlePreview() && PuzzleActions.getInstance().isPuzzleActive()) {
            Image previewImg = PuzzleActions.getInstance().getPuzzlePieceImage();
            int height, width;

            if (previewImg.getHeight(null) < previewImg.getWidth(null)) {
                width = SIZE;
                height = (int) (width * (previewImg.getHeight(null) / (float) previewImg.getWidth(null)));
            } else {
                height = SIZE;
                width = (int) (height * (previewImg.getWidth(null) / (float) previewImg.getHeight(null)));
            }
            g.drawImage(previewImg, 0, 0, width, height, null);
        }

        super.paintComponent(g);
    }

}
