package src.main.java.com.flowlog.analyzer;

import src.main.java.com.flowlog.io.FlowLogReader;
import src.main.java.com.flowlog.io.LookupTable;
import src.main.java.com.flowlog.io.ReportGenerator;
import src.main.java.com.flowlog.mapper.TagMapper;
import src.main.java.com.flowlog.model.FlowLogEntry;
import src.main.java.com.flowlog.model.TaggedFlowLogEntry;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;


public class FlowLogAnalyzer {
    private static final Logger LOGGER = Logger.getLogger(FlowLogAnalyzer.class.getName());

    public static void main(String[] args) {
        if (args.length != 3) {
            LOGGER.severe("Incorrect number of arguments. Usage: java FlowLogAnalyzer <flow_log_file> <lookup_file> <output_prefix>");
            System.exit(1);
        }

        final String flowLogFile = args[0];
        final String lookupFile = args[1];
        final String outputPrefix = args[2];

        try {
            final LookupTable lookupTable = new LookupTable(lookupFile);

            final FlowLogReader flowLogReader = new FlowLogReader(flowLogFile);

            LOGGER.info("Reading flow log entries");
            final List<FlowLogEntry> entries = flowLogReader.readEntries();

            LOGGER.info("Mapping tags to entries");
            final TagMapper tagMapper = new TagMapper(lookupTable);
            final List<TaggedFlowLogEntry> taggedEntries = tagMapper.mapTags(entries);

            LOGGER.info("Generating statistics");
            final StatisticsGenerator statisticsGenerator = new StatisticsGenerator();
            final Map<String, Integer> tagCounts = statisticsGenerator.generateTagCounts(taggedEntries);
            final Map<String, Integer> portProtocolCounts = statisticsGenerator.generatePortProtocolCounts(entries);

            final ReportGenerator reportGenerator = new ReportGenerator(outputPrefix);
            reportGenerator.generateReport(tagCounts, portProtocolCounts);

            LOGGER.info("Processing complete. Results written to " + outputPrefix + "_tag_counter.csv and " + outputPrefix + "_port_protocol_combinations.csv");
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error processing files: " + e.getMessage(), e);
            e.printStackTrace();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "An unexpected error occurred: " + e.getMessage(), e);
            e.printStackTrace();
        }
    }
}