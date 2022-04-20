package covidData;

import covidData.covidData;
import org.apache.commons.csv.CSVRecord;

import static comp3111.covid.DataAnalysis.getFileParser;

public class confirmedDeath extends covidData {
    private int[] totalDeaths;
    private int[] DeathsPerMillion;

    public confirmedDeath(Date date, String[] countriesIsoCode, String dataset){
        this.date = date;
        this.countriesIsoCode = countriesIsoCode;
        this.dataset = dataset;

        totalDeaths = new int[countriesIsoCode.length];
        setTotalDeaths();

        DeathsPerMillion = new int[countriesIsoCode.length];
    }

    public int[] getTotalDeaths() {
        return totalDeaths;
    }
    public int[] getDeathsPerMillion() {
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
                        totalDeaths[i] = Math.max(totalDeaths[i],Integer.parseInt(total_deaths_record));
                        break;
                    }
                }
            }
        }
    }
}
