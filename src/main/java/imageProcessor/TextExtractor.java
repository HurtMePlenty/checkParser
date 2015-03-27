package imageProcessor;

import net.sourceforge.tess4j.ITessAPI;
import net.sourceforge.tess4j.Tesseract;

import java.awt.image.BufferedImage;
import java.util.Arrays;

enum TextExtractor {
    instance;

    public String getText(BufferedImage bufferedImage) {
        String result = "";
        Tesseract tesseract = Tesseract.getInstance();
        tesseract.setOcrEngineMode(ITessAPI.TessOcrEngineMode.OEM_TESSERACT_CUBE_COMBINED);
        tesseract.setPageSegMode(ITessAPI.TessPageSegMode.PSM_OSD_ONLY);
        tesseract.setConfigs(Arrays.asList("letters"));
        try {
            result = tesseract.doOCR(bufferedImage);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //result = result.replaceAll("[^A-Za-z0-9\\.,\\-\\s\\n]", "");
        try {
            result = result.replace(",", ".").replaceAll("[^\\s]*(\\d+)\\s*\\.\\s*(\\d+)[^\\s]*", "$1.$2");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
