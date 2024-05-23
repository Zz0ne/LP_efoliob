import org.jpl7.*;

import java.util.ArrayList;
import java.util.Map;

public class KnowledgeBase {

    public void init() throws KnowledgeBaseError {
        Query consultQuery = new Query("consult('src/dataBase/store.pl')");
        if( consultQuery.hasSolution()) {
            System.out.println("Knowledge base initialized.");
        } else {
            throw new KnowledgeBaseError("Failed to initialize Knowledge base!");
        }
    }

    public ArrayList<Client> getClients() {
        ArrayList<Client> clients = new ArrayList<>();

        Query retrieveClients = new Query("client(Id, Name, District, Loyalty)");

        while (retrieveClients.hasMoreSolutions()) {
            Map<String, Term> solution = retrieveClients.nextSolution();

            int id = solution.get("Id").intValue();
            String name = solution.get("Name").toString();
            String district = solution.get("District").toString();
            int loyalty = solution.get("Loyalty").intValue();

            Client client = new Client(id, name, district, loyalty);
            clients.add(client);
        }

        return clients;
    }

    public ArrayList<Item> getItems() {
        ArrayList<Item> items = new ArrayList<>();

        Query retrieveItems = new Query("item(Id, Name, Category, Price, Stock)");

        while(retrieveItems.hasMoreSolutions()) {
            Map<String, Term> solution = retrieveItems.nextSolution();

            int id = solution.get("Id").intValue();
            String name = solution.get("Name").toString();
            String category = solution.get("Category").toString();
            float price = solution.get("Price").floatValue();
            int stock = solution.get("Stock").intValue();

            Item item = new Item(id, name, category, price, stock);
            items.add(item);
        }
        return items;
    }

    public static class KnowledgeBaseError extends Exception {
        public KnowledgeBaseError(String message) {
            super(message);
        }
    }
}
