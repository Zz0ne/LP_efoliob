public class Client {

    private final int id;
    private final String name;
    private final String district;
    private final int loyaltyYears;
    private final ShoppingCart cart;

    public Client(int id, String name, String district, int loyaltyYears) {
        this.id = id;
        this.name = name;
        this.district = district;
        this.loyaltyYears = loyaltyYears;
        this.cart = new ShoppingCart();
    }

    public int getId(){
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public ShoppingCart getCart() {
        return this.cart;
    }

    public int getLoyaltyYears() {
        return this.loyaltyYears;
    }

    public String getDistrict() {
        return this.district;
    }

    public void addItemToCart(Item item){
        this.cart.addItem(item);
    }
}