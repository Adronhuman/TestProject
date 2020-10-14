package andriispuzzle.view;

import andriispuzzle.puzzle.Puzzle;
import andriispuzzle.puzzle.PuzzlePieceGroup;

import javax.swing.*;
import java.awt.*;

public class PuzzleWindow {

    private final PuzzleMainWindow mainWindow;

    private final SettingsWindow settingsWindow;

    public PuzzleWindow() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }
        mainWindow = new PuzzleMainWindow(this);
        settingsWindow = new SettingsWindow();
    }

    public void bringToFront(PuzzlePieceGroup puzzlepieceGroup) {
        mainWindow.bringToFront(puzzlepieceGroup);
    }

    public Rectangle getPuzzleAreaBounds() {
        return mainWindow.getPuzzleAreaBounds();
    }

    public void showUiSettings() {
        settingsWindow.showUiSettings();
    }

    public void showPuzzleWindow() {
        mainWindow.showPuzzleWindow();
    }

    public void setNewPuzzle(Puzzle puzzle) {
        mainWindow.setNewPuzzle(puzzle);
    }

    public JFrame getPuzzleMainWindow(){
        return this.mainWindow;
    }

    public void gameEnd(){
        mainWindow.gameEnd();
    }

}
