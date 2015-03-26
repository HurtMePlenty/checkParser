package imageProcessor;


import Catalano.Core.IntPoint;
import Catalano.Imaging.FastBitmap;
import Catalano.Imaging.Filters.Crop;
import Catalano.Imaging.Tools.HoughLine;
import Catalano.Imaging.Tools.HoughLineTransformation;
import com.recognition.software.jdeskew.ImageDeskew;

import java.awt.image.BufferedImage;

enum CheckCropper
{
    instance;

    private double checkImageHeightPart = 1;
    private double checkImageWidthPart = 1;
    private int allowedAmountOfBlackDotsForWidth = 5;
    private int allowedAmountOfBlackDotsForHeight = 20;
    private int allowedAmountOfBlackRaws = 10;
    private int allowedAmountOfBlackCols = 5;

    public BufferedImage cropCheck(BufferedImage bufferedImage)
    {
        FastBitmap bitmap = new FastBitmap(bufferedImage);
        int rightBorder = findXBorder(bufferedImage, bitmap.getWidth() / 2, bitmap.getHeight() / 2, borderXDirection.right);
        int leftBorder = findXBorder(bufferedImage, bitmap.getWidth() / 2, bitmap.getHeight() / 2, borderXDirection.left);
        int topBorder = findYBorder(bufferedImage, bitmap.getWidth() / 2, bitmap.getHeight() / 2, borderYDirection.top);
        int bottomBorder = findYBorder(bufferedImage, bitmap.getWidth() / 2, bitmap.getHeight() / 2, borderYDirection.bottom);

        if (rightBorder == -1)
        {
            rightBorder = bitmap.getWidth();
        }
        if (leftBorder == -1)
        {
            leftBorder = 0;
        }

        if (topBorder == -1)
        {
            topBorder = 0;
        }

        if (bottomBorder == -1)
        {
            bottomBorder = bitmap.getHeight();
        }

        BufferedImage result =  ImageCropper.instance.crop(bufferedImage, leftBorder, topBorder, rightBorder - leftBorder, bottomBorder - topBorder);
        ImageSaver.instance.saveImageResult(result, "cropped");
        return result;
    }

    private int findXBorder(BufferedImage bufferedImage, int startX, int startY, borderXDirection direction)
    {
        FastBitmap bitmap = new FastBitmap(bufferedImage);
        bitmap.toRGB();
        bitmap.setCoordinateSystem(FastBitmap.CoordinateSystem.Cartesian);
        int height = bitmap.getHeight();
        int width = bitmap.getWidth();
        int checkLength = (int) ((double) height / checkImageHeightPart);
        int borderX = -1;
        int lastColWithoutNoise = -1;

        int x = startX;
        boolean condition = true;
        int blackCols = 0;
        while (condition)
        {
            IntPoint intPoint = new IntPoint(x, startY);
            bitmap.setRGB(intPoint, 255, 0, 0);
            int currentDots = 0;
            for (int y = startY - checkLength / 2; y < height / 2 + checkLength / 2; y++)
            {
                int color = bitmap.getGray(x, y);
                if (color == 0)
                {
                    currentDots++;
                }
            }
            double percentOfBlack = ((double) currentDots / (double) checkLength) * 100;

            if (percentOfBlack < 30)
            {
                lastColWithoutNoise = x;
                blackCols = 0;

            } else
            {
                blackCols++;
                if (blackCols > allowedAmountOfBlackCols)
                {
                    break;
                }
            }

            if (direction == borderXDirection.right)
            {
                x++;
                condition = x < width;
            } else
            {
                x--;
                condition = x > 0;
            }
        }

        borderX = lastColWithoutNoise;

        /*if (borderX != -1)
        {
            for (int y = startY - checkLength / 2; y < startY + checkLength / 2; y++)
            {
                bitmap.setRGB(borderX, y, 255, 0, 0);
            }
            ImageSaver.instance.saveImageResult(bitmap.toBufferedImage(), "border" + direction.toString());
        }*/

        return borderX;
    }

    private int findYBorder(BufferedImage bufferedImage, int startX, int startY, borderYDirection direction)
    {
        FastBitmap bitmap = new FastBitmap(bufferedImage);
        bitmap.toRGB();
        bitmap.setCoordinateSystem(FastBitmap.CoordinateSystem.Cartesian);
        int height = bitmap.getHeight();
        int width = bitmap.getWidth();
        int checkLength = (int) ((double) width / checkImageWidthPart);
        int borderY;
        int lastRawWithoutNoise = -1;
        int y = startY;
        boolean condition = true;
        int blackRaws = 0;
        while (condition)
        {
            IntPoint intPoint = new IntPoint(startX, y);
            bitmap.setRGB(intPoint, 255, 0, 0);
            int currentDots = 0;
            for (int x = startX - checkLength / 2; x < width / 2 + checkLength / 2; x++)
            {
                int color = bitmap.getGray(x, y);
                if (color == 0)
                {
                    currentDots++;
                }
            }

            double percentOfBlack = ((double) currentDots / (double) checkLength) * 100;

            if (percentOfBlack < 30)
            {
                lastRawWithoutNoise = y;
                blackRaws = 0;
            } else
            {
                blackRaws++;
                if (blackRaws > allowedAmountOfBlackRaws)
                {
                    break;
                }
            }

            if (direction == borderYDirection.top)
            {
                y--;      //start in left top corner
                condition = y > 0;
            } else
            {
                y++;
                condition = y < height;
            }
        }


        borderY = lastRawWithoutNoise;

        /*if (borderY != -1)
        {
            for (int x = startX - checkLength / 2; x < startX + checkLength / 2; x++)
            {
                bitmap.setRGB(x, borderY, 255, 0, 0);
            }
            ImageSaver.instance.saveImageResult(bitmap.toBufferedImage(), "border" + direction.toString());
        }*/

        return borderY;
    }




    enum borderXDirection
    {
        right,
        left;
    }

    enum borderYDirection
    {
        top,
        bottom
    }
}
