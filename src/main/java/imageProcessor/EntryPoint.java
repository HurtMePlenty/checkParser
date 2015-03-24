package imageProcessor;

import net.sourceforge.tess4j.Tesseract;

import java.io.File;

public class EntryPoint
{
    public static void main(String[] args)
    {
        //File image = new File("data\\receipt_1424763681-14_723240.jpg");
        //File image = new File("data\\r14238502408_681231.jpg");
        File image = new File("data\\test1.jpg");
        Tesseract tesseract = Tesseract.getInstance();
        try
        {
           String result = tesseract.doOCR(image);
            System.out.println(result);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
}
