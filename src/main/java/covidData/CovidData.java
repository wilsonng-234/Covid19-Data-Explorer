package covidData;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;

/**
 *  CovidData is an abstract class that contains user input : startDate,endDate,dataset,countries.
 */
public abstract class CovidData {
    protected LocalDate startDate;
    protected LocalDate endDate;
    protected String dataset;
    protected HashSet<String> countries;

    static final DateTimeFormatter datasetFormatter = DateTimeFormatter.ofPattern("M/d/yyyy");

    protected CovidData(LocalDate date, HashSet<String> countries, String dataset){
        this.startDate = this.endDate = date;

        this.countries = new HashSet<>(countries);
        this.dataset = dataset;
    }
    protected CovidData(LocalDate startDate, LocalDate endDate, HashSet<String> countries, String dataset){
        this(startDate, countries, dataset);

        this.endDate = endDate;
    }

    /**
     * get user input startDate
     * @return startDate
     */
    public LocalDate getstartDate(){ return startDate; }

    /**
     * get user input endDate
     * @return endDate
     */
    public LocalDate getendDate(){ return endDate; }

    /**
     * get user input countries
     * @return HashSet<String> containing the user input countries
     */
    public HashSet<String> getCountries() { return countries; }

    /**
     * get dataset name
     * @return dataset name
     */
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