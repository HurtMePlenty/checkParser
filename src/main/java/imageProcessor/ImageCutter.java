package imageProcessor;

import Catalano.Imaging.FastBitmap;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

enum ImageCutter {
    instance;

    public List<BufferedImage> cutImageByLines(BufferedImage bufferedImage) {

        List<BufferedImage> resultList = new ArrayList<BufferedImage>();
        FastBitmap bitmap = new FastBitmap(bufferedImage);
        bitmap.setCoordinateSystem(FastBitmap.CoordinateSystem.Cartesian);
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        int minHeightOfBlock = bitmap.getHeight() / 5;
        int maxHeightOfBlock = minHeightOfBlock + minHeightOfBlock / 3;
        double xSpread = 0.5;
        int xLength = (int) (width * xSpread);
        int xStart = width / 2;

        int currentBlockHeight = 0;

        int cropStartY = 0;
        int partNumber = 0;

        for (int y = 0; y < height; y++) {
            if (currentBlockHeight < minHeightOfBlock) {
                currentBlockHeight++;
                continue;
            } else {
                int currentDots = 0;
                for (int x = xStart - xLength / 2; x < xStart + xLength / 2; x++) {
                    int color = bitmap.getGray(x, y);
                    if (color == 0) {
                        currentDots++;
                    }
                }

                if (currentDots == 0 || currentBlockHeight > maxHeightOfBlock) {
                    BufferedImage part = ImageCropper.instance.crop(bufferedImage, 0, cropStartY, width, currentBlockHeight);
                    ImageSaver.instance.saveImagePartResult(part, String.format("part%d", partNumber++));
                    resultList.add(part);
                    currentBlockHeight = 0;
                    y = y - minHeightOfBlock / 5; //small jump
                    cropStartY = y;
                } else {
                    currentBlockHeight++;
                }
            }
        }
        return resultList;
    }
}
