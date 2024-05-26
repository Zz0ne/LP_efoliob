import java.util.ArrayList;
import java.util.Scanner;

public class Store {
    static ArrayList<Client> clients = null;
    static ArrayList<Item> items = null;
    static Scanner userInput;

    public static void main (String[] args) {
        init();

        while (true) {

            System.out.println("Welcome:");
            System.out.println("0 - Quit.");
            System.out.println("1 - Purchase Goods.");
            System.out.println("2 - Purchase History.");

            int choice = userInput.nextInt();

            switch (choice) {
                case 0:
                    System.exit(0);
                case 1:
                    Client client = getClient();
                    browseShop(client);
                    checkout(client);
                    break;
                case 2:
                    ArrayList<String> results = fetchHistory();
                    System.out.println("Purchase History:");
                    for (String item : results) {
                        System.out.println(item);
                    }
                    System.out.println();
                    break;
                default:
                    System.out.println("Invalid choice");
            }
        }
    }

    public static void init() {
        try {
            KnowledgeBase.init();
        } catch (KnowledgeBase.KnowledgeBaseError e) {
            System.out.println(e.getMessage());
        }
        userInput = new Scanner(System.in);
        clients = KnowledgeBase.getClients();
    }

    public static Client selectClient() {
       for (Client c : clients)
           System.out.println(c.getId() + " " + c.getName());
        System.out.print("Select a client: ");

       int choice = userInput.nextInt();

       if (choice == 0 || choice > clients.size())
           return null;

       return clients.get(choice - 1);
    }

    public static Item selectItem() throws Exception {
        items = KnowledgeBase.getItems();

        for (Item i : items)
            System.out.println(i);

        System.out.print("Select an item: ");
        int choice = userInput.nextInt();

        if (choice == 0 || choice > items.size())
            return null;

        System.out.print("Quantity: ");
        int quantity = userInput.nextInt();

        Item itemInStock = items.get(choice - 1);
        itemInStock.decrementQuantity(quantity);

        Item itemToCart = new Item(itemInStock);
        itemToCart.setQuantity(quantity);

        return itemToCart;
    }

    public static boolean stopBrowsing() {
        while (true) {
            System.out.print("Done Browsing? (y/n): ");
            char choice = userInput.next().charAt(0);

            if (choice == 'y' || choice == 'Y')
                return true;
            else if (choice == 'n' || choice == 'N')
                return false;
            else
                System.out.println("Invalid input");
        }
    }

    public static Client getClient() {
        Client client;

        while ((client = selectClient()) == null)
            System.out.println("Invalid choice");

        return client;
    }

    public static void browseShop(Client client) {
        do {
            try {
                Item item = selectItem();
                assert item != null;
                client.addItemToCart(item);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        } while (!stopBrowsing());
    }

    public static void checkout(Client client) {
        var cart = client.getCart();

        float totalItemPrice = CartOperations.getTotalCartPrice(cart);
        float categoryDiscount = CartOperations.getCategoryDiscount(cart);
        float loyaltyDiscount = CartOperations.getLoyaltyDiscountPrice(client.getLoyaltyYears(), totalItemPrice);
        float shipping = CartOperations.getShippingCost(client.getDistrict());
        float finalPrice = CartOperations.getFinalPrice(totalItemPrice,categoryDiscount,loyaltyDiscount,shipping);

        System.out.println();
        System.out.println("Items:");
        System.out.print(cart);
        System.out.println("Price:");
        System.out.printf("Total item price ----------------------------------------------------%.2f€\n", totalItemPrice);
        System.out.printf("Category discount ---------------------------------------------------%.2f€\n", categoryDiscount);
        System.out.printf("Loyalty discount ----------------------------------------------------%.2f€\n", loyaltyDiscount);
        System.out.printf("Shipping ------------------------------------------------------------%.2f€\n", shipping);
        System.out.printf("Final price ---------------------------------------------------------%.2f€\n", finalPrice);
        System.out.println();
        System.out.println();
        System.out.println("Buy? (y/n): ");

        char choice = userInput.next().charAt(0);
        while (true) {
            if (choice == 'y') {
                PurchaseHistory.add(client.getId(), totalItemPrice, categoryDiscount, loyaltyDiscount, shipping, finalPrice);
                for(Item i: items)
                    KnowledgeBase.updateStock(i);
                break;
            }
            else if (choice == 'n')
                break;
            else
                System.out.println("Invalid choice");
        }
    }

    public static ArrayList<String> fetchHistory() {
        System.out.println("1 - By date.");
        System.out.println("2 - By client.");
        System.out.println("3 - By district.");
        System.out.println("4 - Totals By district.");
        System.out.println("5 - Totals By date.");

        int choice = userInput.nextInt();

        while (true) {
            switch (choice) {
                case 1:
                    System.out.print("Enter date(dd/mm/yyyy): ");
                    String date = userInput.next();
                    return PurchaseHistory.getByDate(date);
                case 2:
                    System.out.print("Enter client id: ");
                    int clientId = userInput.nextInt();
                    return PurchaseHistory.getByClient(clientId);
                case 3:
                    System.out.print("Enter district: ");
                    String district = userInput.next();
                    return PurchaseHistory.getByDistrict(district);
                case 4:
                    System.out.print("Enter district.");
                    String districtId = userInput.next();
                    return PurchaseHistory.getTotalsByDistrict(districtId);
                case 5:
                    System.out.print("Enter date(dd/mm/yyyy): ");
                    String _date = userInput.next();
                    return PurchaseHistory.getTotalsByDate(_date);
                default:
                    System.out.println("Invalid choice");
            }
        }
    }
}
