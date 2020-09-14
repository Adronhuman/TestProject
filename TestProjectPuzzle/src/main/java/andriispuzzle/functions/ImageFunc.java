package andriispuzzle.functions;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageFunc {

    public static BufferedImage BufferedImageOf(Image img) {
        if (img instanceof BufferedImage) {
            return (BufferedImage) img;
        }
        BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();

        return bimage;
    }

    public static boolean imagesAreEqual(Image imgageA, Image imgageB) {
        BufferedImage imgA = BufferedImageOf(imgageA);
        BufferedImage imgB = BufferedImageOf(imgageB);

        if (imgA.getWidth() == imgB.getWidth() && imgA.getHeight() == imgB.getHeight()) {

            for (int y = 0; y < imgA.getHeight(); y++) {
                for (int x = 0; x < imgA.getWidth(); x++) {
                    if (imgA.getRGB(x, y) != imgB.getRGB(x, y)) {
                        return false;
                    }
                }
            }

        } else {
            return false;
        }

        return true;
    }
}
