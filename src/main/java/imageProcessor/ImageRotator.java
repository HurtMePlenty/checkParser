package imageProcessor;

import Catalano.Imaging.FastBitmap;
import Catalano.Imaging.Filters.Crop;
import Catalano.Imaging.Filters.Rotate;
import com.recognition.software.jdeskew.ImageDeskew;

import java.awt.image.BufferedImage;

enum ImageRotator
{
    instance;

    public BufferedImage rotateImage(BufferedImage bufferedImage){

        FastBitmap workBitMap = new FastBitmap(bufferedImage);
        int width = workBitMap.getWidth();
        int height = workBitMap.getHeight();
        int xLength = (int)(width * 0.5);
        int yLength = (int)(height * 0.5);

        //inverted axis cuz of matrix Coordinate System
        Crop crop = new Crop(height / 2 - yLength / 2, width / 2 - xLength / 2, xLength, yLength);
        crop.ApplyInPlace(workBitMap);
        BufferedImage sample = workBitMap.toBufferedImage();
        ImageSaver.instance.saveImageResult(sample, "sampleForRotating");
        ImageDeskew imageDeskew = new ImageDeskew(sample);

        double result = imageDeskew.getSkewAngle();
        FastBitmap resultBitMap = new FastBitmap(bufferedImage);
        Rotate rotate = new Rotate(result);
        rotate.applyInPlace(resultBitMap);
        BufferedImage resultBufferedImage = resultBitMap.toBufferedImage();
        ImageSaver.instance.saveImageResult(resultBufferedImage, "rotated");
        return resultBufferedImage;
    }
}
