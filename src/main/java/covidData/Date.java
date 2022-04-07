package covidData;

public class Date {
    private int year;
    private int month;
    private int day;

    public Date(int year, int month, int day){
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }
    public boolean isBefore(Date other){
        if (year < other.year)
            return true;
        else if (year > other.year)
            return false;
        else {    // year == other.year
            if (month < other.month)
                return true;
            else if (month > other.month)
                return false;
            else {   // month == other.month
                if (day < other.day)
                    return true;
                else if (day > other.day)
                    return false;
                else
                    return false;
            }
        }
    }

    public boolean isEqual(Date other){
        return year == other.year && month == other.month && day == other.day;
    }

    public boolean isAfter(Date other){
        return !isBefore(other) && !isEqual(other);
    }

    @Override
    public String toString() {
        return "Date{" +
                "year=" + year +
                ", month=" + month +
                ", day=" + day +
                '}';
    }
}
