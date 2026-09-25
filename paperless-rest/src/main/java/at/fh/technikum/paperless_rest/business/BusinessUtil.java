package at.fh.technikum.paperless_rest.business;

import at.fh.technikum.paperless_rest.business.exception.InvalidUUIDException;

import java.util.UUID;

public final class BusinessUtil {

    private BusinessUtil() {
        // can not be instantiated
    }

    public static UUID convertToUUID(String uuid) {
        try {
            return UUID.fromString(uuid);
        } catch (IllegalArgumentException ex) {
            throw new InvalidUUIDException(uuid);
        }
    }
}
