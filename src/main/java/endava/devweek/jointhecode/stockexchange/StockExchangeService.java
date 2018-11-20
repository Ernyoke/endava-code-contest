package endava.devweek.jointhecode.stockexchange;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Service
public class StockExchangeService {
    private Logger logger = LoggerFactory.getLogger(StockExchangeService.class);

    private static File newFile(File destinationDir, ZipEntry zipEntry) throws IOException {
        File destFile = new File(destinationDir, zipEntry.getName());
        String destDirPath = destinationDir.getCanonicalPath();
        String destFilePath = destFile.getCanonicalPath();
        if (!destFilePath.startsWith(destDirPath + File.separator)) {
            throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
        }
        return destFile;
    }

    private List<File> unpackZip(MultipartFile multipartFile) {
        String destinationPath = System.getProperty("java.io.tmpdir");
        if (!destinationPath.endsWith("/")) {
            destinationPath += "/";
        }
        logger.info("Temporary directory: " + destinationPath);
        long timeStampMillis = Instant.now().toEpochMilli();
        Path path = Paths.get(destinationPath + "zip_" + timeStampMillis + "/");
        File destinationDir = new File(path.toUri());
        List<File> files = new ArrayList<>();
        if (destinationDir.mkdirs()) {
            try {
                ZipInputStream zipInputStream = new ZipInputStream(multipartFile.getInputStream());
                ZipEntry zipEntry = zipInputStream.getNextEntry();
                byte[] buffer = new byte[1024];
                // Only handle .png files, ignore other type of files.
                while (zipEntry != null && zipEntry.getName().toLowerCase().endsWith(".png")) {
                    File file = newFile(destinationDir, zipEntry);
                    files.add(file);
                    FileOutputStream fileOutputStream = new FileOutputStream(file);
                    int len;
                    while ((len = zipInputStream.read(buffer)) > 0) {
                        fileOutputStream.write(buffer, 0, len);
                    }
                    fileOutputStream.close();
                    zipEntry = zipInputStream.getNextEntry();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            logger.error("Could not make temporary directory!");
        }
        return files;
    }

    private String decodeQRCode(File file) throws IOException, NotFoundException {
        BufferedImage bufferedImage = ImageIO.read(new FileInputStream(file));
        LuminanceSource luminanceSource = new BufferedImageLuminanceSource(bufferedImage);
        Binarizer binarizer = new HybridBinarizer(luminanceSource);
        BinaryBitmap binaryBitmap = new BinaryBitmap(binarizer);
        HashMap<DecodeHintType, Object> hintTypeObjectHashMap = new HashMap<>();
        hintTypeObjectHashMap.put(DecodeHintType.CHARACTER_SET, "UTF-8");
        Result result = new MultiFormatReader().decode(binaryBitmap, hintTypeObjectHashMap);
        return result.getText();
    }

    public String getResult(MultipartFile multipartFile) {
        Map<File, PriceGain> responseMap = new TreeMap<>((file1, file2) -> {
            String name1 = file1.getName().substring(0, file1.getName().lastIndexOf("."));
            String name2 = file2.getName().substring(0, file2.getName().lastIndexOf("."));
            int lastIndexOfDash1 = name1.lastIndexOf("-");
            String strPart1 = name1.substring(0, lastIndexOfDash1);
            int lastIndexOfDash2 = name1.lastIndexOf("-");
            String strPart2 = name2.substring(0, lastIndexOfDash2);
            if (strPart1.equals(strPart2)) {
                try {
                    int number1 = Integer.parseInt(name1.substring(lastIndexOfDash1 + 1));
                    int number2 = Integer.parseInt(name2.substring(lastIndexOfDash2 + 1));
                    return number1 - number2;
                } catch (NumberFormatException e) {
                    return name1.compareToIgnoreCase(name2);
                }
            }
            return name1.compareToIgnoreCase(name2);
        });

        if (multipartFile != null) {
            String fileName = multipartFile.getOriginalFilename();

            // Only handle .zip files, ignore other type if inputs.
            if (fileName != null && fileName.toLowerCase().endsWith(".zip")) {
                List<File> files = unpackZip(multipartFile);
                files.forEach(file -> {
                    try {
                        String decodedString = decodeQRCode(file);
                        logger.info("Decoded: " + file.getName());
                        responseMap.put(file, GainOptimizer.optimize(decodedString));
                    } catch (NotFoundException e) {
                        logger.info("Invalid QR code: " + file.getName());
                    } catch (IOException e) {
                        logger.info("IOException: " + e.getMessage());
                    } catch (NumberFormatException e) {
                        logger.info("Invalid decoded string!");
                    } catch (InvalidInputException e) {
                        logger.info("Invalid input exception: " + e.getMessage());
                    }
                });
            }
        } else {
            logger.info("There was no file uploaded!");
        }
        logger.info("Building JSON output...");
        return ResponseBuilder.build(responseMap);
    }
}
