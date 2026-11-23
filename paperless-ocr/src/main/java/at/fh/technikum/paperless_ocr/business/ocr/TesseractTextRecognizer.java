package at.fh.technikum.paperless_ocr.business.ocr;

import at.fh.technikum.paperless_ocr.business.exception.OcrProcessingException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
public class TesseractTextRecognizer implements TextRecognizer {

    @Override
    public String recognizeText(Path imageFile, Path outputBase) throws IOException, InterruptedException {
        ProcessBuilder tesseractPb = new ProcessBuilder(
                "tesseract",
                imageFile.toAbsolutePath().toString(),
                outputBase.toAbsolutePath().toString(), // write in file
                "-l",
                "deu+eng+rus" // selected languages
        );
        tesseractPb.redirectErrorStream(true);

        Process process = tesseractPb.start();
        String tsOutput = new String(process.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        int exitCode = process.waitFor();

        if (exitCode != 0) {
            throw new OcrProcessingException(
                    "Tesseract failed on " + imageFile.getFileName() + " (exit " + exitCode + "): " + tsOutput
            );
        }

        Path txtFile = Path.of(outputBase + ".txt");
        return Files.readString(txtFile, StandardCharsets.UTF_8);
    }
}
