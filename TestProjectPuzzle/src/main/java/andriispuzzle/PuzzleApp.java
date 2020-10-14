package andriispuzzle;

import andriispuzzle.view.PuzzleWindow;

public class PuzzleApp {

    private static PuzzleApp instance;

    public static PuzzleApp getInstance() {
        if (instance == null) {
            instance = new PuzzleApp();
        }
        return instance;
    }

    private PuzzleWindow puzzleWindow;

    public static void main(String[] args) {
        getInstance().startGame();
    }

    private PuzzleApp() {
        puzzleWindow = new PuzzleWindow();
    }

    public PuzzleWindow getPuzzleWindow() {
        return puzzleWindow;
    }

    public void setPuzzleWindow(PuzzleWindow puzzleWindow) {
        this.puzzleWindow = puzzleWindow;
    }

    private void startGame() {
        puzzleWindow.showPuzzleWindow();
    }
}
