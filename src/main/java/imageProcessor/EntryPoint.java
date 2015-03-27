package imageProcessor;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class EntryPoint {
    //private static String fileName = "r14238502408_681231.jpg";

    public static void main(String[] args) {
        try {
            File file = new File("result\\r14234792719_691192.jpg\\parts\\part4.png");

            BufferedImage bufferedImage = ImageIO.read(file);
            String res = TextExtractor.instance.getText(bufferedImage);
            int a = 1;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    /*
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
        }  */
    }
}
