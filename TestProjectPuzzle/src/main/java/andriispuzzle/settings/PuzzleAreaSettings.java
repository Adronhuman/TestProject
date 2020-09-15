package andriispuzzle.settings;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.awt.*;
import java.util.Observable;

public class PuzzleAreaSettings extends Observable{

    private Color puzzleAreaBackgroundColor = Color.WHITE;

    private boolean showPuzzlePreview = false;

    private double usedSizeOfPuzzleare = 0.5;


    public PuzzleAreaSettings() {
    }

    public Color getPuzzleAreaBackgroundColor() {
        return puzzleAreaBackgroundColor;
    }

    public void setPuzzleAreaBackgroundColor(Color newColor) {
        puzzleAreaBackgroundColor = newColor;
        setChanged();
        notifyObservers();
    }

    public double getUsedSizeOfPuzzleare() {
        return usedSizeOfPuzzleare;
    }

    public void setUsedSizeOfPuzzleare(double usedSizeOfPuzzleare) {
        this.usedSizeOfPuzzleare = usedSizeOfPuzzleare;
    }

    public boolean getShowPuzzlePreview() {
        return showPuzzlePreview;
    }

    public void setShowPuzzlePreview(boolean showPuzzlePreview) {
        this.showPuzzlePreview = showPuzzlePreview;
        setChanged();
        notifyObservers();
    }

    public void loadFromFile(Element settingsNode) {
        Node thisSettingsNode = settingsNode.getElementsByTagName("puzzlearea-settings").item(0);
        NodeList list;

        if (thisSettingsNode == null) {
            return;
        }
        list = thisSettingsNode.getChildNodes();

        for (int i = 0; i < list.getLength(); i++) {
            Node node = list.item(i);
            switch (node.getNodeName()) {
                case "background-color":
                    try {
                        puzzleAreaBackgroundColor = new Color(Integer.parseInt(node.getTextContent()));
                    } catch (NumberFormatException ex) {
                    }
                    break;
                case "show-puzzle-preview":
                    showPuzzlePreview = Boolean.parseBoolean(node.getTextContent());
                    break;
            }
        }
        setChanged();
        notifyObservers();
    }

    public void saveToFile(Document doc, Element rootElement) {
        Element settingsElement = doc.createElement("puzzlearea-settings");
        Element tmpElement;

        rootElement.appendChild(settingsElement);

        tmpElement = doc.createElement("background-color");
        tmpElement.setTextContent(String.valueOf(puzzleAreaBackgroundColor.getRGB()));
        settingsElement.appendChild(tmpElement);

        tmpElement = doc.createElement("show-puzzle-preview");
        tmpElement.setTextContent(String.valueOf(showPuzzlePreview));
        settingsElement.appendChild(tmpElement);
    }

}
