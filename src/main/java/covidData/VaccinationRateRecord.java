package covidData;

/**
 * Instances of vaccinationRate contain fully vaccinated and rate of vaccination for a country
 * Vaccination Table Column Structure
 * @author Chiu Chi Shing
 * @since 2021-10-09
 */
public class VaccinationRateRecord {
    /**
     *  NOT_FOUND String indicates datum is not found as of the date
     */
    final public static String NOT_FOUND = "NOT FOUND";
    String country;
    String fullyVaccinated;
    String rateOfVaccination;

    /**
     *
     * @param country               Name of the country in the vaccination data.
     * @param fullyVaccinated       Total number of fully vaccinated individual in the country until a day.
     * @param rateOfVaccination     Rate of vaccination in the country.
     */
    public VaccinationRateRecord(String country, String fullyVaccinated, String rateOfVaccination) {
        this.country = country;
        this.fullyVaccinated = fullyVaccinated;
        this.rateOfVaccination = rateOfVaccination;
    }

    /**
     * Get the name of the country recorded.
     * @return String representing the country's name
     */
    public String getCountry() { return country; }
    /**
     * Get the amount of fully vaccinated people in the country until a day
     * @return String on number of fully vaccinated people
     */
    public String getFullyVaccinated() { return fullyVaccinated; }
    /**
     * Get the amount of vaccinated people per population in the country until a day.
     * @return String on number of fully vaccinated people per population
     */
    public String getRateOfVaccination() { return rateOfVaccination; }

    /**
     * Set the name of the country recorded.
     */
    public void setCountry(String country) { this.country = country; }
    /**
     * Set the amount of fully vaccinated people in the country until a day
     */
    public void setFullyVaccinated(String fullyVaccinated) { this.fullyVaccinated = fullyVaccinated; }
    /**
     * Set the amount of vaccinated people per population in the country until a day.
     */
    public void setRateOfVaccination(String rateOfVaccination) { this.rateOfVaccination = rateOfVaccination; }
}
