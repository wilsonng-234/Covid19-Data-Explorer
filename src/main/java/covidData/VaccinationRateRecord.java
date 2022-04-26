package covidData;

public class VaccinationRateRecord {
    final public static String NOT_FOUND = "NOT FOUND";
    String country;
    String fullyVaccinated;
    String rateOfVaccination;

    public VaccinationRateRecord(String country, String fullyVaccinated, String rateOfVaccination) {
        this.country = country;
        this.fullyVaccinated = fullyVaccinated;
        this.rateOfVaccination = rateOfVaccination;
    }

    public String getCountry() { return country; }
    public String getFullyVaccinated() { return fullyVaccinated; }
    public String getRateOfVaccination() { return rateOfVaccination; }

    public void setCountry(String country) { this.country = country; }
    public void setFullyVaccinated(String fullyVaccinated) { this.fullyVaccinated = fullyVaccinated; }
    public void setRateOfVaccination(String rateOfVaccination) { this.rateOfVaccination = rateOfVaccination; }
}
