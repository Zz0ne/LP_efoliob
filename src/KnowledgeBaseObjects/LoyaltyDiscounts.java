package KnowledgeBaseObjects;

public class LoyaltyDiscounts {

    private final int years;
    private final float discount;

    public LoyaltyDiscounts(int years, float discount) {
        this.years = years;
        this.discount = discount;
    }

    public int getYears() {
        return years;
    }

    public float getDiscount() {
        return discount;
    }

    @Override
    public String toString() {
        return years + " " + discount;
    }
}
