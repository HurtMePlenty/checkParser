package imageProcessor;


import org.apache.commons.io.FileUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

enum ImageSaver
{
    instance;

    public void saveImageResult(BufferedImage bufferedImage, String fileName)
    {
        try
        {
            String sourceName = ImageProcessor.instance.fileName;
            File outputfile = new File(String.format("result\\%s\\%s.png", sourceName, fileName));
            outputfile.getParentFile().mkdirs();
            ImageIO.write(bufferedImage, "png", outputfile);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    public void saveTextResult(String textResult, String fileName)
    {
        try
        {
            String sourceName = ImageProcessor.instance.fileName;
            File outputfile = new File(String.format("result\\%s\\%s.txt", sourceName, fileName));
            outputfile.getParentFile().mkdirs();
            FileUtils.writeStringToFile(outputfile, textResult);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }
}
