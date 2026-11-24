package at.fh.technikum.paperless_ocr.ocr;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class PdfOcrIntegrationTest {

    private static final String IMAGE_DIR = "pdfs";

    @Test
    void testDoc1PdfRenderingAndOcr(@TempDir Path tempDir) throws Exception {
        // Given
        Path pdfPath = resolveResourcePath("pdfs/test_pdf01.pdf");
        List<String> expectedTerms = List.of("Tesseract", "Texterkennung", "einwandfrei");
        assertTrue(Files.exists(pdfPath), "PDF nicht gefunden: " + pdfPath);

        // When
        List<Path> images = renderPdfWithGhostscript(pdfPath, tempDir);
        String recognizedText = extractTextWithTesseract(images, "deu");

        // Then
        assertValidImages(images);
        assertExpectedTermsPresent(recognizedText, expectedTerms, "test_pdf01.pdf");
    }

    @Test
    void testDoc2PdfRenderingAndOcr(@TempDir Path tempDir) throws Exception {
        // Given
        Path pdfPath = resolveResourcePath("pdfs/test_pdf02.pdf");
        List<String> expectedTerms = List.of("Rechnungsnummer", "45,99");
        assertTrue(Files.exists(pdfPath), "PDF nicht gefunden: " + pdfPath);

        // When
        List<Path> images = renderPdfWithGhostscript(pdfPath, tempDir);
        String recognizedText = extractTextWithTesseract(images, "deu");

        // Then
        assertValidImages(images);
        assertExpectedTermsPresent(recognizedText, expectedTerms, "test_pdf02.pdf");
    }

    @Test
    void testDoc3PdfOcr() throws Exception {
        // Given
        List<Path> images = resolveImagesForPrefix("doc03");
        List<String> expectedTerms = List.of("Zentrum", "Links unten", "Rechts oben");

        // When
        String recognizedText = extractTextWithTesseract(images, "deu");

        // Then
        assertValidImages(images);
        assertExpectedTermsPresent(recognizedText, expectedTerms, "doc03");
    }

    @Test
    void testDoc4PdfOcrReadable() throws Exception {
        // Given
        List<Path> images = resolveImagesForPrefix("doc04");
        List<String> expectedTerms = List.of("Äpfel", "Frühling", "Öfen");

        // When
        String recognizedText = extractTextWithTesseract(images, "deu");

        // Then
        assertValidImages(images);
        System.out.println(recognizedText + " -> " + expectedTerms);
        assertExpectedTermsPresent(recognizedText, expectedTerms, "doc04");
    }

    @Test
    void testDoc4PdfOcrUnreadable() throws Exception {
        // Given
        List<Path> images = resolveImagesForPrefix("doc04");
        String unreadableTerm = "schriftartentest";

        // When
        String recognizedText = extractTextWithTesseract(images, "deu");
        String normalizedText = recognizedText.toLowerCase().replaceAll("\\s+", " ").trim();

        // Then
        assertValidImages(images);
        assertFalse(
                normalizedText.contains(unreadableTerm),
                "Begriff in Zierschrift sollte nicht erkannt werden, tauchte aber auf in: " + normalizedText
        );
    }

    @Test
    void testDoc5PdfOcr() throws Exception {
        // Given
        List<Path> images = resolveImagesForPrefix("doc05");
        List<String> expectedTerms = List.of("вечер", "программист");

        // When
        String recognizedText = extractTextWithTesseract(images, "rus");

        // Then
        assertValidImages(images);
        assertExpectedTermsPresent(recognizedText, expectedTerms, "doc05");
    }

    private void assertValidImages(List<Path> images) throws IOException {
        assertFalse(images.isEmpty(), "Keine Bilder vorhanden.");
        for (Path imagePath : images) {
            assertTrue(Files.size(imagePath) > 1024, "Bilddatei zu klein/leer: " + imagePath);
            BufferedImage image = ImageIO.read(imagePath.toFile());
            assertNotNull(image, "Bild konnte nicht gelesen werden: " + imagePath);
            assertTrue(image.getWidth() > 100 && image.getHeight() > 100,
                    "Unplausible Bildgröße: " + image.getWidth() + "x" + image.getHeight());
        }
    }

    private void assertExpectedTermsPresent(String recognizedText, List<String> expectedTerms, String docName) {
        String normalizedText = recognizedText
                .toLowerCase()
                .replaceAll("\\s+", " ")
                .trim();

        assertTrue(normalizedText.length() >= 20,
                "Zu wenig OCR-Text erkannt in " + docName);

        List<String> missingTerms = expectedTerms.stream()
                .map(String::toLowerCase)
                .filter(term -> !normalizedText.contains(term))
                .toList();


        System.out.println("Missing: " + missingTerms);
        System.out.println("Erkannt: " + normalizedText);
        assertTrue(missingTerms.isEmpty(), () ->
                "Fehlende Begriffe in OCR für " + docName + ": " + missingTerms +
                        "\nErkannter Text: " + normalizedText
        );
    }

    private String extractTextWithTesseract(List<Path> images, String language) throws IOException, InterruptedException {
        StringBuilder combinedText = new StringBuilder();
        for (Path imagePath : images) {
            combinedText.append(runTesseractOnImage(imagePath, language)).append('\n');
        }
        return combinedText.toString();
    }


    private String runTesseractOnImage(Path imagePath, String language) throws IOException, InterruptedException {
        String tesseractBinary = resolveTesseractBinary();

        ProcessBuilder pb = new ProcessBuilder(
                tesseractBinary,
                imagePath.toAbsolutePath().toString(),
                "stdout",
                "-l", language,
                "--psm", "3"
        );
        pb.redirectErrorStream(true);

        Process process = pb.start();
        String output = new String(process.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        assertTrue(process.waitFor(60, TimeUnit.SECONDS), "Tesseract-Timeout");
        assertEquals(0, process.exitValue(), "Tesseract fehlgeschlagen (" + tesseractBinary + "):\n" + output);

        return output;
    }

    private String resolveTesseractBinary() {
        String configured = System.getProperty("tesseract.command");
        if (configured != null && !configured.isBlank()) {
            return configured;
        }

        List<Path> candidates = List.of(
                Path.of("C:\\Program Files\\Tesseract-OCR\\tesseract.exe"),
                Path.of("C:\\Program Files (x86)\\Tesseract-OCR\\tesseract.exe")
        );
        for (Path candidate : candidates) {
            if (Files.isRegularFile(candidate)) {
                return candidate.toString();
            }
        }

        return "tesseract";
    }

    private List<Path> resolveImagesForPrefix(String prefix) throws URISyntaxException, IOException {
        URL dirUrl = getClass().getClassLoader().getResource(IMAGE_DIR);
        assertNotNull(dirUrl, "Image dir nicht gefunden: " + IMAGE_DIR);
        Path dirPath = Path.of(dirUrl.toURI());

        try (Stream<Path> files = Files.list(dirPath)) {
            List<Path> matches = files
                    .filter(Files::isRegularFile)
                    .filter(p -> {
                        String name = p.getFileName().toString().toLowerCase();
                        return name.startsWith(prefix.toLowerCase() + "-") && name.endsWith(".png");
                    })
                    .sorted(Comparator.comparing(p -> p.getFileName().toString()))
                    .toList();

            assertFalse(matches.isEmpty(), "Keine PNG-Bilder gefunden für '" + prefix + "' in " + dirPath);
            return matches;
        }
    }

    private Path resolveResourcePath(String resourcePath) throws URISyntaxException {
        URL url = getClass().getClassLoader().getResource(resourcePath);
        assertNotNull(url, "Ressource nicht gefunden: " + resourcePath);
        return Path.of(url.toURI());
    }

    private List<Path> renderPdfWithGhostscript(Path pdfPath, Path outDir) throws IOException, InterruptedException {
        Path outPattern = outDir.resolve("page-%03d.png");
        String gsBinary = resolveGhostscriptBinary();

        ProcessBuilder pb = new ProcessBuilder(
                gsBinary,
                "-dSAFER",
                "-dBATCH",
                "-dNOPAUSE",
                "-sDEVICE=png16m",
                "-r300",
                "-sOutputFile=" + outPattern,
                pdfPath.toAbsolutePath().toString()
        );
        pb.redirectErrorStream(true);

        Process process = pb.start();
        String output = new String(process.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        assertTrue(process.waitFor(60, TimeUnit.SECONDS), "Ghostscript-Timeout");
        assertEquals(0, process.exitValue(), "Ghostscript fehlgeschlagen (" + gsBinary + "):\n" + output);

        try (Stream<Path> files = Files.list(outDir)) {
            return files
                    .filter(p -> p.getFileName().toString().startsWith("page-") && p.getFileName().toString().endsWith(".png"))
                    .sorted(Comparator.comparing(p -> p.getFileName().toString()))
                    .toList();
        }
    }

    private String resolveGhostscriptBinary() {
        String configured = System.getProperty("gs.command");
        if (configured != null && !configured.isBlank()) {
            return configured;
        }

        // Sucht automatisch unter C:\Program Files\gs\gs*\bin\gswin64c.exe oder gswin32c.exe
        Path gsRoot = Path.of("C:\\Program Files\\gs");
        if (Files.isDirectory(gsRoot)) {
            try (Stream<Path> walk = Files.walk(gsRoot, 3)) {
                Optional<Path> found = walk
                        .filter(Files::isRegularFile)
                        .filter(p -> {
                            String name = p.getFileName().toString().toLowerCase();
                            return name.equals("gswin64c.exe") || name.equals("gswin32c.exe");
                        })
                        .sorted(Comparator.reverseOrder())
                        .findFirst();
                if (found.isPresent()) {
                    return found.get().toString();
                }
            } catch (IOException ignored) {
                // Fallback auf "gs" im PATH
            }
        }

        return "gs";
    }
}