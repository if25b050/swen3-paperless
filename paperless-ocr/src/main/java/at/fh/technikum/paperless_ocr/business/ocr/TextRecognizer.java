package at.fh.technikum.paperless_ocr.business.ocr;

import java.io.IOException;
import java.nio.file.Path;

public interface TextRecognizer {
    String recognizeText(Path imageFile, Path outputBase) throws IOException, InterruptedException;
}
