package covidData;

public class VaccinationRateTable {
    String country;
    String fullyVaccinated;
    String rateOfVaccination;

    public VaccinationRateTable(String country, String fullyVaccinated, String rateOfVaccination) {
        this.country = country;
        this.fullyVaccinated = fullyVaccinated;
        this.rateOfVaccination = rateOfVaccination;
    }

    public String getCountry() {
        return country;
    }

    public String getFullyVaccinated() {
        return fullyVaccinated;
    }

    public String getRateOfVaccination() {
        return rateOfVaccination;
    }
}
