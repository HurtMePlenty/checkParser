package imageProcessor;


import Catalano.Core.IntPoint;
import Catalano.Imaging.FastBitmap;
import Catalano.Imaging.Filters.Crop;

import java.awt.image.BufferedImage;

enum CheckCropper
{
    instance;

    private double checkImageHeightPart = 1.5;
    private double checkImageWidthPart = 3;
    private int allowedAmountOfBlackDotsForWidth = 5;
    private int requiredNumberOfEmptyColsForWidth = 10;
    private int allowedAmountOfBlackDotsForHeight = 20;
    private int requiredNumberOfEmptyColsForHeight = 60;

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
        return crop(bufferedImage, leftBorder, topBorder, rightBorder - leftBorder, bottomBorder - topBorder);
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
        int emptyCols = 0;
        int x = startX;
        boolean condition = true;
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

            if (currentDots < allowedAmountOfBlackDotsForWidth)
            {
                emptyCols++;
                if (emptyCols > requiredNumberOfEmptyColsForWidth)
                {
                    borderX = x;
                    break;
                }
            } else
            {
                emptyCols = 0;
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

        if (borderX != -1)
        {
            for (int y = startY - checkLength / 2; y < startY + checkLength / 2; y++)
            {
                bitmap.setRGB(borderX, y, 255, 0, 0);
            }
            ImageSaver.instance.saveImageResult(bitmap.toBufferedImage(), "border" + direction.toString());
        }

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
        int borderY = -1;
        int emptyCols = 0;
        int y = startY;
        boolean condition = true;
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

            if (currentDots < allowedAmountOfBlackDotsForHeight)
            {
                emptyCols++;
                if (emptyCols > requiredNumberOfEmptyColsForHeight)
                {
                    borderY = y;
                    break;
                }
            } else
            {
                emptyCols = 0;
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

        if (borderY != -1)
        {
            for (int x = startX - checkLength / 2; x < startX + checkLength / 2; x++)
            {
                bitmap.setRGB(x, borderY, 255, 0, 0);
            }
            ImageSaver.instance.saveImageResult(bitmap.toBufferedImage(), "border" + direction.toString());
        }

        return borderY;
    }


    private BufferedImage crop(BufferedImage bufferedImage, int x, int y, int width, int height)
    {
        FastBitmap fastBitmap = new FastBitmap(bufferedImage);

        //inversed order because crop use matrix CoordinateSystem
        Crop crop = new Crop(y, x, width, height);
        crop.ApplyInPlace(fastBitmap);
        BufferedImage resultBufferedImage = fastBitmap.toBufferedImage();
        ImageSaver.instance.saveImageResult(resultBufferedImage, "cropped");
        return resultBufferedImage;
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
