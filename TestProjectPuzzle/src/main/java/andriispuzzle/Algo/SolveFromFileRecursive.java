package andriispuzzle.Algo;

import andriispuzzle.puzzle.Direction;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

import static andriispuzzle.Algo.SolveFromFile.countOfSimilarPixelsInDirection;


public class SolveFromFileRecursive  implements Solver {
    private final static int delta = 10;
    private static List<BufferedImage> imageList = new ArrayList();
    private static int bestArray[][];
    private static int rowCount,columnCount;
    private static int bestLevel;

    public BufferedImage solve(String filePath,int rowCount,int columnCount){
        init(filePath,rowCount,columnCount);
        long time = System.nanoTime();
        solveImage(0,0,-1,0,new boolean[imageList.size()],new int[rowCount][columnCount]);

        int imageWidth,imageHeight;
        imageWidth = imageList.get(0).getWidth();
        imageHeight = imageList.get(0).getHeight();
        BufferedImage frame = new BufferedImage(imageWidth*columnCount,imageHeight*rowCount,BufferedImage.TYPE_INT_RGB);
        Graphics2D field = frame.createGraphics();
        for (int i = 0;i<rowCount;i++){
            for (int j =0;j<columnCount;j++){
                field.drawImage(imageList.get(bestArray[i][j]),imageWidth*j,imageHeight*i,null);
            }
        }
        System.out.println("solved");
        System.out.println((System.nanoTime()-time)/1000000000);
        return frame;
    }
    public void init(String filePath,int rowCountt,int columnCountt) {
        rowCount = rowCountt;
        columnCount = columnCountt;
        bestArray = new int[rowCount][columnCount];
        bestLevel = Integer.MIN_VALUE;
        imageList.clear();
        File directory = new File(filePath);
        File[] files = directory.listFiles();


        try {
            for (int i=0;i<files.length;i++) {
                BufferedImage image = ImageIO.read(files[i]);
                imageList.add(image);
            }
        } catch (IOException exception) {
            System.out.println(exception.getLocalizedMessage());
        }
    }
    public void solveImage(int x,int y,int value,int level,boolean[] used,int[][] array){
        if (value ==-1 && x==0 && y==0){
            int max = Integer.MIN_VALUE;
            for (int i = 0;i<imageList.size();i++){
                if (!used[i]){
                    solveImage(x,y,i,0,Arrays.copyOf(used,used.length), Arrays.stream(array).map(int[]::clone).toArray(int[][]::new));
                }
            }
            return;
        }
        array[y][x] = value;
        used[value]=true;
        if (x==columnCount-1&& y==rowCount-1){
            if (level > bestLevel){
                bestLevel = level;
                bestArray = array;
            }
            return;
        }
        List<Integer> best = new ArrayList<>();
        double max = Integer.MIN_VALUE;
        int nextY = nextY(x,y),nextX = nextX(x,y);
        int v1,v2;
        double v;
        for (int i = 0;i<imageList.size();i++){
            if (!used[i]){
                if (nextX==0) {
                    v = countOfSimilarPixelsInDirection(imageList.get(array[y][0]),imageList.get(i),Direction.BOTTOM);
                }
                else if (y==0) {
                    v = countOfSimilarPixelsInDirection(imageList.get(value), imageList.get(i), Direction.RIGHT);;
                }
                else{
                    v1 = countOfSimilarPixelsInDirection(imageList.get(array[y-1][x+1]),imageList.get(i),Direction.BOTTOM);
                    v2 = countOfSimilarPixelsInDirection(imageList.get(value), imageList.get(i), Direction.RIGHT);
                    v = (v1+v2)*0.5;
                }
                if (v>max){
                    best.clear();
                    best.add(i);
                    max= v;
                }
                else if (v==max && best.size()<1){
                    best.add(i);
                }
            }
        }
        int res = (int)max+level;
        for(int index:best) {
            solveImage(nextX, nextY, index, res, Arrays.copyOf(used, used.length), Arrays.stream(array).map(int[]::clone).toArray(int[][]::new));
        }
        return;
    }

    public static int nextX(int x,int y){
        if (x==columnCount-1){
            return 0;
        }
        else{
            return x+1;
        }
    }
    public static int nextY(int x,int y){
        if (x==columnCount-1){
            return y+1;
        }
        else{
            return y;
        }
    }
    public static HashMap<Integer,Boolean> copy(
            HashMap<Integer, Boolean> original)
    {
        HashMap<Integer,Boolean >copy = new HashMap<Integer,Boolean>();
        for (Map.Entry<Integer,Boolean> entry : original.entrySet())
        {
            copy.put(entry.getKey(), entry.getValue());
        }
        return copy;
    }

}
