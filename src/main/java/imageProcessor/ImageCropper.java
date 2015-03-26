package imageProcessor;

import Catalano.Imaging.FastBitmap;
import Catalano.Imaging.Filters.Crop;

import java.awt.image.BufferedImage;

enum ImageCropper
{
    instance;

    public BufferedImage crop(BufferedImage bufferedImage, int x, int y, int width, int height)
    {
        FastBitmap fastBitmap = new FastBitmap(bufferedImage);

        //inversed order because crop use matrix CoordinateSystem
        Crop crop = new Crop(y, x, width, height);
        crop.ApplyInPlace(fastBitmap);
        BufferedImage resultBufferedImage = fastBitmap.toBufferedImage();
        return resultBufferedImage;
    }
}
