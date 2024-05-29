import java.util.ArrayList;
import java.util.Scanner;

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
            System.out.println("2 - Purchase History.");
            System.out.println("3 - Inventory Management.");

            int choice = userInput.nextInt();

            switch (choice) {
                case 0:
                    System.exit(0);
                case 1:
                    Client client = getClient();
                    browseShop(client);
                    checkout(client);
                    System.out.println();
                    break;
                case 2:
                    fetchHistory();
                    System.out.println();
                    break;
                case 3:
                    manageInventory();
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
        userInput = new MyScanner();
        clients = ClientManagement.getClients();
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
        System.out.println("Buy? (y/n): ");

        char choice = userInput.nextLine().charAt(0);
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

    public static void fetchHistory() {

        while (true) {
            System.out.println("Purchase History:");
            System.out.println("0 - Go back.");
            System.out.println("1 - By date.");
            System.out.println("2 - By client.");
            System.out.println("3 - By district.");
            System.out.println("4 - Totals By district.");
            System.out.println("5 - Totals By date.");
            System.out.print("Choice: ");

            int choice = userInput.nextInt();
            System.out.println();

            ArrayList<String> results = new ArrayList<>();
            switch (choice) {
                case 0:
                    return ;
                case 1:
                    System.out.print("Enter date(dd/mm/yyyy): ");
                    String date = userInput.nextLine();
                    results = PurchaseHistory.getByDate(date);
                    break;
                case 2:
                    System.out.print("Enter client id: ");
                    int clientId = userInput.nextInt();
                    results = PurchaseHistory.getByClient(clientId);
                    break;
                case 3:
                    System.out.print("Enter district: ");
                    String district = userInput.nextLine();
                    results =  PurchaseHistory.getByDistrict(district);
                    break;
                case 4:
                    System.out.print("Enter district: ");
                    String districtId = userInput.nextLine();
                    results.add(PurchaseHistory.getTotalsByDistrict(districtId));
                    break;
                case 5:
                    System.out.print("Enter date(dd/mm/yyyy): ");
                    String _date = userInput.nextLine();
                    results.add(PurchaseHistory.getTotalsByDate(_date));
                    break;
                default:
                    System.out.println("Invalid choice");
                    continue;
            }

            System.out.println();
            for (String item : results) {
                System.out.println(item);
            }
            System.out.println();
        }
    }

    public static void manageInventory() {

        while (true) {
            System.out.println("InventoryManagement:");
            System.out.println("0 - Go back.");
            System.out.println("1 - Check items.");
            System.out.println("2 - Check items by category.");
            System.out.println("3 - Check categories.");
            System.out.println("4 - Add category.");
            System.out.println("5 - Remove category.");
            System.out.println("6 - Edit category.");
            System.out.println("7 - Add item.");
            System.out.println("8 - Remove item.");
            System.out.println("9 - Edit item.");

            int choice = userInput.nextInt();

            switch (choice) {
                case 0:
                    return;
                case 1:
                    var itemList = InventoryManagement.getItems();
                    for (var item : itemList)
                        System.out.println(item);
                    break;
                case 2:
                    System.out.print("Insert category: ");
                    String category = userInput.nextLine();
                    var itemByCatList = InventoryManagement.getItemsByCategory(category);
                    for (Item item : itemByCatList)
                        System.out.println(item);
                    break;
                case 3:
                    var categoryList = InventoryManagement.getCategories();
                    for (var cat : categoryList)
                        System.out.println(cat);
                    break;
                case 4:
                    System.out.print("Enter new category name: ");
                    String catName = userInput.nextLine();
                    System.out.print("Enter category discount: ");
                    float discount = userInput.nextFloat();
                    InventoryManagement.addCategory(catName, discount);
                    break;
                case 5:
                    System.out.print("Enter category to remove: ");
                    String removeCatName = userInput.nextLine();
                    InventoryManagement.removeCategory(removeCatName);
                    break;
                case 6:
                    System.out.print("Enter category name: ");
                    String catToEdit = userInput.nextLine();
                    System.out.print("Enter new discount: ");
                    float newDiscount = userInput.nextFloat();
                    InventoryManagement.editCategory(catToEdit, newDiscount);
                    break;
                case 7:
                    System.out.print("Enter Item name: ");
                    String itemName = userInput.nextLine();
                    System.out.print("Enter category: ");
                    String itemCategory = userInput.nextLine();
                    System.out.print("Enter price: ");
                    float itemPrice = userInput.nextFloat();
                    System.out.print("Enter quantity: ");
                    int quantity = userInput.nextInt();
                    InventoryManagement.addItem(itemName, itemCategory, itemPrice, quantity);
                    break;
                case 8:
                    System.out.print("Enter item id: ");
                    int itemToRemove = userInput.nextInt();
                    InventoryManagement.removeItem(itemToRemove);
                    break;
                case 9:
                    System.out.print("Enter item id: ");
                    int idToEdit = userInput.nextInt();
                    System.out.print("Enter item name: ");
                    String newName = userInput.nextLine();
                    System.out.print("Enter category: ");
                    String newCategory = userInput.nextLine();
                    System.out.print("Enter price: ");
                    float newPrice = userInput.nextFloat();
                    System.out.print("Enter Stock: ");
                    int newStock = userInput.nextInt();
                    InventoryManagement.editItem(idToEdit, newName, newCategory, newPrice, newStock);
                    break;
            }
        }
    }
}
