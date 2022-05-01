package covidData;

import javafx.scene.control.CheckBox;

import java.util.Objects;

public class CountrySelection implements Comparable<CountrySelection>{
    private String name;
    private CheckBox select;

    public CountrySelection(String name){
        this.name = name;
        select = new CheckBox();
    }

    public String getName() {
        return name;
    }

    public CheckBox
    getSelect() {
        return select;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CountrySelection that = (CountrySelection) o;

        return Objects.equals(name, that.name);
    }

    @Override
    public int compareTo(CountrySelection o) {
        return this.name.compareTo(o.name);
    }
}
