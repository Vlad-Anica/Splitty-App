package commons;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Date {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;
    public int day;

    public int month;
    public int year;
    public int hour;
    public int minute;

    /**
     *
     * @param id The id of the Date
     * @param day The day of the Date
     * @param month The month of the date
     * @param year The year of the date
     * @param hour The hour of the date
     * @param minute The minute of the date
     */
    public Date(long id, int day, int month, int year, int hour, int minute) {
        this.id = id;
        this.day = day;
        this.month = month;
        this.year = year;
        this.hour = hour;
        this.minute = minute;
    }

    public Date() {

    }


    /**
     *
     * @return the ID of the Date
     */
    public long getId() {
        return id;
    }

    /**
     *
     * @return The Day of the Date
     */
    public int getDay() {
        return day;
    }

    /**
     *
     * @return The month of the Date
     */
    public int getMonth() {
        return month;
    }

    /**
     *
     * @return The year of the Date
     */
    public int getYear() {
        return year;
    }

    /**
     *
     * @return The hour of the Date
     */
    public int getHour() {
        return hour;
    }

    /**
     *
     * @return The Minute of the Date
     */
    public int getMinute() {
        return minute;
    }


    /**
     * Sets the Day of the Date
     * @param day
     */
    public void setDay(int day) {
        this.day = day;
    }

    /**
     * Sets the month of the Date
     * @param month
     */
    public void setMonth(int month) {
        this.month = month;
    }

    /**
     * Sets the year of the Date
     * @param year
     */
    public void setYear(int year) {
        this.year = year;
    }

    /**
     * Sets the Hour of the Date
     * @param hour
     */
    public void setHour(int hour) {
        this.hour = hour;
    }

    /**
     * Sets the Minute of the Date
     * @param minute
     */
    public void setMinute(int minute) {
        this.minute = minute;
    }


    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, MULTI_LINE_STYLE);
    }
}