# Flow Log Parser

This Java program parses flow log data, maps each row to a tag based on a lookup table, and generates statistics on tag and port/protocol combination counts.

## Requirements

- Java 8 or higher
- Input files should be in plain text (ASCII) format
- Flow log file size can be up to 10 MB
- Lookup file can have up to 10,000 mappings

## Usage

Compile the Java files:

```
javac -d out *.java
```

Run the program:

```
java FlowLogParser <flow_log_file> <lookup_file> <output_file>
```

Replace `<flow_log_file>`, `<lookup_file>`, and `<output_file>` with the appropriate file paths.

Example for program:

```
 java -cp out FlowLogParser input/filelogs.txt input/lookup_file.txt output/output_file.txt

```

## Assumptions

1. The flow log file format follows the AWS VPC Flow Logs format (version 2).
2. The protocol is always TCP (as it's not explicitly provided in the sample data).
3. The lookup table CSV file has a header row that is skipped during parsing.
4. In case of duplicate port/protocol combinations in the lookup table, the first occurrence is used.
5. The program does not validate the content of the input files beyond basic parsing.
6. All file operations are performed using UTF-8 encoding.