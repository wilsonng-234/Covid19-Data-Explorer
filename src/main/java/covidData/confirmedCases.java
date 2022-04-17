package covidData;

import covidData.covidData;
import org.apache.commons.csv.CSVRecord;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static comp3111.covid.DataAnalysis.getFileParser;

public class confirmedCases extends covidData {
    private int[] totalCases;
    private double[] casesPerMillion;
//    private Map<String,List<Integer>> cumulativeCases;

    public confirmedCases(Date date, String[] countriesIsoCode, String dataset){
        this.date = date;
        this.countriesIsoCode = countriesIsoCode;
        this.dataset = dataset;

        totalCases = new int[countriesIsoCode.length];
        casesPerMillion = new double[countriesIsoCode.length];
        setTotalCases();
        setCasesPerMillion();
    }

//    public confirmedCases(Date startDate,Date endDate,String[] countriesIsoCode, String dataset){
//        this.startDate = date;
//        this.endDate = endDate;
//        this.countriesIsoCode = countriesIsoCode;
//        this.dataset = dataset;
//
//        cumulativeCases = new HashMap<>();
//        for (String country : countriesIsoCode){
//            cumulativeCases.put(country,new ArrayList<>(365 + 365 * (endDate.getYear() - startDate.getYear())));
//        }
//    }

    public int[] getTotalCases() {
        return totalCases;
    }
    public double[] getCasesPerMillion() {
        return casesPerMillion;
    }

    private void setTotalCases() {
        for (CSVRecord csvRecord : getFileParser(dataset)){
            String dateRecord = csvRecord.get("date");
            if (dateRecord.equals(""))
                continue;

            String[] dateRecordInfo = dateRecord.split("/");
            Date recordDate = new Date(Integer.parseInt(dateRecordInfo[1]),
                    Integer.parseInt(dateRecordInfo[0]),
                    Integer.parseInt(dateRecordInfo[1]));

            if (recordDate.isBefore(date) || recordDate.isEqual(date))
            {
                String total_cases_record = csvRecord.get("total_cases");
                if (total_cases_record.equals(""))
                    continue;

                for (int i = 0 ; i < countriesIsoCode.length ; i++) {
                    if (countriesIsoCode[i].equals(csvRecord.get("iso_code"))) {
                        totalCases[i] = Math.max(totalCases[i],Integer.parseInt(total_cases_record));
                        break;
                    }
                }
            }
        }
    }

    private void setCasesPerMillion() {
        for (CSVRecord csvRecord : getFileParser(dataset)){
            String dateRecord = csvRecord.get("date");
            if (dateRecord.equals(""))
                continue;

            String[] dateRecordInfo = dateRecord.split("/");
            Date recordDate = new Date(Integer.parseInt(dateRecordInfo[1]),
                    Integer.parseInt(dateRecordInfo[0]),
                    Integer.parseInt(dateRecordInfo[1]));

            if (recordDate.isBefore(date) || recordDate.isEqual(date))
            {
                String total_cases_per_million_record = csvRecord.get("total_cases_per_million");
                if (total_cases_per_million_record.equals(""))
                    continue;

                for (int i = 0 ; i < countriesIsoCode.length ; i++) {
                    if (countriesIsoCode[i].equals(csvRecord.get("iso_code"))) {
                        casesPerMillion[i] = Math.max(casesPerMillion[i],Double.parseDouble(total_cases_per_million_record));
                        break;
                    }
                }
            }
        }
    }

//    private void setCumulativeCases(){
//        for (CSVRecord csvRecord : getFileParser(dataset)){
//            for (String country : countriesIsoCode){
//                if (country.equals(csvRecord.get("iso_code"))) {
//                    List<Integer> cumulativeCasesSoFar = cumulativeCases.get(country);
//
//
//                    cumulativeCasesSoFar.get(cumulativeCasesSoFar.size() - 1);
//                }
//            }
//        }
//    }
}
