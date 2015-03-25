package imageProcessor;

import net.sourceforge.tess4j.Tesseract;

import java.awt.image.BufferedImage;

enum TextExtractor
{
    instance;

    public String getText(BufferedImage bufferedImage)
    {
        String result = "";
        Tesseract tesseract = Tesseract.getInstance();
        try
        {
            result = tesseract.doOCR(bufferedImage);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return result;
    }
}
