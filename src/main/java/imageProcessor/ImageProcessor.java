package imageProcessor;

import java.awt.image.BufferedImage;

enum ImageProcessor
{
    instance;

    public String fileName;

    public void processImage(BufferedImage bufferedImage, String fileName)
    {
        this.fileName = fileName;
        BufferedImage preparedImage = ImagePreparator.instance.prepareImage(bufferedImage);
        CheckCropper.instance.cropCheck(preparedImage);
    }
}
