//package covidData;
//
//import javafx.scene.chart.XYChart;
//import org.apache.commons.csv.CSVRecord;
//
//import java.time.LocalDate;
//import java.util.HashMap;
//import java.util.HashSet;
//
//import static comp3111.covid.DataAnalysis.getFileParser;
//
//public class VaccinationRate extends CovidData {
//    private HashMap<String, VaccinationRateRow> vaccinationRateTable;
//    private HashMap<String, XYChart.Series<String, Float>>  vaccinationRateChart;
//
//    public VaccinationRate(LocalDate date, HashSet<String> countries, String dataset) {
//        super(date, countries, dataset);
//        vaccinationRateTable = new HashMap<>();
//    }
//    public VaccinationRate(LocalDate startDate, LocalDate endDate, HashSet<String> countries, String dataset) {
//        this(startDate, countries, dataset);
//        this.endDate = endDate;
//        vaccinationRateChart = new HashMap<>();
//    }
//
//
//
//    public HashMap<String, VaccinationRateRow> getVaccinationRateTable() {
//        final String NOT_FOUND = "NOT FOUND";
//
//        for (String countryName : countries)
//            vaccinationRateTable.put(countryName,new VaccinationRateRow(countryName,NOT_FOUND,NOT_FOUND));
//
//        for (CSVRecord csvRecord : getFileParser(dataset)) {
//            String countryName = csvRecord.get("location");
//            if (!countries.contains(countryName))
//                continue;
//
//            //String countryISO = csvRecord.get("iso_code");
//            String dateRecord = csvRecord.get("date");
//            if (dateRecord.equals("")) {
//                continue;
//            }
//
//            String[] dateRecordInfo = dateRecord.trim().split("/");
//            LocalDate recordDate = LocalDate.of(
//                    Integer.parseInt(dateRecordInfo[2]),
//                    Integer.parseInt(dateRecordInfo[0]),
//                    Integer.parseInt(dateRecordInfo[1]));
//
//            if (recordDate.isEqual(startDate))
//            {
//                //String iso_code = csvRecord.get("iso_code");
//
//                String fullyVaccinated = csvRecord.get("people_fully_vaccinated");
//                String rateOfVaccination = csvRecord.get("people_fully_vaccinated_per_hundred");
//
//                if (fullyVaccinated.equals(""))
//                    fullyVaccinated = NOT_FOUND;
//
//                if (rateOfVaccination.equals(""))
//                    rateOfVaccination = NOT_FOUND;
//
//                VaccinationRateRow row = vaccinationRateTable.get(countryName);
//
//                row.setFullyVaccinated(fullyVaccinated);
//                row.setRateOfVaccination(rateOfVaccination);
//            }
//        }
//        return vaccinationRateTable;
//    }
//
//
//    public HashMap<String, XYChart.Series<String, Float>> getVaccinationRateChart() {
//
//        for (String country : countries) {
//            XYChart.Series<String, Float> series = new XYChart.Series<>();
//
//            for (CSVRecord csvRecord : getFileParser(dataset)) {
//
//                String countryName = csvRecord.get("location");
//                if (!country.equals(countryName))
//                    continue;
//
//                //String countryISO = csvRecord.get("iso_code");
//                String dateRecord = csvRecord.get("date");
//                if (dateRecord.equals("")) {
//                    continue;
//                }
//
//                LocalDate recordDate = LocalDate.of(
//                        Integer.parseInt(dateRecord.trim().split("/")[2]),
//                        Integer.parseInt(dateRecord.trim().split("/")[0]),
//                        Integer.parseInt(dateRecord.trim().split("/")[1]));
//                String rate = csvRecord.get("people_fully_vaccinated_per_hundred");
//                Float rateOfVaccination;
//                if (rate == null || rate.equals("")) rateOfVaccination = 0.0f;
//                else rateOfVaccination = Float.parseFloat(rate);
//
//                if ((recordDate.isAfter(startDate) || recordDate.equals(startDate)) &&
//                        (recordDate.isBefore(endDate) || recordDate.equals(endDate))){
//
//                    series.getData().add(new XYChart.Data<>(recordDate.toString(), rateOfVaccination));
//                    System.out.println(recordDate);
//                    System.out.println( rateOfVaccination);
//                }
//            }
//            series.setName(country);
//            vaccinationRateChart.put(country, series);
//        }
//
//        return vaccinationRateChart;
//    }
//}
//
//
////	private List<Map<String,String>> fullyVaccinated;
////	private List<Map<String,String>> rateOfVaccination;
////
////	public VaccinationRate(String dataset) {
////		super(dataset);
////	}
////
////	public List<Map<String,String>> getFullyVaccinated() { return fullyVaccinated; }
////
////	public List<Map<String,String>> getRateOfVaccination() { return rateOfVaccination; }
////
////	public void getVaccinationTable()
////    {
////		var table = new HashMap<String, VaccinationRate>();
////		for (CSVRecord csvRecord : getFileParser(dataset)){
////			String dateRecord = csvRecord.get("date");
////			if (dateRecord.equals(""))
////				continue;
////
////			LocalDate recordDate = LocalDate.parse(csvRecord.get("date"), datasetFormatter);
////
////			if (recordDate.isBefore(this.dateStart) || recordDate.isEqual(this.dateStart))
////			{
////				String s1 = csvRecord.get("people_fully_vaccinated");
////				String s2 = csvRecord.get("people_fully_vaccinated_per_hundred");
////				if (!s1.equals("") && !s2.equals("")) {
////					for (int i = 0 ; i < countriesISO.size() ; i++) {
////						if (countriesISO.contains(csvRecord.get("iso_code"))) {
////							fullyVaccinated.get(i).put(csvRecord.get("iso_code"), Long.toString(Long.parseLong(s1)));
////							rateOfVaccination.get(i).put(csvRecord.get("iso_code"), Double.toString(Double.parseDouble(s2)));
////							break;
////						}
////					}
////				}
////			}
////		}
////
////    }
