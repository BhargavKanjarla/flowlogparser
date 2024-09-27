package src.main.java.com.flowlog.io;

import src.main.java.com.flowlog.model.FlowLogEntry;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;


public class FlowLogReader {
    private static final Logger LOGGER = Logger.getLogger(FlowLogReader.class.getName());
    private static final String PROTOCOL_CSV_FILE = "src/resources/protocols.csv";

    private final String filename;
    private final Map<String, String> protocolMap;

    public FlowLogReader(String filename) throws IOException {
        this.filename = filename;
        this.protocolMap = loadProtocols();
    }

    private Map<String, String> loadProtocols() throws IOException {
        Map<String, String> protocols = new HashMap<>();
        ClassLoader classLoader = getClass().getClassLoader();
        try (InputStream inputStream = classLoader.getResourceAsStream(PROTOCOL_CSV_FILE);
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            if (inputStream == null) {
                throw new IOException("Protocol file not found: " + PROTOCOL_CSV_FILE);
            }

            String line;
            boolean isFirstLine = true;
            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue; // Skipping the header line
                }
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    String decimal = parts[0].replaceAll("[^0-9]", "").trim();
                    String keyword = parts[1].replaceAll("\"", "").trim();
                    if (!decimal.isEmpty() && !keyword.isEmpty()) {
                        protocols.put(decimal, keyword.toLowerCase());
                    }
                }
            }

            LOGGER.info("Parsed " + protocols.size() + " protocol numbers from CSV file");

            if (protocols.isEmpty()) {
                throw new IOException("No protocols parsed from CSV file: " + PROTOCOL_CSV_FILE);
            }

            return protocols;
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error processing protocol numbers from CSV: " + PROTOCOL_CSV_FILE, e);
            throw e;
        }
    }

    public List<FlowLogEntry> readEntries() throws IOException {
        try {
            return Files.lines(Paths.get(filename))
                    .map(this::parseLine)
                    .filter(java.util.Objects::nonNull)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error reading flow log file: " + filename, e);
            throw e;
        }
    }

    private FlowLogEntry parseLine(String line) {
        String[] parts = line.split("\\s+");
        if (parts.length < 7) {
            LOGGER.warning("Invalid line in flow log: " + line);
            return null;
        }
        String dstPort = parts[6];
        String protocol = protocolMap.getOrDefault(parts[7], "UNKNOWN");
        return new FlowLogEntry(dstPort, protocol);
    }
}