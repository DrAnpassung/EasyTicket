package dranpassung.easyTicket;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Ticket {
    private final UUID id;
    private final UUID creatorUUID;
    private final String creatorName;
    private final String message;
    private final long timestamp;
    private long lastUpdate;
    private String lastResponderName;
    private final List<String> transcript = new ArrayList<>();

    // Constructor for New Tickets
    public Ticket(UUID creatorUUID, String creatorName, String message) {
        this.id = UUID.randomUUID();
        this.creatorUUID = creatorUUID;
        this.creatorName = creatorName;
        this.message = message;
        this.timestamp = System.currentTimeMillis();
        this.lastUpdate = this.timestamp;
        addMessage(creatorName, message);
    }

    // Constructor for Loading from File
    public Ticket(UUID id, UUID creatorUUID, String creatorName, String message, long timestamp, long lastUpdate, String lastResponderName, List<String> transcript) {
        this.id = id;
        this.creatorUUID = creatorUUID;
        this.creatorName = creatorName;
        this.message = message;
        this.timestamp = timestamp;
        this.lastUpdate = lastUpdate;
        this.lastResponderName = lastResponderName;
        this.transcript.addAll(transcript);
    }

    public void addMessage(String sender, String text) {
        transcript.add(sender + ": " + text);
        lastResponderName = sender;
        lastUpdate = System.currentTimeMillis();
    }

    public boolean isUnanswered() {
        return lastResponderName.equals(creatorName);
    }

    public UUID getId() { return id; }
    public UUID getCreatorUUID() { return creatorUUID; }
    public String getCreatorName() { return creatorName; }
    public String getMessage() { return message; }
    public long getTimestamp() { return timestamp; }
    public long getLastUpdate() { return lastUpdate; }
    public String getLastResponderName() { return lastResponderName; }
    public List<String> getTranscript() { return transcript; }
}