package covidData;

import static comp3111.covid.DataAnalysis.getFileParser;

import java.util.ArrayList;

import org.apache.commons.csv.CSVRecord;

public class newConfirmedDeath extends covidData {
	private int totalDeaths;
    private double DeathsPerMillion;

    public newConfirmedDeath(Date date, String[] countriesIsoCode, String dataset){
        this.date = date;
        this.countriesIsoCode = countriesIsoCode;
        this.dataset = dataset;

        setTotalDeaths();
        setDeathsPerMillion();
       
    }

    public int getTotalDeaths() {
        return totalDeaths;
    }
    public double getDeathsPerMillion() {
        return DeathsPerMillion;
    }

    private void setTotalDeaths() {
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
                String total_deaths_record = csvRecord.get("total_deaths");
                if (total_deaths_record.equals(""))
                    continue;

                for (int i = 0 ; i < countriesIsoCode.length ; i++) {
                    if (countriesIsoCode[i].equals(csvRecord.get("iso_code"))) {
                        totalDeaths = Math.max(totalDeaths,Integer.parseInt(total_deaths_record));
                        break;
                    }
                }
            }
        }
    }
    
    private void setDeathsPerMillion() {
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
                String total_deaths_per_million_record = csvRecord.get("total_deaths_per_million");
                if (total_deaths_per_million_record.equals(""))
                    continue;

                for (int i = 0 ; i < countriesIsoCode.length ; i++) {
                    if (countriesIsoCode[i].equals(csvRecord.get("iso_code"))) {
                        DeathsPerMillion = Math.max(DeathsPerMillion,Double.parseDouble(total_deaths_per_million_record));
                        break;
                    }
                }
            }
        }
    }
}
