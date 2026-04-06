package dranpassung.easyTicket;

public enum TicketSortMode {
    DATE_NEWEST("Newest Created"),
    DATE_OLDEST("Oldest Created"),
    UPDATE_NEWEST("Recently Updated"),
    UPDATE_OLDEST("Least Recently Updated"),
    UNANSWERED("Unanswered by Staff");

    private final String displayName;

    TicketSortMode(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public TicketSortMode next() {
        return values()[(this.ordinal() + 1) % values().length];
    }
}