package covidData;

import org.apache.commons.csv.CSVRecord;

import static comp3111.covid.DataAnalysis.getFileParser;

public class confirmedCases extends covidData{
    private int[] totalCases;
    private int[] casesPerMillion;

    public confirmedCases(Date date,String[] countriesIsoCode,String dataset){
        this.date = date;
        this.countriesIsoCode = countriesIsoCode;
        this.dataset = dataset;

        totalCases = new int[countriesIsoCode.length];
        setTotalCases();

        casesPerMillion = new int[countriesIsoCode.length];
    }

    public int[] getTotalCases() {
        return totalCases;
    }
    public int[] getCasesPerMillion() {
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
}
