package at.fh.technikum.paperless_common.business.integration.mqueue;

import java.io.Serializable;
import java.util.UUID;

/**
 * Object which represents an AI-Summary which is sent via the Message-Queue.
 */
public record GenAiFinishedMessage(UUID uuid, String summary) implements Serializable {
}
