package andriispuzzle.settings;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.Observable;


public class PuzzleSettings extends Observable {

    private int puzzlePieceNumber = 100;

    private int connectDistancePercent = 10;

    public int getPuzzlePieceNumber() {
        return puzzlePieceNumber;
    }

    public void setPuzzlePieceNumber(int newNumber) {
        if (newNumber > 0) {
            puzzlePieceNumber = newNumber;
            setChanged();
            notifyObservers();
        }
    }

    public int getConnectDistancePercent() {
        return connectDistancePercent;
    }

    public void setConnectDistancePercent(int connectDistancePercent) {
        this.connectDistancePercent = connectDistancePercent;
    }


    public void loadFromFile(Element settingsNode) {
        Node thisSettingsNode = settingsNode.getElementsByTagName("puzzle-settings").item(0);
        NodeList list;

        if (thisSettingsNode == null) {
            return;
        }
        list = thisSettingsNode.getChildNodes();

        for (int i = 0; i < list.getLength(); i++) {
            Node node = list.item(i);

            switch (node.getNodeName()) {
                case "puzzlepiece-number":
                    try {
                        puzzlePieceNumber = Integer.parseInt(node.getTextContent());
                    } catch (NumberFormatException ex) {
                    }
                    break;
                case "snap-distance-percent":
                    try {
                        connectDistancePercent = Integer.parseInt(node.getTextContent());
                    } catch (NumberFormatException ex) {
                    }
                    break;
            }
        }

        setChanged();
        notifyObservers();
    }


    public void saveToFile(Document doc, Element rootElement) {
        Element settingsElement = doc.createElement("puzzle-settings");
        Element tmpElement;
        rootElement.appendChild(settingsElement);
        tmpElement = doc.createElement("puzzlepiece-number");
        tmpElement.setTextContent(Integer.toString(puzzlePieceNumber));
        settingsElement.appendChild(tmpElement);

    }

}
