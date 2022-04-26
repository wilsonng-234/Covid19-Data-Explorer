package covidData;

import java.time.LocalDate;
import java.util.HashSet;

public abstract class CovidData {
    protected LocalDate startDate;
    protected LocalDate endDate;
    protected String dataset;
    protected HashSet<String> countries;

//    static final DateTimeFormatter datasetFormatter = DateTimeFormatter.ofPattern("M/d/yyyy");

    protected CovidData(LocalDate date, HashSet<String> countries, String dataset){
        this.startDate = this.endDate = date;

        this.countries = new HashSet<>(countries);
        this.dataset = dataset;
    }
    protected CovidData(LocalDate startDate, LocalDate endDate, HashSet<String> countries, String dataset){
        this(startDate, countries, dataset);

        this.endDate = endDate;
    }

    public LocalDate getstartDate(){ return startDate; }
    public LocalDate getendDate(){ return endDate; }
    public HashSet<String>  getCountriesIsoCode() { return countries; }
    public String getDataset() { return dataset; }

//    public List<String> countryToISO (List<String> countries){
//        List<String> ISO = new ArrayList<>();
//        for (CSVRecord csvRecord : getFileParser(dataset)) {
//            if (countries.contains(csvRecord.get("location")))
//                if (!ISO.contains(csvRecord.get("iso_code")))
//                    ISO.add(csvRecord.get("iso_code"));
//        }
//        return ISO;
//    }
}