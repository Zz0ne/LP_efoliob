import java.util.ArrayList;
import java.util.Scanner;

public class Store {
    static KnowledgeBase kb = new KnowledgeBase();
    static ArrayList<Client> clients = null;
    static ArrayList<Item> items = null;
    static Scanner userInput;

    public static void init() {
        try {
            kb.init();
        } catch (KnowledgeBase.KnowledgeBaseError e) {
            System.out.println(e.getMessage());
        }
        userInput = new Scanner(System.in);
        clients = kb.getClients();
        items = kb.getItems();
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

    public static void main (String[] args) {

        init();
        Client client = null;

        while ((client = selectClient()) == null)
            System.out.println("Invalid choice");

        do {
            try {
                Item item = selectItem();
                assert item != null;
                client.addItemToCart(item);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        } while (!stopBrowsing());

        System.out.println(client.getCart());
    }
}
