import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.HashSet;

public class JobData {

    private static final String DATA_FILE = "src/main/resources/job_data.csv";
    private static boolean isDataLoaded = false;

    private static ArrayList<HashMap<String, String>> allJobs;


    public static ArrayList<String> findAll(String field) {


        loadData();

        ArrayList<String> values = new ArrayList<>();

        for (HashMap<String, String> row : allJobs) {
            String aValue = row.get(field);

            if (!values.contains(aValue)) {
                values.add(aValue);
            }
        }

        return values;
    }

    public static ArrayList<HashMap<String, String>> findAll() {


        loadData();

        return allJobs;
    }


    public static ArrayList<HashMap<String, String>> findByColumnAndValue(String column, String value) {

        loadData();

        HashSet<HashMap<String, String>> uniqueJobs = new HashSet<>();

        for (HashMap<String, String> row : allJobs) {

            String aValue = row.get(column);

            if (aValue != null && aValue.toLowerCase().contains(value.toLowerCase())) {
                uniqueJobs.add(row);
            }
        }


        return new ArrayList<>(uniqueJobs);
    }

    /**
     * Search all columns for the given term
     *
     * @param value The search term to look for
     * @return      List of all jobs with at least one field containing the value
     */
    public static ArrayList<HashMap<String, String>> findByValue(String value) {

        loadData();

        ArrayList<HashMap<String, String>> jobs = new ArrayList<>();

        HashSet<HashMap<String, String>> uniqueJobs = new HashSet<>();


        for (HashMap<String, String> row : allJobs) {

            for (String key : row.keySet()) {
                String aValue = row.get(key);

                if (aValue.toLowerCase().contains(value.toLowerCase())) {
                    uniqueJobs.add(row);
                    break;
                }
            }
        }

        jobs.addAll(uniqueJobs);
        return jobs;
    }



    private static void loadData() {

        // Only load data once
        if (isDataLoaded) {
            return;
        }

        try {


            Reader in = new FileReader(DATA_FILE);
            CSVParser parser = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(in);
            List<CSVRecord> records = parser.getRecords();
            Integer numberOfColumns = records.get(0).size();
            String[] headers = parser.getHeaderMap().keySet().toArray(new String[numberOfColumns]);

            allJobs = new ArrayList<>();


            for (CSVRecord record : records) {
                HashMap<String, String> newJob = new HashMap<>();

                for (String headerLabel : headers) {
                    newJob.put(headerLabel, record.get(headerLabel));
                }

                allJobs.add(newJob);
            }


            isDataLoaded = true;

        } catch (IOException e) {
            System.out.println("Failed to load job data");
            e.printStackTrace();
        }
    }

}
