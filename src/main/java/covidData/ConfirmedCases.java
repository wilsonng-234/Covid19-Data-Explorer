package covidData;

import javafx.scene.chart.XYChart;
import org.apache.commons.csv.CSVRecord;

import java.time.LocalDate;
import java.util.*;

import static comp3111.covid.DataAnalysis.getFileParser;
import static covidData.ConfirmedCasesRecord.NOT_FOUND;

public class ConfirmedCases extends CovidData {
    private HashMap<String, ConfirmedCasesRecord> confirmedCasesTable;   // key: country location name  value: ConfirmedCasesRecord
    private HashMap<String,XYChart.Series<String,Number>> confirmedCasesChart;   // key: country location name  value : data point <Date,value>

    public ConfirmedCases(LocalDate date, HashSet<String> countries, String dataset){
        super(date,countries,dataset);
        confirmedCasesTable = new HashMap<>();
        confirmedCasesChart = new HashMap<>();
    }
    public ConfirmedCases(LocalDate startDate,LocalDate endDate, HashSet<String> countries, String dataset){
        super(startDate,endDate,countries,dataset);
        confirmedCasesTable = new HashMap<>();
        confirmedCasesChart = new HashMap<>();
    }

    public HashMap<String, ConfirmedCasesRecord> getconfirmedCasesTable() {
        for (String countryName : countries)
            confirmedCasesTable.put(countryName,new ConfirmedCasesRecord(countryName,NOT_FOUND,NOT_FOUND));

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

                ConfirmedCasesRecord display = confirmedCasesTable.get(countryName);

                display.setTotalCases(totalCases);
                display.setTotalCasesPerMillion(totalCasesPerMillion);
            }
        }

        return confirmedCasesTable;
    }

    public HashMap<String,XYChart.Series<String, Number>> getConfirmedCasesChart() {
        assert(startDate.isBefore(endDate) || startDate.equals(endDate));

        for (String countryName : countries) {
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName(countryName);
            confirmedCasesChart.put(countryName, series);
        }

        for (CSVRecord csvRecord : getFileParser(dataset)) {
            String countryName = csvRecord.get("location");
            if (!countries.contains(countryName))
                continue;

            String countryISO = csvRecord.get("iso_code");
            String dateRecord = csvRecord.get("date");
            if (dateRecord.equals("")) {
                System.out.println("empty dateRecord??");
                throw new IllegalArgumentException();
                //continue;
            }

            String[] dateRecordInfo = dateRecord.trim().split("/");
            LocalDate recordDate = LocalDate.of(Integer.parseInt(dateRecordInfo[2]),
                                                Integer.parseInt(dateRecordInfo[0]),
                                                Integer.parseInt(dateRecordInfo[1]));

            String totalCasesPerMillion = csvRecord.get("total_cases_per_million");
            if (recordDate.isEqual(startDate) || (recordDate.isAfter(startDate) && recordDate.isBefore(endDate)) || recordDate.isEqual(endDate)) {
                if (!totalCasesPerMillion.equals("")) {
                    confirmedCasesChart.get(countryName).getData().add(
                            new XYChart.Data<String, Number>(recordDate.toString(), Double.parseDouble(totalCasesPerMillion))
                    );
                }
                else{

                }
            }
        }

        return confirmedCasesChart;
    }
}