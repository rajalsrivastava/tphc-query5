package model;

public class Region {

    public int regionKey;
    public String name;
    public String comment;

    @Override
    public String toString() {
        return "Region#" + regionKey + " - " + name;
    }
}
