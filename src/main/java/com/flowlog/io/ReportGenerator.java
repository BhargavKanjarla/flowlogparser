package src.main.java.com.flowlog.io;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

public class ReportGenerator {
    private final String outputFile;

    public ReportGenerator(String outputFile) {
        this.outputFile = outputFile;
    }

    public void generateReport(Map<String, Integer> tagCounts, Map<String, Integer> portProtocolCounts) throws IOException {
        StringBuilder report = new StringBuilder();
        report.append("Tag Counts:\n");
        report.append("Tag,Count\n");
        tagCounts.forEach((tag, count) -> report.append(tag).append(",").append(count).append("\n"));

        report.append("\nPort/Protocol Combination Counts:\n");
        report.append("Port,Protocol,Count\n");
        portProtocolCounts.forEach((key, count) -> {
            String[] parts = key.split(",");
            report.append(parts[0]).append(",").append(parts[1]).append(",").append(count).append("\n");
        });

        Files.write(Paths.get(outputFile), report.toString().getBytes());
    }
}
