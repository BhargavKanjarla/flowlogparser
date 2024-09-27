package src.main.java.com.flowlog.analyzer;

import src.main.java.com.flowlog.model.FlowLogEntry;
import src.main.java.com.flowlog.model.TaggedFlowLogEntry;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StatisticsGenerator {
    public Map<String, Integer> generateTagCounts(List<TaggedFlowLogEntry> entries) {
        return entries.stream()
                .collect(Collectors.groupingBy(TaggedFlowLogEntry::getTag, Collectors.summingInt(e -> 1)));
    }

    public Map<String, Integer> generatePortProtocolCounts(List<FlowLogEntry> entries) {
        return entries.stream()
                .collect(Collectors.groupingBy(
                        e -> e.getDstPort() + "," + e.getProtocol(),
                        Collectors.summingInt(e -> 1)
                ));
    }
}