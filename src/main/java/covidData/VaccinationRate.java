package covidData;

import org.apache.commons.csv.CSVRecord;

import java.time.LocalDate;
import java.util.*;

import static comp3111.covid.DataAnalysis.getFileParser;

public class VaccinationRate extends CovidData {
	private HashMap<String, VaccinationRateTable> vaccinationRateTable;

	public VaccinationRate(LocalDate date, List<String> countries, String dataset) {
		super(date, countries, dataset);
		vaccinationRateTable = new HashMap<>();
	}



	public HashMap<String, VaccinationRateTable> getVaccinationRateTable() {
		List<String> ISO = countryToISO(countries);
		System.out.println(ISO);
		for (CSVRecord csvRecord : getFileParser(dataset)) {

			if (!ISO.contains(csvRecord.get("iso_code")) || csvRecord.get("date").equals(""))
				continue;

			LocalDate recordDate = LocalDate.parse(csvRecord.get("date"), datasetFormatter);
			if (recordDate.isBefore(this.startDate) || recordDate.isEqual(this.startDate))
			{
				String s1 = csvRecord.get("people_fully_vaccinated");
				String s2 = csvRecord.get("people_fully_vaccinated_per_hundred");
				if (!s1.equals("") && !s2.equals("")) {
					VaccinationRateTable v = new VaccinationRateTable(csvRecord.get("location"), s1, s2);
					vaccinationRateTable.put(csvRecord.get("iso_code"), v);
				}
			}

		}
		return vaccinationRateTable;
	}
}


//	private List<Map<String,String>> fullyVaccinated;
//	private List<Map<String,String>> rateOfVaccination;
//
//	public VaccinationRate(String dataset) {
//		super(dataset);
//	}
//
//	public List<Map<String,String>> getFullyVaccinated() { return fullyVaccinated; }
//
//	public List<Map<String,String>> getRateOfVaccination() { return rateOfVaccination; }
//
//	public void getVaccinationTable()
//    {
//		var table = new HashMap<String, VaccinationRate>();
//		for (CSVRecord csvRecord : getFileParser(dataset)){
//			String dateRecord = csvRecord.get("date");
//			if (dateRecord.equals(""))
//				continue;
//
//			LocalDate recordDate = LocalDate.parse(csvRecord.get("date"), datasetFormatter);
//
//			if (recordDate.isBefore(this.dateStart) || recordDate.isEqual(this.dateStart))
//			{
//				String s1 = csvRecord.get("people_fully_vaccinated");
//				String s2 = csvRecord.get("people_fully_vaccinated_per_hundred");
//				if (!s1.equals("") && !s2.equals("")) {
//					for (int i = 0 ; i < countriesISO.size() ; i++) {
//						if (countriesISO.contains(csvRecord.get("iso_code"))) {
//							fullyVaccinated.get(i).put(csvRecord.get("iso_code"), Long.toString(Long.parseLong(s1)));
//							rateOfVaccination.get(i).put(csvRecord.get("iso_code"), Double.toString(Double.parseDouble(s2)));
//							break;
//						}
//					}
//				}
//			}
//		}
//
//    }
