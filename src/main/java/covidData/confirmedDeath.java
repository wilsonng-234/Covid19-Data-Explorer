package covidData;

import javafx.scene.chart.XYChart;
import org.apache.commons.csv.CSVRecord;

import java.time.LocalDate;
import java.util.*;

import static comp3111.covid.DataAnalysis.getFileParser;
import static covidData.confirmedDeathRecord.NOT_FOUND;

public class confirmedDeath extends CovidData {
    private HashMap<String, confirmedDeathRecord> confirmedDeathTable;   // key: country location name  value: ConfirmedCasesRecord
    private HashMap<String,XYChart.Series<String,Number>> confirmedDeathChart;   // key: country location name  value : data point <Date,value>

    //private HashMap<String,XYChart.Series<LocalDate,String>> confirmedCasesChart;   // key: country location name  value : data point <LocalDate,String>

    public confirmedDeath(LocalDate date, HashSet<String> countries, String dataset){
        super(date,countries,dataset);
        confirmedDeathTable = new HashMap<>();
        confirmedDeathChart = new HashMap<>();
    }
    public confirmedDeath(LocalDate startDate,LocalDate endDate, HashSet<String> countries, String dataset){
        super(startDate,endDate,countries,dataset);
        confirmedDeathTable = new HashMap<>();
        confirmedDeathChart = new HashMap<>();
    }

    public HashMap<String, confirmedDeathRecord> getconfirmedDeathTable() {
        for (String countryName : countries)
            confirmedDeathTable.put(countryName,new confirmedDeathRecord(countryName,NOT_FOUND,NOT_FOUND));

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

                String totalDeath = csvRecord.get("total_deaths");
                String totalDeathPerMillion = csvRecord.get("total_deaths_per_million");

                if (totalDeath.equals(""))
                    totalDeath = NOT_FOUND;

                if (totalDeathPerMillion.equals(""))
                    totalDeathPerMillion = NOT_FOUND;

                confirmedDeathRecord display = confirmedDeathTable.get(countryName);

                display.setTotalDeath(totalDeath);
                display.setTotalDeathPerMillion(totalDeathPerMillion);
            }
        }

        return confirmedDeathTable;
    }

    public HashMap<String,XYChart.Series<String, Number>> getConfirmedDeathChart() {
        assert(startDate.isBefore(endDate) || startDate.equals(endDate));

        for (String countryName : countries) {
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName(countryName);
            confirmedDeathChart.put(countryName, series);
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

            String totalDeathPerMillion = csvRecord.get("total_deaths_per_million");
            if (recordDate.isEqual(startDate) || (recordDate.isAfter(startDate) && recordDate.isBefore(endDate)) || recordDate.isEqual(endDate)) {
                if (!totalDeathPerMillion.equals("")) {
                    confirmedDeathChart.get(countryName).getData().add(
                            new XYChart.Data<String, Number>(recordDate.toString(), Double.parseDouble(totalDeathPerMillion))
                    );
                }
                else{

                }
            }
        }

        return confirmedDeathChart;
    }

//    public HashMap<String,XYChart.Series<LocalDate, String>> getConfirmedCasesChart() {
//        assert(startDate.isBefore(endDate) || startDate.equals(endDate));
//
//        for (String countryName : countries) {
//            XYChart.Series<LocalDate, String> series = new XYChart.Series<>();
//            series.setName(countryName);
//            confirmedCasesChart.put(countryName, series);
//        }
//
//        for (CSVRecord csvRecord : getFileParser(dataset)) {
//            String countryName = csvRecord.get("location");
//            if (!countries.contains(countryName))
//                continue;
//
//            String dateRecord = csvRecord.get("date");
//            if (dateRecord.equals("")) {
//                System.out.println("empty dateRecord??");
//                throw new IllegalArgumentException();
//                //continue;
//            }
//
//            String[] dateRecordInfo = dateRecord.trim().split("/");
//            LocalDate recordDate = LocalDate.of(Integer.parseInt(dateRecordInfo[2]),
//                                                Integer.parseInt(dateRecordInfo[0]),
//                                                Integer.parseInt(dateRecordInfo[1]));
//
//            String totalCasesPerMillion = csvRecord.get("total_cases_per_million");
//
//            if (recordDate.isEqual(startDate) || (recordDate.isAfter(startDate) && recordDate.isBefore(endDate)) || recordDate.isEqual(endDate)) {
//                if (!totalCasesPerMillion.equals("")) {
//                    confirmedCasesChart.get(countryName).getData().add(
//                            new XYChart.Data<LocalDate, String>(recordDate, totalCasesPerMillion)
//                    );
//                }
//                else{
//                    confirmedCasesChart.get(countryName).getData().add(
//                            new XYChart.Data<LocalDate, String>(recordDate, NOT_FOUND)
//                    );
//                }
//            }
//        }
//
//        return confirmedCasesChart;
//    }
}