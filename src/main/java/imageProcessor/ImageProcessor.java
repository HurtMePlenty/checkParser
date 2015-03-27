package imageProcessor;

import java.awt.image.BufferedImage;
import java.util.List;

enum ImageProcessor {
    instance;

    public String fileName;

    public void processImage(BufferedImage bufferedImage, String fileName) {
        this.fileName = fileName;
        BufferedImage preparedImage = ImagePreparator.instance.prepareImage(bufferedImage);

        BufferedImage rotated = ImageRotator.instance.rotateImage(preparedImage);
        BufferedImage cropped = CheckCropper.instance.cropCheck(rotated);

        List<BufferedImage> bufferedImageList = ImageCutter.instance.cutImageByLines(cropped);
        StringBuilder builder = new StringBuilder();
        for (BufferedImage imagePart : bufferedImageList) {
            String partText = TextExtractor.instance.getText(imagePart);
            builder.append(partText);
            builder.append("\n");
        }

        String text = TextExtractor.instance.getText(cropped);
        ImageSaver.instance.saveTextResult(text, "resultFullText");
        ImageSaver.instance.saveTextResult(builder.toString(), "resultAssembled");
    }
}
