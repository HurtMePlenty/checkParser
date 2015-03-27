package imageProcessor;


import org.apache.commons.io.FileUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

enum ImageSaver {
    instance;

    public void saveImageResult(BufferedImage bufferedImage, String fileName) {
        try {
            String sourceName = ImageProcessor.instance.fileName;
            String path = Paths.get("result", sourceName, fileName + ".png").toString();
            File outputfile = new File(path);
            outputfile.getParentFile().mkdirs();
            ImageIO.write(bufferedImage, "png", outputfile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveImagePartResult(BufferedImage bufferedImage, String fileName) {
        try {
            String sourceName = ImageProcessor.instance.fileName;
            String path = Paths.get("result", sourceName, "parts", fileName + ".png").toString();
            File outputfile = new File(path);
            outputfile.getParentFile().mkdirs();
            ImageIO.write(bufferedImage, "png", outputfile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveTextResult(String textResult, String fileName) {
        try {
            String sourceName = ImageProcessor.instance.fileName;
            String path = Paths.get("result", sourceName, fileName + ".txt").toString();
            File outputfile = new File(path);
            outputfile.getParentFile().mkdirs();
            FileUtils.writeStringToFile(outputfile, textResult);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
