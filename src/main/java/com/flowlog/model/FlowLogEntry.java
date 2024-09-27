package src.main.java.com.flowlog.model;

public class FlowLogEntry {
    private final String dstPort;
    private final String protocol;

    public FlowLogEntry(final String dstPort, final String protocol) {
        this.dstPort = dstPort;
        this.protocol = protocol;
    }

    public String getDstPort() {
        return dstPort;
    }

    public String getProtocol() {
        return protocol;
    }
}
