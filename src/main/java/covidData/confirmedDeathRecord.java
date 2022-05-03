package covidData;

/**
 *  Instances of ConfirmedDeathRecord contain totalDeath and totalDeathPerMillion for a country
 */
public class confirmedDeathRecord {
    /**
     *  NOT_FOUND String indicates datum is not found as of the date
     */
    final public static String NOT_FOUND = "NOT FOUND";
    String country;
    String totalDeath;
    String totalDeathPerMillion;

    /**
     *
     * @param country               The selected country
     * @param totalDeath            String formatted totalDeath datum
     * @param totalDeathPerMillion  String formatted totalDeathPerMillion datum
     */
    public confirmedDeathRecord(String country , String totalDeath , String totalDeathPerMillion){
        this.country = country;
        this.totalDeath = totalDeath;
        this.totalDeathPerMillion = totalDeathPerMillion;
    }

    /**
     *
     * @return country
     */
    public String getCountry() {
        return country;
    }
    /**
     *
     * @return totalDeath
     */
    public String getTotalDeath() {
        return totalDeath;
    }
    /**
     *
     * @return totalDeathPerMillion
     */
    public String getTotalDeathPerMillion() {
        return totalDeathPerMillion;
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
     * Set totalDeath
     * @param totalDeath totalDeath to set
     */
    public void setTotalDeath(String totalDeath) {
        this.totalDeath = totalDeath;
    }

    /**
     * Set totalDeathPerMillion
     * @param totalDeathPerMillion totalDeathPerMillion to set
     */
    public void setTotalDeathPerMillion(String totalDeathPerMillion) {
        this.totalDeathPerMillion = totalDeathPerMillion;
    }
}

