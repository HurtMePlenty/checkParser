package imageProcessor;

import org.springframework.util.StringUtils;

import java.awt.image.BufferedImage;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

enum ImageProcessor {
    instance;

    public String fileName;

    Pattern itemPricePattern = Pattern.compile("(.*?)\\s(\\d)+[.\\s]+(\\d+)[^\\s]*$");
    Pattern hasCharactersPattern = Pattern.compile("[\\D]{3,}");

    public void processImage(BufferedImage bufferedImage, String fileName) {
        this.fileName = fileName;
        BufferedImage preparedImage = ImagePreparator.instance.prepareImage(bufferedImage);

        BufferedImage rotated = ImageRotator.instance.rotateImage(preparedImage);
        BufferedImage cropped = CheckCropper.instance.cropCheck(rotated);

        List<BufferedImage> bufferedImageList = ImageCutter.instance.cutImageByLines(cropped);
        StringBuilder assembledTextAuto = new StringBuilder();
        StringBuilder assembledTextBlock = new StringBuilder();

        for (BufferedImage imagePart : bufferedImageList) {
            String partTextAuto = TextExtractor.instance.getTextPSMAuto(imagePart);
            assembledTextAuto.append(partTextAuto);
            assembledTextAuto.append("\n");

            String partTextBlock = TextExtractor.instance.getTextPSMSingleBlock(imagePart);
            assembledTextBlock.append(partTextBlock);
            assembledTextBlock.append("\n");
        }

        String textAuto = TextExtractor.instance.getTextPSMAuto(cropped);
        ImageSaver.instance.saveTextResult(textAuto, "resultFullTextAuto");
        String textBlock = TextExtractor.instance.getTextPSMSingleBlock(cropped);
        ImageSaver.instance.saveTextResult(textBlock, "resultFullTextBlock");

        ImageSaver.instance.saveTextResult(assembledTextAuto.toString(), "resultAssembledAuto");
        ImageSaver.instance.saveTextResult(assembledTextBlock.toString(), "resultAssembledBlock");

        Map<String, String> solidTextResultAuto = getItemPrices(textAuto);
        Map<String, String> solidTextResultBlock = getItemPrices(textBlock);
        Map<String, String> assembledTextResultAuto = getItemPrices(assembledTextAuto.toString());
        Map<String, String> assembledTextResultBlock = getItemPrices(assembledTextBlock.toString());


        List<Map<String, String>> resultedMaps = new ArrayList<Map<String, String>>();
        resultedMaps.add(solidTextResultAuto);
        resultedMaps.add(solidTextResultBlock);
        resultedMaps.add(assembledTextResultAuto);
        resultedMaps.add(assembledTextResultBlock);

        int maxRaws = 0;
        int mapIndex = 0;

        int current = 0;
        for (Map<String, String> map : resultedMaps) {
            if (map.keySet().size() > maxRaws) {
                maxRaws = map.keySet().size();
                mapIndex = current;
            }
            current++;
        }


        Map<String, String> finalResult = resultedMaps.get(mapIndex);
        List<CheckData> checkDataList = new ArrayList<CheckData>();
        for (String itemName : finalResult.keySet()) {
            CheckData checkData = new CheckData();
            checkData.checkFileName = fileName;
            checkData.productName = itemName;
            checkData.price = finalResult.get(itemName);
            checkDataList.add(checkData);
        }

        for (CheckData checkData : checkDataList) {
            CsvBuilder.instance.addItemToResult(checkData);
        }

    }

    private Map<String, String> getItemPrices(String data) {
        Map<String, String> result = new LinkedHashMap<String, String>();
        if (StringUtils.isEmpty(data)) {
            return result;
        }

        String[] lines = data.split("\n");
        for (String line : lines) {
            line = line.trim();
            if (StringUtils.isEmpty(line)) {
                continue;
            }
            Matcher priceItemMatcher = itemPricePattern.matcher(line);
            if (priceItemMatcher.find()) {
                String itemName = priceItemMatcher.group(1);
                itemName = itemName.replaceAll("\\s\\d+[\\.\\s]*\\d*", "");
                Matcher hasCharactersMatcher = hasCharactersPattern.matcher(itemName);
                if (!hasCharactersMatcher.find()) {
                    continue;
                }
                String price = String.format("%s.%s", priceItemMatcher.group(2), priceItemMatcher.group(3));
                if (!result.containsKey(itemName)) {
                    result.put(itemName, price);
                }
            }
        }

        return result;
    }

}
