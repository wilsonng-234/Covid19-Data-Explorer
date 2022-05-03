package covidData;

/**
 *  Instances of ConfirmedCasesRecord contain totalCases and totalCasesPerMillion for a country
 */
public class ConfirmedCasesRecord {
    /**
     *  NOT_FOUND String indicates datum is not found as of the date
     */
    final public static String NOT_FOUND = "NOT FOUND";

    String country;
    String totalCases;
    String totalCasesPerMillion;

    

    /**
     *
     * @param country               The selected country
     * @param totalCases            String formatted totalCases datum
     * @param totalCasesPerMillion  String formatted totalCasesPerMillion datum
     */
    public ConfirmedCasesRecord(String country , String totalCases , String totalCasesPerMillion){
        this.country = country;
        this.totalCases = totalCases;
        this.totalCasesPerMillion = totalCasesPerMillion;
    }

    /**
     * Get countryName
     * @return country countryName
     */
    public String getCountry() {
        return country;
    }

    /**
     * Get totalCases datum
     * @return totalCases totalCases datum
     */
    public String getTotalCases() {
        return totalCases;
    }

    /**
     * Get totalCasesPerMillion datum
     * @return totalCasesPerMillion totalCasesPerMillion datum
     */
    public String getTotalCasesPerMillion() {
        return totalCasesPerMillion;
    }

    /**
     * Set the country
     * @param country country to set
     *
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * Set totalCases
     * @param totalCases totalCases to set
     */
    public void setTotalCases(String totalCases) {
        this.totalCases = totalCases;
    }

    /**
     * Set totalCasesPerMillion
     * @param totalCasesPerMillion totalCasesPerMillion to set
     */
    public void setTotalCasesPerMillion(String totalCasesPerMillion) {
        this.totalCasesPerMillion = totalCasesPerMillion;
    }
}
