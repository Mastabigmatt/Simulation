package Simulation;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CSVM {


    private static final char DEFAULT_SEPARATOR = ',';

    public static void writeLine(Writer w, List<String> values) throws IOException {
        writeLine(w, values, DEFAULT_SEPARATOR, ' ');
    }

    private static String followCVSformat(String value) {

        String result = value;
        if (result.contains("\"")) {
            result = result.replace("\"", "\"\"");
        }
        return result;

    }

    public static void writeLine(Writer w, List<String> values, char separators, char customQuote) throws IOException {

        boolean first = true;

        if (separators == ' ') {
            separators = DEFAULT_SEPARATOR;
        }

        StringBuilder sb = new StringBuilder();
        for (String value : values) {
            if (!first) {
                sb.append(separators);
            }
            if (customQuote == ' ') {
                sb.append(followCVSformat(value));
            }

            first = false;
        }
        sb.append("\n");
        w.append(sb.toString());


    }

    public static void addToList(Writer writer, int i, List<String> list, int numbers[], String events[], double times[], String stations[]) throws Exception {

        list = new ArrayList<>();
        list.add(String.valueOf(numbers[i]));
        list.add(events[i]);
        list.add(String.valueOf(times[i]));
        list.add(stations[i]);

        writeLine(writer, list);


    }
    //@TODO: it already outputs data files, Modify for Assignment purpose store 6 measures for all experiments and inter arrival times variations
    //  mean delay and the 90% percentile for regular jobs
    //  mean delay and the 90% percentile for gpu jobs
    //  mean delay and the 90% percentile for all jobs
    //  and think of other relevant numbers

    public static void writeCSV(Sink sink, String csvFileCreation, String csvFileStarted, String csvFileCompleted) throws Exception {
        int numbers[] = sink.getNumbers();
        String events[] = sink.getEvents();
        double times[] = sink.getTimes();
        String stations[] = sink.getStations();
        FileWriter writer_1 = new FileWriter(csvFileCreation);
        FileWriter writer_2 = new FileWriter(csvFileStarted);
        FileWriter writer_3 = new FileWriter(csvFileCompleted);

        writeLine(writer_1, Arrays.asList("Position", "event", "time", "type of client"));
        writeLine(writer_2, Arrays.asList("Position", "event", "time", "type of client"));
        writeLine(writer_3, Arrays.asList("Position", "event", "time", "type of client"));

        List<String> list = null;
        List<String> list_2 = null;
        List<String> list_3 = null;

        for (int i = 0; i < numbers.length; i++) {

            if (events[i] == "Creation") {
                addToList(writer_1, i, list, numbers, events, times, stations);
            }
            if (events[i] == "service started") {
                addToList(writer_2, i, list_2, numbers, events, times, stations);
            }
            if (events[i] == "service complete") {
                addToList(writer_3, i, list_3, numbers, events, times, stations);
            }
        }

        writer_1.flush();
        writer_2.flush();
        writer_3.flush();

        writer_1.close();
        writer_2.close();
        writer_3.close();

    }


}
