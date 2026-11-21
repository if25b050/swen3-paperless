package at.fh.technikum.paperless_ocr.business.ocr;

import at.fh.technikum.paperless_ocr.business.exception.OcrProcessingException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Component
public class GhostscriptPdfRenderer implements PdfRenderer {

    @Override
    public List<Path> renderToPng(Path pdfFile, Path targetDir) throws IOException, InterruptedException {
        Path outputPattern = targetDir.resolve("page-%03d.png");

        ProcessBuilder pb = new ProcessBuilder(
                resolveCommand(),
                "-dSAFER",
                "-dBATCH",
                "-dNOPAUSE",
                "-sDEVICE=png16m",
                "-r300",
                "-o", outputPattern.toAbsolutePath().toString(),
                pdfFile.toAbsolutePath().toString()
        );
        pb.redirectErrorStream(true);

        Process process = pb.start();
        String gsOutput = new String(process.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        int exitCode = process.waitFor();

        if (exitCode != 0) {
            throw new OcrProcessingException(
                    "Ghostscript failed for " + pdfFile.getFileName() + " (exit " + exitCode + "): " + gsOutput
            );
        }

        try (var stream = Files.list(targetDir)) {
            return stream
                    .filter(path -> {
                        String name = path.getFileName().toString();
                        return name.startsWith("page-") && name.endsWith(".png");
                    })
                    .sorted()
                    .toList();
        }
    }

    private static String resolveCommand() {
        String prop = System.getProperty("gs.command");
        if (prop != null && !prop.isBlank()) {
            return prop;
        }

        String env = System.getenv("GS_COMMAND");
        if (env != null && !env.isBlank()) {
            return env;
        }

        java.nio.file.Path gsRoot = java.nio.file.Path.of("C:\\Program Files\\gs");
        if (java.nio.file.Files.isDirectory(gsRoot)) {
            try (java.util.stream.Stream<java.nio.file.Path> walk = java.nio.file.Files.walk(gsRoot, 3)) {
                return walk
                        .filter(java.nio.file.Files::isRegularFile)
                        .filter(p -> {
                            String name = p.getFileName().toString().toLowerCase();
                            return name.equals("gswin64c.exe") || name.equals("gswin32c.exe");
                        })
                        .sorted(java.util.Comparator.reverseOrder())
                        .map(java.nio.file.Path::toString)
                        .findFirst()
                        .orElse("gs");
            } catch (java.io.IOException ignored) {
                // Fallback auf "gs"
            }
        }

        return "gs";
    }
}