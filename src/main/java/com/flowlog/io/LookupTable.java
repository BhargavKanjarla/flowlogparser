package src.main.java.com.flowlog.io;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class LookupTable {
    private static final Logger LOGGER = Logger.getLogger(LookupTable.class.getName());
    private final Map<String, String> lookupMap;

    public LookupTable(String filename) throws IOException {
        Path path = Paths.get(filename);
        validateFile(path);
        this.lookupMap = loadLookupTable(path);
    }

    private void validateFile(Path path) throws FileNotFoundException {
        if (!Files.exists(path)) {
            LOGGER.severe("Lookup table file does not exist: " + path);
            throw new FileNotFoundException("Lookup table file does not exist: " + path);
        }
        if (!Files.isReadable(path)) {
            LOGGER.severe("Lookup table file is not readable: " + path);
            throw new FileNotFoundException("Lookup table file is not readable: " + path);
        }
    }

    private Map<String, String> loadLookupTable(Path path) throws IOException {
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            return reader.lines()
                    .skip(1)
                    .map(this::parseLine)
                    .filter(parts -> parts.length == 3)
                    .collect(Collectors.toMap(
                            parts -> parts[0].toLowerCase() + "," + parts[1].toLowerCase(),
                            parts -> parts[2],
                            (v1, v2) -> v1,
                            HashMap::new
                    ));
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error reading lookup table file: " + path, e);
            throw new IOException("Error reading lookup table file: " + path, e);
        }
    }

    private String[] parseLine(String line) {
        String[] parts = line.split(",");
        if (parts.length != 3) {
            LOGGER.warning("Invalid line in lookup table: " + line);
        }
        return parts;
    }

    public String getTag(String dstPort, String protocol) {
        if (dstPort == null || protocol == null) {
            LOGGER.warning("Null destination port or protocol");
            return "Untagged";
        }
        return lookupMap.getOrDefault(dstPort.toLowerCase() + "," + protocol.toLowerCase(), "Untagged");
    }
}