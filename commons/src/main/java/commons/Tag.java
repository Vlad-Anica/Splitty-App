package commons;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.Objects;

@Entity
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String color;
    private String type;

    /**
     * constructor for Tag class
     * @param color color of the tag
     * @param type type of the tag
     */
    public Tag(String color, String type) {
        this.color = color;
        this.type = type;
    }

    /**
     * constructor with no args
     */
    public Tag(){}

    /**
     * getter for id
     * @param id id of object
     */
    public void getId(long id) {
        this.id = id;
    }

    /**
     * getter for color
     * @return color
     */
    public String getColor() {
        return color;
    }

    /**
     * setter for color
     * @param color new color
     */
    public void setColor(String color) {
        this.color = color;
    }

    /**
     * getter for type
     * @return type new type
     */
    public String getType() {
        return type;
    }

    /**
     * setter for type
     * @param type new type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * checks if it is equal to an Object o
     * @param obj the object
     * @return true if equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    /**
     * hash function
     * @return hash code of the object
     */
    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    /**
     * checks if its fields are equal to that of another object, without looking at the ids
     * @param o object to check
     * @return true if equal, false otherwise
     */
    public boolean equalsWithoutId(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tag tag = (Tag) o;
        return Objects.equals(color, tag.color) && Objects.equals(type, tag.type);
    }


    /**
     * turns the object into a readable string
     * @return a human readable string
     */
    @Override
    public String toString() {
        return "Tag: " + color + ", " + type + "; " + id;
    }
}
