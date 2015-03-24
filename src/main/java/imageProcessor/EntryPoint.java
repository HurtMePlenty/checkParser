package imageProcessor;

import magick.ImageInfo;
import magick.ImageMagick;
import magick.MagickImage;
import net.sourceforge.tess4j.Tesseract;
import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.JavaCV;

import java.io.File;

public class EntryPoint
{
    public static void main(String[] args)
    {
        try
        {
            //File image = new File("data\\receipt_1424763681-14_723240.jpg");
            //File image = new File("data\\r14238502408_681231.jpg");
            String fileName = "data\\qq.png";
            File imageFile = new File(fileName);
            ImageInfo imageInfo = new ImageInfo(fileName);
            MagickImage image = new MagickImage(imageInfo); //load image

            //CanvasFrame cf = new CanvasFrame();


            /*Tesseract tesseract = Tesseract.getInstance();

            String result = tesseract.doOCR(image);
            String resultString = result.replaceAll("[^\\x00-\\x7F]", "").replace("|", "");
            System.out.println(resultString);   */
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
}
