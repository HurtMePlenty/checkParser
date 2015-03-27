package imageProcessor;


import Catalano.Core.IntPoint;
import Catalano.Imaging.Corners.SusanCornersDetector;
import Catalano.Imaging.FastBitmap;
import Catalano.Imaging.Filters.BradleyLocalThreshold;
import Catalano.Imaging.Filters.Grayscale;
import Catalano.Imaging.Filters.Sharpen;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

enum ImagePreparator
{
    instance;

    public BufferedImage prepareImage(BufferedImage bufferedImage)
    {
        ImageSaver.instance.saveImageResult(bufferedImage, "original");
        //BufferedImage sharped = sharp(image);
        BufferedImage grayImage = grayScale(bufferedImage);
        BufferedImage thresholdImage = bradleyLocalThreshold(grayImage);
        return thresholdImage;
    }

    private BufferedImage grayScale(BufferedImage bufferedImage)
    {
        FastBitmap bitmap = new FastBitmap(bufferedImage);
        if(bitmap.isGrayscale()){
            return bufferedImage;
        }
        Grayscale g = new Grayscale(Grayscale.Algorithm.Average);
        g.applyInPlace(bitmap);
        BufferedImage resultBufferedImage = bitmap.toBufferedImage();
        //ImageSaver.instance.saveImageResult(resultBufferedImage, "grayscale");
        return resultBufferedImage;
    }

    private BufferedImage sharp(BufferedImage bufferedImage)
    {
        FastBitmap bitmap = new FastBitmap(bufferedImage);
        Sharpen sharpen = new Sharpen();
        sharpen.applyInPlace(bitmap);
        sharpen.applyInPlace(bitmap);

        BufferedImage resultBufferedImage = bitmap.toBufferedImage();
        ImageSaver.instance.saveImageResult(resultBufferedImage, "sharpen");
        return resultBufferedImage;
    }

    private BufferedImage susanCornerDetection(BufferedImage bufferedImage)
    {
        FastBitmap fastBitmap = new FastBitmap(bufferedImage);
        SusanCornersDetector susan = new SusanCornersDetector();
        ArrayList<IntPoint> lst = susan.ProcessImage(fastBitmap);
        fastBitmap.toRGB();
        for (IntPoint p : lst)
        {
            fastBitmap.setRGB(p, 255, 0, 0);
        }
        BufferedImage resultBufferedImage = fastBitmap.toBufferedImage();
        ImageSaver.instance.saveImageResult(resultBufferedImage, "susanCorner");
        return resultBufferedImage;
    }

    private BufferedImage bradleyLocalThreshold(BufferedImage bufferedImage)
    {
        FastBitmap fastBitmap = new FastBitmap(bufferedImage);
        BradleyLocalThreshold bradley = new BradleyLocalThreshold();
        //bradley.setPixelBrightnessDifferenceLimit(0.02f);
        bradley.applyInPlace(fastBitmap);
        BufferedImage resultBufferedImage = fastBitmap.toBufferedImage();
        //ImageSaver.instance.saveImageResult(resultBufferedImage, "bradleyLocalThreshold");
        return resultBufferedImage;
    }
}
