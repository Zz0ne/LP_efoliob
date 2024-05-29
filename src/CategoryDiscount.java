public class CategoryDiscount {
    private final String category;
    private final float discount;

    public CategoryDiscount(String category, float discount) {
        this.category = category;
        this.discount = discount;
    }

    public String getCategory() {
        return category;
    }

    public float getDiscount() {
        return discount;
    }

    @Override
    public String toString() {
        return category + " " + discount;
    }
}
