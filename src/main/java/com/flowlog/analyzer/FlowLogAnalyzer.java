package src.main.java.com.flowlog.analyzer;

import src.main.java.com.flowlog.io.FlowLogReader;
import src.main.java.com.flowlog.io.LookupTable;
import src.main.java.com.flowlog.io.ReportGenerator;
import src.main.java.com.flowlog.mapper.TagMapper;
import src.main.java.com.flowlog.model.FlowLogEntry;
import src.main.java.com.flowlog.model.TaggedFlowLogEntry;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FlowLogAnalyzer {
    private static final Logger LOGGER = Logger.getLogger(FlowLogAnalyzer.class.getName());

    public static void main(String[] args) {

        if (args.length != 3) {
            LOGGER.severe("Incorrect number of arguments. Usage: java FlowLogAnalyzer <flow_log_file> <lookup_file> <output_file>");
            System.exit(1);
        }

        String flowLogFile = args[0];
        String lookupFile = args[1];
        String outputFile = args[2];

        try {
            LookupTable lookupTable = new LookupTable(lookupFile);
            FlowLogReader flowLogReader = new FlowLogReader(flowLogFile);
            List<FlowLogEntry> entries = flowLogReader.readEntries();

            TagMapper tagMapper = new TagMapper(lookupTable);
            List<TaggedFlowLogEntry> taggedEntries = tagMapper.mapTags(entries);

            StatisticsGenerator statisticsGenerator = new StatisticsGenerator();
            Map<String, Integer> tagCounts = statisticsGenerator.generateTagCounts(taggedEntries);
            Map<String, Integer> portProtocolCounts = statisticsGenerator.generatePortProtocolCounts(entries);

            ReportGenerator reportGenerator = new ReportGenerator(outputFile);
            reportGenerator.generateReport(tagCounts, portProtocolCounts);

            LOGGER.info("Processing complete. Results written to " + outputFile);
        } catch (FileNotFoundException e) {
            LOGGER.log(Level.SEVERE, "File not found: " + e.getMessage(), e);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error processing files: " + e.getMessage(), e);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "An unexpected error occurred: " + e.getMessage(), e);
        }
    }
}