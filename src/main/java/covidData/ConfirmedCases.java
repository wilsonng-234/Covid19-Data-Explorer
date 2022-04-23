package covidData;

import org.apache.commons.csv.CSVRecord;

import java.time.LocalDate;
import java.util.*;

import static comp3111.covid.DataAnalysis.getFileParser;

public class ConfirmedCases extends CovidData {
    private HashMap<String, ConfirmedCasesTable> confirmedCasesTable;   // key: country location name  value: ConfirmedCasesTable
    private HashMap<String,?> confirmedCasesChart;

    public ConfirmedCases(LocalDate date, HashSet<String> countries, String dataset){
        super(date,countries,dataset);
        confirmedCasesTable = new HashMap<>();
    }

    public HashMap<String, ConfirmedCasesTable> getconfirmedCasesTable() {
        final String NOT_FOUND = "NOT FOUND";

        for (String countryName : countries)
            confirmedCasesTable.put(countryName,new ConfirmedCasesTable(countryName,NOT_FOUND,NOT_FOUND));

        for (CSVRecord csvRecord : getFileParser(dataset)){
            String countryName = csvRecord.get("location");
            if (!countries.contains(countryName))
                continue;

            String countryISO = csvRecord.get("iso_code");
            String dateRecord = csvRecord.get("date");
            if (dateRecord.equals("")) {
                continue;
            }

            String[] dateRecordInfo = dateRecord.trim().split("/");
            LocalDate recordDate = LocalDate.of(Integer.parseInt(dateRecordInfo[2]),
                                                Integer.parseInt(dateRecordInfo[0]),
                                                Integer.parseInt(dateRecordInfo[1]));

            if (recordDate.isEqual(startDate))
            {
                String iso_code = csvRecord.get("iso_code");

                String totalCases = csvRecord.get("total_cases");
                String totalCasesPerMillion = csvRecord.get("total_cases_per_million");

                if (totalCases.equals(""))
                    totalCases = NOT_FOUND;

                if (totalCasesPerMillion.equals(""))
                    totalCasesPerMillion = NOT_FOUND;

                ConfirmedCasesTable display = confirmedCasesTable.get(countryName);

                display.setTotalCases(totalCases);
                display.setTotalCasesPerMillion(totalCasesPerMillion);
            }
        }

        return confirmedCasesTable;
    }
}