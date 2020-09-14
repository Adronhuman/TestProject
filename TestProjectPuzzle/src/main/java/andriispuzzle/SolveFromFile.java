package andriispuzzle;

import andriispuzzle.puzzle.Direction;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SolveFromFile {
    private final static int delta = 1000000;

    public static BufferedImage solve(String filePath,int rowCount,int columnCount){
       File directory = new File(filePath);
       File[] files = directory.listFiles();
       String imageType = files[0].getName().split("\\.")[1];

       List<BufferedImage> imageList = new ArrayList<>();

        try {
           for (File file : files) {
               BufferedImage image = ImageIO.read(file);
               imageList.add(image);
           }
       }catch (IOException exception){
           System.out.println(exception.getLocalizedMessage());
       }
        int N = imageList.size();
        BufferedImage bestImage = imageList.get(0);
        int bestLevel = 0;
        int imageWidth = imageList.get(0).getWidth();
        int imageHeight = imageList.get(0).getHeight();
        for (int corner = 0;corner<N;corner++){
            int level =0;
            BufferedImage frame = new BufferedImage(imageWidth*columnCount,imageHeight*rowCount,BufferedImage.TYPE_INT_RGB);
            Graphics2D field = frame.createGraphics();
            field.drawImage(imageList.get(corner),0,0,null);
            BufferedImage buf = imageList.get(corner);
            List<BufferedImage> localImageList = new ArrayList<>();

            List<List<BufferedImage>> frameMirror= new ArrayList<>();
            imageList.forEach(localImageList::add);
            localImageList.remove(corner);

            frameMirror.add(new ArrayList<>());
            frameMirror.get(0).add(imageList.get(corner));

            for (int i = 0;i<columnCount-1;i++){
                int maxCount = 0;
                int maxIndex = 0;
                for(int j=0;j<localImageList.size();j++){
                    int count = countOfSimilarPixelsInDirection(buf,localImageList.get(j),Direction.RIGHT);
                    if (count > maxCount){
                        maxCount = count;
                        maxIndex = j;
                    }
                }
                buf = localImageList.remove(maxIndex);
                field.drawImage(buf,imageWidth *(i+1),0,null);
                frameMirror.get(0).add(buf);
                level += maxCount;
            }

            for(int r =1;r<rowCount;r++){
                int maxCount = 0;
                int maxIndex = 0;
                frameMirror.add(new ArrayList<>());
                for (int j=0;j<localImageList.size();j++){
                    int count = countOfSimilarPixelsInDirection(localImageList.get(j),
                                                    frameMirror.get(r-1).get(0),
                                                    Direction.TOP);
                    if (count>maxCount){
                        maxCount = count;
                        maxIndex = j;
                    }
                }
                level+=maxCount;
                buf = localImageList.remove(maxIndex);
                frameMirror.get(r).add(buf);
                field.drawImage(buf,0,r*imageHeight,null);

                for (int i = 0;i<columnCount-1;i++){
                    maxCount = 0;
                    maxIndex = 0;
                    for(int j=0;j<localImageList.size();j++){
                        int count = countOfSimilarPixelsInDirection(buf,localImageList.get(j),Direction.RIGHT);
                        count += countOfSimilarPixelsInDirection(localImageList.get(j),frameMirror.get(r-1).get(i+1),Direction.TOP);
                        if (count > maxCount){
                            maxCount = count;
                            maxIndex = j;
                        }
                    }
                    buf = localImageList.remove(maxIndex);
                    field.drawImage(buf,imageWidth *(i+1),r*imageHeight,null);
                    frameMirror.get(r).add(buf);
                    level += maxCount;
                }

            }
            if (level > bestLevel){
                bestLevel = level;
                bestImage = frame;
            }
        }
        System.out.println("puzzle solved!!!");
        return bestImage;
    }

    public static int countOfSimilarPixelsInDirection(BufferedImage thisImage, BufferedImage otherImage, Direction direction) {
        int count = 0;
        switch (direction){
            case LEFT:
                for(int y =0;y<thisImage.getHeight();y++){
                    if(Math.abs(thisImage.getRGB(0, y)-otherImage.getRGB(otherImage.getWidth()-1, y))<delta){
                        count++;
                    }
                }
                break;
            case RIGHT:
                for(int y=0;y<thisImage.getHeight();y++){
                    if(Math.abs(otherImage.getRGB(0,y)-thisImage.getRGB(thisImage.getWidth()-1,y))<delta){
                        count++;
                    }
                }
                break;
            case TOP:
                for(int x=0;x<thisImage.getWidth();x++){
                    if(Math.abs(thisImage.getRGB(x,0)-otherImage.getRGB(x,otherImage.getHeight()-1))<delta){
                        count++;
                    }
                }
                break;
            case BOTTOM:
                for(int x=0;x<thisImage.getWidth();x++){
                    if(Math.abs(otherImage.getRGB(x,0)-thisImage.getRGB(x,otherImage.getHeight()-1))<delta){
                        count++;
                    }
                }
                break;
        }
        return count;
    }
}
