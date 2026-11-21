package at.fh.technikum.paperless_ocr.business.ocr;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public interface PdfRenderer {
    List<Path> renderToPng(Path pdfFile, Path targetDir) throws IOException, InterruptedException;
}
