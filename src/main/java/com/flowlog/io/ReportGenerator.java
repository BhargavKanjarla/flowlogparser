package src.main.java.com.flowlog.io;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;


public class ReportGenerator {
    private final String outputPrefix;

    public ReportGenerator(String outputPrefix) {
        this.outputPrefix = outputPrefix;
    }

    public void generateReport(Map<String, Integer> tagCounts, Map<String, Integer> portProtocolCounts) throws IOException {
        generateTagCountReport(tagCounts);
        generatePortProtocolReport(portProtocolCounts);
    }

    private void generateTagCountReport(Map<String, Integer> tagCounts) throws IOException {
        String filename = outputPrefix + "_tag_counter.csv";
        StringBuilder report = new StringBuilder();
        report.append("Tag,Count\n");
        tagCounts.forEach((tag, count) -> report.append(tag).append(",").append(count).append("\n"));

        Files.write(Paths.get(filename), report.toString().getBytes());
    }

    private void generatePortProtocolReport(Map<String, Integer> portProtocolCounts) throws IOException {
        String filename = outputPrefix + "_port_protocol_combinations.csv";
        StringBuilder report = new StringBuilder();
        report.append("Port,Protocol,Count\n");
        portProtocolCounts.forEach((key, count) -> {
            String[] parts = key.split(",");
            report.append(parts[0]).append(",").append(parts[1]).append(",").append(count).append("\n");
        });

        Files.write(Paths.get(filename), report.toString().getBytes());
    }
}
