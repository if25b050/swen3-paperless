package at.fh.technikum.paperless_rest.document.business.model;

import at.fh.technikum.paperless_rest.common.model.ValidationModel;

import java.util.regex.Pattern;

public record DocumentCreateModel(
        String name,
        byte[] file) implements ValidationModel {

    public static final Pattern FILE_NAME_PATTERN = Pattern.compile("^[\\w,\\s-]+\\.pdf$");

    @Override
    public String validationLogic() {
        if (name == null || name.isBlank()) {
            return "Name is required.";
        }
        if (!FILE_NAME_PATTERN.matcher(name).matches()) {
            return "The file name does not allow special characters and end with .pdf.";
        }
        if (file == null || file.length == 0) {
            return "File is required.";
        }
        return "";
    }
}
