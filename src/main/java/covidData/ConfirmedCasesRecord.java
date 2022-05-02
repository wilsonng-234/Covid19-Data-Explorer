package covidData;

public class ConfirmedCasesRecord {
    final public static String NOT_FOUND = "NOT FOUND";
    private String country;
    private String totalCases;
    private String totalCasesPerMillion;

    public ConfirmedCasesRecord(String country , String totalCases , String totalCasesPerMillion){
        this.country = country;
        this.totalCases = totalCases;
        this.totalCasesPerMillion = totalCasesPerMillion;
    }

    public String getCountry() {
        return country;
    }
    public String getTotalCases() {
        return totalCases;
    }
    public String getTotalCasesPerMillion() {
        return totalCasesPerMillion;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setTotalCases(String totalCases) {
        this.totalCases = totalCases;
    }

    public void setTotalCasesPerMillion(String totalCasesPerMillion) {
        this.totalCasesPerMillion = totalCasesPerMillion;
    }
}
