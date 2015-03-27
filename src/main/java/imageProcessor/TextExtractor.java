package imageProcessor;

import net.sourceforge.tess4j.ITessAPI;
import net.sourceforge.tess4j.Tesseract;

import java.awt.image.BufferedImage;
import java.util.Arrays;

enum TextExtractor {
    instance;

    public String getTextPSMAuto(BufferedImage bufferedImage){
        return getText(bufferedImage, ITessAPI.TessPageSegMode.PSM_AUTO);
    }

    public String getTextPSMSingleBlock(BufferedImage bufferedImage){
        return getText(bufferedImage, ITessAPI.TessPageSegMode.PSM_SINGLE_BLOCK);
    }

    private String getText(BufferedImage bufferedImage, int tessPageSegMode) {
        String result = "";
        Tesseract tesseract = Tesseract.getInstance();
        //tesseract.setOcrEngineMode(ITessAPI.TessOcrEngineMode.OEM_TESSERACT_ONLY);
        tesseract.setPageSegMode(tessPageSegMode);
        tesseract.setTessVariable("tessedit_char_whitelist", "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890-,.");
        try {
            result = tesseract.doOCR(bufferedImage);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            result = result.replace(",", ".").replaceAll("[^\\s]*(\\d+)\\s*\\.\\s*(\\d+)[^\\s]*", "$1.$2");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
