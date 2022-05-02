package covidData;

import javafx.scene.control.CheckBox;

import java.util.Objects;

/**
 *  Instances of CountrySelection represents a row in the countrySelectionTable.
 *  It contains a countryName and a CheckBox.
 */
public class CountrySelection implements Comparable<CountrySelection>{
    private String name;
    private CheckBox select;

    /**
     * Constructor of CountrySelection initialize the name field and checkBox.
     * @param name country name
     */
    public CountrySelection(String name){
        this.name = name;
        select = new CheckBox();
    }

    /**
     * get country name
     * @return country name
     */
    public String getName() {
        return name;
    }

    /**
     * get checkbox
     * @return checkbox
     */
    public CheckBox getSelect() {
        return select;
    }

    /**
     * Check whether two check boxes have the same country name
     * @param o another object
     * @return whether two check boxes have the same country name
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CountrySelection that = (CountrySelection) o;

        return Objects.equals(name, that.name);
    }

    /**
     * Compare two check boxes by name lexicographically
     * @param o another CountrySelection
     * @return difference in lexicographically order
     */
    @Override
    public int compareTo(CountrySelection o) {
        return this.name.compareTo(o.name);
    }
}
