package src.main.java.com.flowlog.mapper;

import src.main.java.com.flowlog.io.LookupTable;
import src.main.java.com.flowlog.model.FlowLogEntry;
import src.main.java.com.flowlog.model.TaggedFlowLogEntry;

import java.util.List;
import java.util.stream.Collectors;

public class TagMapper {
    private final LookupTable lookupTable;

    public TagMapper(LookupTable lookupTable) {
        this.lookupTable = lookupTable;
    }

    public List<TaggedFlowLogEntry> mapTags(List<FlowLogEntry> entries) {
        return entries.stream()
                .map(entry -> new TaggedFlowLogEntry(entry, lookupTable.getTag(entry.getDstPort(), entry.getProtocol())))
                .collect(Collectors.toList());
    }
}
