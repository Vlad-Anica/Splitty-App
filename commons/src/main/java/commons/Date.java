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

    private Date(){

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