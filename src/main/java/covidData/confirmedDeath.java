package covidData;

import javafx.scene.chart.XYChart;
import org.apache.commons.csv.CSVRecord;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.*;

import static comp3111.covid.DataAnalysis.getFileParser;
import static covidData.confirmedDeathRecord.NOT_FOUND;

/**
 *  Instances of ConfirmedDeath contain required information for creating table and chart <br>
 *
 *  table information : (total Death,total Death per million) for each country <br>
 *  chart information : (date,cumulative confirmed Death) for each country
 */
public class confirmedDeath extends CovidData {
    /**
     *  key: country location name  value: ConfirmedDeathRecord
     */
    private HashMap<String, confirmedDeathRecord> confirmedDeathTable;   // key: country location name  value: confirmedDeathRecord
    /**
     *  key: country location name  value : data point <LocalDate.toEpochDay ,cumulative confirmed Death>
     */
    private HashMap<String,XYChart.Series<Number,Number>> confirmedDeathChart;   // key: country location name  value : data point <Date,value>

    //private HashMap<String,XYChart.Series<LocalDate,String>> confirmedDeathChart;   // key: country location name  value : data point <LocalDate,String>

    /**
     *
     * @param date  The date for table
     * @param countries The countries selected for table
     * @param dataset   The dataset
     */
    public confirmedDeath(LocalDate date, HashSet<String> countries, String dataset){
        super(date,countries,dataset);
        confirmedDeathTable = new HashMap<>();
        confirmedDeathChart = new HashMap<>();
    }
    /**
     *
     * @param startDate The startDate of period for chart
     * @param endDate   The endDate of period for chart
     * @param countries The countries selected for chart
     * @param dataset   The dataset
     */
    public confirmedDeath(LocalDate startDate,LocalDate endDate, HashSet<String> countries, String dataset){
        super(startDate,endDate,countries,dataset);
        confirmedDeathTable = new HashMap<>();
        confirmedDeathChart = new HashMap<>();
    }

    /**
     *
     * @return  table information : (total Death,total Death per million) for each country <br>
     *          key : country  ,  value : (total Death,total Death per million)
     */
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

            NumberFormat numberFormat = NumberFormat.getInstance();
            numberFormat.setMaximumFractionDigits(2);
            confirmedDeathRecord row = confirmedDeathTable.get(countryName);
            String totalDeath = csvRecord.get("total_deaths").trim();
            String totalDeathPerMillion = csvRecord.get("total_deaths_per_million").trim();
            if (recordDate.isBefore(startDate))
            {
                if (!totalDeath.equals("")){
                    totalDeath = numberFormat.format(Double.parseDouble(totalDeath));
                    String suffix = "last found on " + recordDate.toString();

                    int numAppend = suffix.length() - totalDeath.length();
                    String prefix = " ".repeat(numAppend*2);

                    row.setTotalDeath(prefix + totalDeath + "\n" + suffix);
                }

                if (!totalDeathPerMillion.equals("")){
                    totalDeathPerMillion = numberFormat.format(Double.parseDouble(totalDeathPerMillion));
                    String suffix = "last found on " + recordDate.toString();

                    int numAppend = suffix.length() - totalDeathPerMillion.length();
                    String prefix = " ".repeat(numAppend*2);

                    row.setTotalDeathPerMillion(prefix + totalDeathPerMillion + "\n" + suffix);
                }
            }
            else if (recordDate.isEqual(startDate)){
                if (!totalDeath.equals("")) {
                    totalDeath = numberFormat.format(Double.parseDouble(totalDeath));
                    row.setTotalDeath(totalDeath);
                }

                if (!totalDeathPerMillion.equals("")){
                    totalDeathPerMillion = numberFormat.format(Double.parseDouble(totalDeathPerMillion));
                    row.setTotalDeathPerMillion(totalDeathPerMillion);
                }
            }
        }

        return confirmedDeathTable;
    }

    /**
     *
     * @return  chart information : (date,cumulative confirmed Death) for each country <br>
     *          key : country  ,  value : (LocalDate.toEpochDay,cumulative Death per million)
     */
    public HashMap<String,XYChart.Series<Number, Number>> getConfirmedDeathChart() {
        assert(startDate.isBefore(endDate) || startDate.equals(endDate));

        for (String countryName : countries) {
            XYChart.Series<Number, Number> series = new XYChart.Series<>();
            series.setName(countryName);
            confirmedDeathChart.put(countryName, series);
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

            String totalDeathPerMillion = csvRecord.get("total_deaths_per_million");
            if (recordDate.isEqual(startDate) || (recordDate.isAfter(startDate) && recordDate.isBefore(endDate)) || recordDate.isEqual(endDate)) {
                try {
                    if (!totalDeathPerMillion.equals("")) {
                        confirmedDeathChart.get(countryName).getData().add(
                                new XYChart.Data<Number, Number>(recordDate.toEpochDay(), Double.parseDouble(totalDeathPerMillion))
                        );
                    }
                }
                catch (NumberFormatException exception){
                    // do nothing
                    System.out.println("NumberFormatException");
                }
            }
        }

        return confirmedDeathChart;
    }
}