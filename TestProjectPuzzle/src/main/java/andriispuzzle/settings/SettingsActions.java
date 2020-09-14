package andriispuzzle.settings;

import andriispuzzle.PuzzleApp;
import andriispuzzle.functions.PuzzleActions;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.*;
import java.io.File;
import java.io.IOException;


public class SettingsActions {

    public final static String SETTINGS_FILE_NAME = "settings.xml";

    private static SettingsActions instance;

    private final PuzzleSettings puzzleSettings;

    private final PuzzleAreaSettings puzzleareaSettings;

    private SettingsActions() {
        puzzleSettings = new PuzzleSettings();
        puzzleareaSettings = new PuzzleAreaSettings();
        try {
            loadSettingsFromFile();
        } catch (IOException ex) {
            System.out.println(ex.getLocalizedMessage());
        }
    }

    public static SettingsActions getInstance() {
        if (instance == null) {
            instance = new SettingsActions();
        }
        return instance;
    }


    public int getPuzzlePieceSnapDistancePercent() {
        return puzzleSettings.getConnectDistancePercent();
    }

    public boolean getShowPuzzlePreview() {
        return puzzleareaSettings.getShowPuzzlePreview();
    }

    public void setShowPuzzlePreview(boolean showPuzzlePreview) {
        puzzleareaSettings.setShowPuzzlePreview(showPuzzlePreview);
    }

    public void setUsedSizeOfPuzzlearea(double number) {
        puzzleareaSettings.setUsedSizeOfPuzzleArea(number);
    }

    public Color getPuzzleAreaBackgroundColor() {
        return puzzleareaSettings.getPuzzleAreaBackgroundColor();
    }

    public void setPuzzleAreaBackgroundColor(Color newColor) {
        puzzleareaSettings.setPuzzleAreaBackgroundColor(newColor);
    }

    public int getPuzzlePieceNumber() {
        return puzzleSettings.getPuzzlePieceNumber();
    }

    public void setPuzzlePieceNumber(int newNumber) {
        puzzleSettings.setPuzzlePieceNumber(newNumber);
    }



    public void loadSettingsFromFile() throws IOException {
        File file = new File(SETTINGS_FILE_NAME);

        try {
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file);
            doc.getDocumentElement().normalize();

            Node settingsNode = doc.getElementsByTagName("settings").item(0);
            if (settingsNode != null) {
                puzzleSettings.loadFromFile((Element) settingsNode);
                puzzleareaSettings.loadFromFile((Element) settingsNode);
            }
        } catch (Exception ex) {
            System.out.println(ex.getLocalizedMessage());
        }
    }

    public void saveSettingsToFile() {
        File file = new File(SETTINGS_FILE_NAME);

        try {
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();

            Element root = doc.createElement("settings");
            doc.appendChild(root);
            puzzleSettings.saveToFile(doc, root);
            puzzleareaSettings.saveToFile(doc, root);

            TransformerFactory.newInstance().newTransformer().transform(new DOMSource(doc),new StreamResult(file));

        } catch (Exception exception){
            System.out.println(exception.getLocalizedMessage());
        }
    }


    public Dimension getPuzzlePieceSize(double puzzleHeight, double puzzleWidth, int puzzleRows, int puzzleColumns) {
        int resizedWidth,resizedHeight;
        resizedHeight = PuzzleApp.getInstance().getPuzzleWindow().getPuzzleAreaBounds().height;
        resizedWidth = (int)(resizedHeight*(puzzleWidth / puzzleHeight));
        return new Dimension(resizedWidth / puzzleColumns, resizedHeight / puzzleRows);
    }

    public Dimension getPuzzlePieceSize() {
        PuzzleActions puzzleActions = PuzzleActions.getInstance();
        return getPuzzlePieceSize(
                puzzleActions.getPuzzleHeight(),
                puzzleActions.getPuzzleWidth(),
                puzzleActions.getPuzzlePieceRowCount(),
                puzzleActions.getPuzzlePieceColumnCount());
    }

}
