import java.util.ArrayList;

public class Store {
    public static void main (String[] args) {

        var kb = new KnowledgeBase();
        try {
            kb.init();
        } catch (KnowledgeBase.KnowledgeBaseError e) {
            System.out.println(e.getMessage());
        }

        ArrayList<Client> clients = kb.getClients();
        ArrayList<Item> items = kb.getItems();

        for (Client client: clients)
            System.out.println(client.getName());
        for (Item item: items)
            System.out.println(item.getId());

    }
}
