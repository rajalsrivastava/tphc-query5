package model;

public class PartSupp {

    private int partKey;
    private int suppKey;
    private double supplyCost;
    private int availableQuantity;
    private String comment;

    public PartSupp(int partKey, int suppKey, double supplyCost, int availableQuantity, String comment) {
        this.partKey = partKey;
        this.suppKey = suppKey;
        this.supplyCost = supplyCost;
        this.availableQuantity = availableQuantity;
        this.comment = comment;
    }

    public int getPartKey() {
        return partKey;
    }

    public int getSuppKey() {
        return suppKey;
    }

    // Add more getters if you need them
}
