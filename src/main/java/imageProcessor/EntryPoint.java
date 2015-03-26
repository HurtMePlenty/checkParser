package imageProcessor;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class EntryPoint
{
    //private static String fileName = "r14238502408_681231.jpg";

    public static void main(String[] args)
    {
        try
        {
            File dataFolder = new File("data");
            for (File imageFile : dataFolder.listFiles())
            {
                BufferedImage bufferedImage = ImageIO.read(imageFile);
                ImageProcessor.instance.processImage(bufferedImage, imageFile.getName());
            }
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
}
