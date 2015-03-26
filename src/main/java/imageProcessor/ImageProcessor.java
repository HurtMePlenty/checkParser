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

        BufferedImage rotated = ImageRotator.instance.rotateImage(preparedImage);
        BufferedImage cropped = CheckCropper.instance.cropCheck(rotated);

        ImageCutter.instance.cutImageByLines(cropped);

        //String text = TextExtractor.instance.getText(cropped);
        //ImageSaver.instance.saveTextResult(text, "result");
    }
}
