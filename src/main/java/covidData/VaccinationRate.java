package covidData;

import javafx.scene.chart.XYChart;
import org.apache.commons.csv.CSVRecord;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;

import static comp3111.covid.DataAnalysis.getFileParser;

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
        final String NOT_FOUND = "NOT FOUND";

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

            if (recordDate.isEqual(startDate))
            {
                //String iso_code = csvRecord.get("iso_code");

                String fullyVaccinated = csvRecord.get("people_fully_vaccinated");
                String rateOfVaccination = csvRecord.get("people_fully_vaccinated_per_hundred");

                if (fullyVaccinated.equals(""))
                    fullyVaccinated = NOT_FOUND;

                if (rateOfVaccination.equals(""))
                    rateOfVaccination = NOT_FOUND;

                VaccinationRateRecord row = vaccinationRateTable.get(countryName);

                row.setFullyVaccinated(fullyVaccinated);
                row.setRateOfVaccination(rateOfVaccination);
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
