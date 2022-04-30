package covidData;

public class confirmedDeathRecord {
    final public static String NOT_FOUND = "NOT FOUND";
    String country;
    String totalDeath;
    String totalDeathPerMillion;

    public confirmedDeathRecord(String country , String totalDeath , String totalDeathPerMillion){
        this.country = country;
        this.totalDeath = totalDeath;
        this.totalDeathPerMillion = totalDeathPerMillion;
    }

    public String getCountry() {
        return country;
    }
    public String getTotalDeath() {
        return totalDeath;
    }
    public String getTotalDeathPerMillion() {
        return totalDeathPerMillion;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setTotalDeath(String totalDeath) {
        this.totalDeath = totalDeath;
    }

    public void setTotalDeathPerMillion(String totalDeathPerMillion) {
        this.totalDeathPerMillion = totalDeathPerMillion;
    }
}
