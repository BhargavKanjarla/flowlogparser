package src.main.java.com.flowlog.analyzer;

import src.main.java.com.flowlog.model.FlowLogEntry;
import src.main.java.com.flowlog.model.TaggedFlowLogEntry;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StatisticsGenerator {
    public Map<String, Integer> generateTagCounts(final List<TaggedFlowLogEntry> entries) {
        Map<String, Integer> map = new HashMap<>();
        for (TaggedFlowLogEntry entry : entries) {
            map.merge(entry.getTag(), 1, Integer::sum);
        }
        return map;
    }

    public Map<String, Integer> generatePortProtocolCounts(final List<FlowLogEntry> entries) {
        return entries.stream()
                .collect(Collectors.groupingBy(
                        e -> e.getDstPort() + "," + e.getProtocol(),
                        Collectors.summingInt(e -> 1)
                ));
    }
}