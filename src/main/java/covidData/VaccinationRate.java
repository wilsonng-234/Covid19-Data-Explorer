package covidData;

import javafx.scene.chart.XYChart;
import org.apache.commons.csv.CSVRecord;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;

import static comp3111.covid.DataAnalysis.getFileParser;
import static covidData.VaccinationRateRecord.NOT_FOUND;

public class VaccinationRate extends CovidData {
	private HashMap<String, VaccinationRateRecord> vaccinationRateTable;
    private HashMap<String, XYChart.Series<Number, Number>>  vaccinationRateChart;

	public VaccinationRate(LocalDate date, HashSet<String> countries, String dataset) {
		super(date, countries, dataset);
		vaccinationRateTable = new HashMap<>();
	}
    public VaccinationRate(LocalDate startDate, LocalDate endDate, HashSet<String> countries, String dataset) {
        this(startDate, countries, dataset);
        this.endDate = endDate;
        vaccinationRateChart = new HashMap<>();
    }


    // table
	public HashMap<String, VaccinationRateRecord> getVaccinationRateTable() {
        for (String countryName : countries)
            vaccinationRateTable.put(countryName,new VaccinationRateRecord(countryName,NOT_FOUND,NOT_FOUND));

		for (CSVRecord csvRecord : getFileParser(dataset)) {
            String countryName = csvRecord.get("location");
            if (!countries.contains(countryName))
                continue;

            //String countryISO = csvRecord.get("iso_code");
            String dateRecord = csvRecord.get("date");
            if (dateRecord.equals("")) {
                continue;
            }

            String[] dateRecordInfo = dateRecord.trim().split("/");
            LocalDate recordDate = LocalDate.of(
                    Integer.parseInt(dateRecordInfo[2]),
                    Integer.parseInt(dateRecordInfo[0]),
                    Integer.parseInt(dateRecordInfo[1]));

            NumberFormat numberFormat = NumberFormat.getInstance();
            numberFormat.setMaximumFractionDigits(2);
            VaccinationRateRecord row = vaccinationRateTable.get(countryName);

            String fullyVaccinated = csvRecord.get("people_fully_vaccinated").trim();
            String rateOfVaccination = csvRecord.get("people_fully_vaccinated_per_hundred").trim();

            if (recordDate.isBefore(startDate))
            {
                if (!fullyVaccinated.equals("")){
                    fullyVaccinated = numberFormat.format(Double.parseDouble(fullyVaccinated));
                    String suffix = "last found on " + recordDate.toString();

                    int numAppend = suffix.length() - fullyVaccinated.length();
                    String prefix = " ".repeat(numAppend*2);

                    row.setFullyVaccinated(prefix + fullyVaccinated + "\n" + suffix);
                }

                if (!rateOfVaccination.equals("")){
                    rateOfVaccination = numberFormat.format(Double.parseDouble(rateOfVaccination));
                    String suffix = "last found on " + recordDate.toString();

                    int numAppend = suffix.length() - rateOfVaccination.length();
                    String prefix = " ".repeat(numAppend*2);

                    row.setRateOfVaccination(prefix + rateOfVaccination + "%\n" + suffix);
                }
            }
            else if (recordDate.isEqual(startDate)){
                if (!fullyVaccinated.equals("")) {
                    fullyVaccinated = numberFormat.format(Double.parseDouble(fullyVaccinated));
                    row.setFullyVaccinated(fullyVaccinated);
                }

                if (!rateOfVaccination.equals("")){
                    rateOfVaccination = numberFormat.format(Double.parseDouble(rateOfVaccination))+"%";
                    row.setRateOfVaccination(rateOfVaccination);
                }
            }
		}
		return vaccinationRateTable;
	}

    // chart
    public HashMap<String, XYChart.Series<Number, Number>> getVaccinationRateChart() {
        assert (startDate.isBefore(endDate) || startDate.equals(endDate));

        for (String country : countries) {
            XYChart.Series<Number, Number> series = new XYChart.Series<>();
            series.setName(country);
            vaccinationRateChart.put(country, series);
        }

        for (CSVRecord csvRecord : getFileParser(dataset)) {
            String countryName = csvRecord.get("location");
            if (!countries.contains(countryName))
                continue;

            //String countryISO = csvRecord.get("iso_code");
            String dateRecord = csvRecord.get("date");
            if (dateRecord.equals("")) continue;
            LocalDate recordDate = LocalDate.parse(csvRecord.get("date"), datasetFormatter);
            String rateOfVaccination = csvRecord.get("people_fully_vaccinated_per_hundred");
            if (!recordDate.isBefore(startDate) && !recordDate.isAfter(endDate)) {
                if (!rateOfVaccination.equals("")) {
                    vaccinationRateChart.get(countryName).getData().add(
                            new XYChart.Data<>(recordDate.toEpochDay(), Float.parseFloat(rateOfVaccination))
                    );
                } else {

                }
            }
        }
        return vaccinationRateChart;
    }
}
