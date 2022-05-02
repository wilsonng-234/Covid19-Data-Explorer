package covidData;

import javafx.scene.chart.XYChart;
import org.apache.commons.csv.CSVRecord;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.*;

import static comp3111.covid.DataAnalysis.getFileParser;
import static covidData.ConfirmedCasesRecord.NOT_FOUND;

/**
 *  Instances of ConfirmedCases contain required information for creating table and creating chart
 *
 *  table information : (total cases,total cases per million) for each country
 *  chart information : (date,cumulative confirmed cases) for each country
 */

public class ConfirmedCases extends CovidData {
    /**
     *  key: country location name  value: ConfirmedCasesRecord
     */
    private HashMap<String, ConfirmedCasesRecord> confirmedCasesTable;
    /**
     *  key: country location name  value : data point <LocalDate.toEpochDay ,cumulative confirmed cases>
     */
    private HashMap<String,XYChart.Series<Number,Number>> confirmedCasesChart;


    /**
     *
     * @param date  The date for table
     * @param countries The countries selected for table
     * @param dataset   The dataset
     */
    public ConfirmedCases(LocalDate date, HashSet<String> countries, String dataset){
        super(date,countries,dataset);
        confirmedCasesTable = new HashMap<>();
        confirmedCasesChart = new HashMap<>();
    }

    /**
     *
     * @param startDate The startDate of period for chart
     * @param endDate   The endDate of period for chart
     * @param countries The countries selected for chart
     * @param dataset   The dataset
     */
    public ConfirmedCases(LocalDate startDate,LocalDate endDate, HashSet<String> countries, String dataset){
        super(startDate,endDate,countries,dataset);
        confirmedCasesTable = new HashMap<>();
        confirmedCasesChart = new HashMap<>();
    }

    /**
     *
     * @return  table information : (total cases,total cases per million) for each country
     *          key : country  ,  value : (total cases,total cases per million)
     */
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

            NumberFormat numberFormat = NumberFormat.getInstance();
            numberFormat.setMaximumFractionDigits(2);
            ConfirmedCasesRecord row = confirmedCasesTable.get(countryName);
            String totalCases = csvRecord.get("total_cases").trim();
            String totalCasesPerMillion = csvRecord.get("total_cases_per_million").trim();
            if (recordDate.isBefore(startDate))
            {
                if (!totalCases.equals("")){
                    totalCases = numberFormat.format(Double.parseDouble(totalCases));
                    String suffix = "last found on " + recordDate.toString();

                    int numAppend = suffix.length() - totalCases.length();
                    String prefix = " ".repeat(numAppend*2);

                    row.setTotalCases(prefix + totalCases + "\n" + suffix);
                }

                if (!totalCasesPerMillion.equals("")){
                    totalCasesPerMillion = numberFormat.format(Double.parseDouble(totalCasesPerMillion));
                    String suffix = "last found on " + recordDate.toString();

                    int numAppend = suffix.length() - totalCasesPerMillion.length();
                    String prefix = " ".repeat(numAppend*2);

                    row.setTotalCasesPerMillion(prefix + totalCasesPerMillion + "\n" + suffix);
                }
            }
            else if (recordDate.isEqual(startDate)){
                if (!totalCases.equals("")) {
                    totalCases = numberFormat.format(Double.parseDouble(totalCases));
                    row.setTotalCases(totalCases);
                }

                if (!totalCasesPerMillion.equals("")){
                    totalCasesPerMillion = numberFormat.format(Double.parseDouble(totalCasesPerMillion));
                    row.setTotalCasesPerMillion(totalCasesPerMillion);
                }
            }
        }

        return confirmedCasesTable;
    }

    /**
     *
     * @return  chart information : (date,cumulative confirmed cases) for each country
     *          key : country  ,  value : (LocalDate.toEpochDay,cumulative cases per million)
     */
    public HashMap<String,XYChart.Series<Number, Number>> getConfirmedCasesChart() {
        assert(startDate.isBefore(endDate) || startDate.equals(endDate));

        for (String countryName : countries) {
            XYChart.Series<Number, Number> series = new XYChart.Series<>();
            series.setName(countryName);
            confirmedCasesChart.put(countryName, series);
        }

        for (CSVRecord csvRecord : getFileParser(dataset)) {
            String countryName = csvRecord.get("location");
            if (!countries.contains(countryName))
                continue;

            String dateRecord = csvRecord.get("date");
            if (dateRecord.equals("")) {
                continue;
            }

            String[] dateRecordInfo = dateRecord.trim().split("/");
            LocalDate recordDate = LocalDate.of(Integer.parseInt(dateRecordInfo[2]),
                    Integer.parseInt(dateRecordInfo[0]),
                    Integer.parseInt(dateRecordInfo[1]));

            String totalCasesPerMillion = csvRecord.get("total_cases_per_million");
            if (recordDate.isEqual(startDate) || (recordDate.isAfter(startDate) && recordDate.isBefore(endDate)) || recordDate.isEqual(endDate)) {
                try {
                    if (!totalCasesPerMillion.equals("")) {
                        confirmedCasesChart.get(countryName).getData().add(
                                new XYChart.Data<Number, Number>(recordDate.toEpochDay(), Double.parseDouble(totalCasesPerMillion))
                        );
                    }
                }
                catch (NumberFormatException exception){
                    // do nothing
                    System.out.println("NumberFormatException");
                }
            }
        }

        return confirmedCasesChart;
    }
}