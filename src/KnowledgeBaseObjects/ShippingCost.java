package KnowledgeBaseObjects;

public class ShippingCost {

    private final String district;
    private final float cost;

    public ShippingCost(String district, float cost) {
        this.district = district;
        this.cost = cost;
    }

    public String getDistrict() {
        return district;
    }

    public float getCost() {
        return cost;
    }

    @Override
    public String toString() {
        return district + " " + cost;
    }
}
