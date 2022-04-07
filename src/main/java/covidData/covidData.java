package covidData;

public abstract class covidData {
    protected Date date;
    protected String[] countriesIsoCode;
    protected String dataset;

    public Date getDate(){ return date; }
    public String[] getCountriesIsoCode() { return countriesIsoCode; }
    public String getDataset() { return dataset; }
}
