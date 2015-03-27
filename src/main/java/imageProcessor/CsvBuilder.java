package imageProcessor;

import java.io.File;
import java.io.FileWriter;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

import java.nio.file.Paths;

enum CsvBuilder {
    instance;

    File resultFile;
    String separator = ";";

    private CsvBuilder() {
        String path = Paths.get(".", "result.csv").toString();
        resultFile = new File(path);
        if (!resultFile.exists()) {
            try {
                resultFile.createNewFile();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        writeHeader();
    }


    private void writeHeader() {
        StringBuilder builder = new StringBuilder();
        builder.append("File name");
        builder.append(separator);
        builder.append("Item name");
        builder.append(separator);
        builder.append("Price");
        try {
            Files.write(builder.toString(), resultFile, Charsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void addItemToResult(CheckData checkData) {
        StringBuilder builder = new StringBuilder();
        builder.append("\n");
        builder.append(checkData.checkFileName.replace(";", ""));
        builder.append(separator);
        builder.append(checkData.productName.replace(";", ""));
        builder.append(separator);
        builder.append(checkData.price.replace(";", "").replace(".", ","));
        try {
            Files.append(builder.toString(), resultFile, Charsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
