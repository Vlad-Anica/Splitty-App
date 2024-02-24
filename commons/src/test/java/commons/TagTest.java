package commons;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TagTest {
    private static final Tag tag1 = new Tag("aaaaaa", "t1");
    private static final String beginTag1 = "Tag: aaaaaa, t1; ";
    private static final Tag tag2 = new Tag("bbbbbb", "t2");
    private static final Tag tag3 = new Tag("aaaaaa", "t1");
    private static final Tag dummyTag = new Tag("a", "b");

    @Test
    void checkConstructor() {
        Tag tag = new Tag("color1", "type1");
        assertTrue(tag.getType().equals("type1"));
        assertTrue(tag.getColor().equals("color1"));
    }
    @Test
    void getColor() {
        assertTrue(tag1.getColor().equals("aaaaaa"));
    }

    @Test
    void setColor() {
        dummyTag.setColor("b");
        assertTrue(dummyTag.getColor().equals("b"));
    }

    @Test
    void getType() {
        assertTrue(tag1.getType().equals("t1"));
    }

    @Test
    void setType() {
        dummyTag.setType("t5");
        assertTrue(dummyTag.getType().equals("t5"));
    }

    @Test
    void testHashCode() {
        assertTrue(tag1.hashCode() == tag3.hashCode());
    }


    @Test
    void testEquals() {
        assertFalse(tag1.equals(tag2));
    }

    @Test
    void testEqualsTrue() {
        assertTrue(tag1.equals(tag1));
    }

    @Test
    void testEqualsWithoutIdFalse() {
        assertFalse(tag1.equalsWithoutId(tag2));
    }

    @Test
    void testEqualsWithoutIdTrue() {
        assertTrue(tag1.equalsWithoutId(tag3));
    }

    @Test
    void testToString() {
        assertTrue(tag1.toString().startsWith(beginTag1));
    }
}