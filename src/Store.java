import Helpers.KnowledgeBase;
import Helpers.MyScanner;
import Helpers.ShoppingCart;
import KnowledgeBaseObjects.Client;
import KnowledgeBaseObjects.Item;

import java.util.ArrayList;

public class Store {
    static ArrayList<Client> clients = null;
    static ArrayList<Item> items = null;
    static MyScanner userInput;

    public static void main (String[] args) {
        init();

        while (true) {

            System.out.println("Welcome:");
            System.out.println("0 - Quit.");
            System.out.println("1 - Purchase Goods.");
            System.out.println("2 - Consult History.");
            System.out.println("3 - Manage Inventory.");
            System.out.println("4 - Manage Costs and Discounts.");
            System.out.println("5 - Manage Clients.");

            System.out.print("Choice: ");
            int choice = userInput.nextInt();

            switch (choice) {
                case 0:
                    userInput.close();
                    System.exit(0);
                case 1:
                    Client client = getClient();
                    browseShop(client);
                    checkout(client);
                    break;
                case 2:
                    PurchaseHistory.consult(userInput);
                    break;
                case 3:
                    InventoryManagement.manage(userInput);
                    break;
                case 4:
                    CostAndDiscountManagement.manage(userInput);
                    break;
                case 5:
                    ClientManagement.manage(userInput);
                    break;
                default:
                    System.out.println("Invalid choice");
            }
            System.out.println();
        }
    }

    public static void init() {
        try {
            KnowledgeBase.init();
        } catch (KnowledgeBase.KnowledgeBaseError e) {
            System.out.println(e.getMessage());
        }
        userInput = new MyScanner();
        clients = ClientManagement.getClients();
    }

    public static Client selectClient() {
       for (Client c : clients)
           System.out.println(c);
        System.out.print("Select a client: ");

       int choice = userInput.nextInt();

       if (choice == 0 || choice > clients.size())
           return null;

       return clients.get(choice - 1);
    }

    public static Item selectItem() throws Exception {
        items = InventoryManagement.getItems();

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
            char choice = userInput.nextLine().charAt(0);

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
        ShoppingCart cart = client.getCart();

        if (cart.isEmpty()) return;

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
        System.out.println("Buy? (y/n): ");

        char choice = userInput.nextLine().charAt(0);
        while (true) {
            if (choice == 'y') {
                PurchaseHistory.add(client.getId(), totalItemPrice, categoryDiscount, loyaltyDiscount, shipping, finalPrice);
                for(Item i: items)
                    InventoryManagement.updateStock(i);
                break;
            }
            else if (choice == 'n')
                break;
            else
                System.out.println("Invalid choice");
        }
    }
}
