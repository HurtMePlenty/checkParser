package imageProcessor;

import Catalano.Imaging.FastBitmap;

import java.awt.image.BufferedImage;
import java.util.List;

enum ImageCutter
{
    instance;

    private int minHeightOfBlock = 100;
    private int maxHeightOfBlock = 250;
    private double xSpread = 0.5;

    public List<BufferedImage> cutImageByLines(BufferedImage bufferedImage)
    {
        FastBitmap bitmap = new FastBitmap(bufferedImage);
        bitmap.setCoordinateSystem(FastBitmap.CoordinateSystem.Cartesian);
        int width = bitmap.getWidth();
        int xLength = (int) (width * xSpread);
        int xStart = width / 2;
        int height = bitmap.getHeight();

        int currentBlockHeight = 0;

        int cropStartY = 0;
        int partNumber = 0;

        for (int y = 0; y < height; y++)
        {
            if (currentBlockHeight < minHeightOfBlock)
            {
                currentBlockHeight++;
                continue;
            } else
            {
                int currentDots = 0;
                for (int x = xStart - xLength / 2; x < xStart + xLength / 2; x++)
                {
                    int color = bitmap.getGray(x, y);
                    if (color == 0)
                    {
                        currentDots++;
                    }
                }

                if (currentDots == 0 || currentBlockHeight > maxHeightOfBlock)
                {
                    BufferedImage part = ImageCropper.instance.crop(bufferedImage, 0, cropStartY, width, currentBlockHeight);
                    ImageSaver.instance.saveImagePartResult(part, String.format("part%d", partNumber++));
                    currentBlockHeight = 0;
                    y = y - 30; //small jump
                    cropStartY = y;
                } else
                {
                    currentBlockHeight++;
                }
            }
        }
        return null;
    }
}
