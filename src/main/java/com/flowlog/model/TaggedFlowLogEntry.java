package src.main.java.com.flowlog.model;

public class TaggedFlowLogEntry extends FlowLogEntry {
    private final String tag;

    public TaggedFlowLogEntry(final FlowLogEntry entry, final String tag) {
        super(entry.getDstPort(), entry.getProtocol());
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }
}
