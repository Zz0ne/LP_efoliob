import org.jpl7.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Map;

public class KnowledgeBase {

    public void init() throws KnowledgeBaseError {
        Query consultQuery = new Query("consult('backend/dataBase/store.pl')");
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

            // Remove os ';
            // Ex: 'Alice' -> Alice
            name = name.substring(1, name.length() - 1);
            district = district.substring(1, district.length() - 1);

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

    public void addPurchaseHistory(int id ,float totalPrice, float catDisc, float loyalDisc, float shipping, float finPrice) {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String formattedDate = currentDate.format(formatter);

        String prologQuery = String.format(
                "assertz(history(%d, '%s', %f, %f, %f, %f, %f))",
                id, formattedDate, totalPrice, catDisc, loyalDisc, shipping, finPrice
        );

        Query addPurchase = new Query(prologQuery);
        if (addPurchase.hasSolution()) {
            System.out.println("Purchase history added successfully.");
        } else {
            System.out.println("Failed to add purchase history.");
        }
    }

    public void updateStock(Item item) {
        String prologQuery = String.format(
                "update_stock(%d, %d)",
                item.getId(), item.getQuantity()
        );

        Query updateStock = new Query(prologQuery);
        if (!updateStock.hasSolution()) {
            System.out.printf("Failed to update %s stock.\n", item.getName());
        }
    }

    public static class KnowledgeBaseError extends Exception {
        public KnowledgeBaseError(String message) {
            super(message);
        }
    }
}
