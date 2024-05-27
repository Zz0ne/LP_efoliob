import org.jpl7.Term;

import java.util.ArrayList;
import java.util.Map;

public class InventoryManagement {
    public static ArrayList<Item> getItems() {
        ArrayList<Item> items = new ArrayList<>();

        try {
            String query = "item(Id, Name, Category, Price, Stock)";
            var results = KnowledgeBase.fetchQuery(query);

            for (var result : results) {
                int id = result.get("Id").intValue();
                String name = result.get("Name").toString();
                String category = result.get("Category").toString();
                float price = result.get("Price").floatValue();
                int stock = result.get("Stock").intValue();

                Item item = new Item(id, name, category, price, stock);
                items.add(item);
            }

        } catch (KnowledgeBase.KnowledgeBaseError e){
            System.out.println(e.getMessage());
        }

        return items;
    }

    public static ArrayList<Item> getItemsByCategory(String category) {
        ArrayList<Item> items = new ArrayList<>();

        try {
            String query = String.format("item(Id, Name, %s, Price, Stock)", category);
            var results = KnowledgeBase.fetchQuery(query);

            for (var result : results) {
                int id = result.get("Id").intValue();
                String name = result.get("Name").toString();
                float price = result.get("Price").floatValue();
                int stock = result.get("Stock").intValue();

                Item item = new Item(id, name, category, price, stock);
                items.add(item);
            }

        } catch (KnowledgeBase.KnowledgeBaseError e){
            System.out.println(e.getMessage());
        }

        return items;
    }

    public static ArrayList<String> getCategories() {
        ArrayList<String> categories = new ArrayList<>();

        try {
            String query = "discount(Category, Value)";
            var results = KnowledgeBase.fetchQuery(query);

            for (var result: results) {
                String category = result.get("Catehory").toString();
                categories.add(category);
            }
        } catch (KnowledgeBase.KnowledgeBaseError e) {
            System.out.println(e.getMessage());
        }
        return categories;
    }

    public static void addCategory(String category, float discount) {
        String queryStr = String.format("assertz(discount('%s', %f))", category, discount);

        try {
            KnowledgeBase.addQuery(queryStr);
            System.out.println("Category added successfully.");
        } catch (KnowledgeBase.KnowledgeBaseError e) {
            System.out.println("Failed to add category.");
        }
    }

    public static void removeCategory(String category) {
        String queryStr = String.format("retractall(discount('%s', _))", category);

        try {
            KnowledgeBase.addQuery(queryStr);
            System.out.printf("%s removed successfully.\n", category);
        } catch (KnowledgeBase.KnowledgeBaseError e) {
            System.out.printf("Failed to remove %s\n.", category);
        }
    }

    public static void editCategory(String category, float discount) {
        removeCategory(category);
        addCategory(category, discount);
    }

    public static void addItem(Item item) {


    }

    public static void removeItem(int id) {
        String queryStr = String.format("retractall(item(%d, Name, _, _, _))", id);

        try {
            KnowledgeBase.addQuery(queryStr);
            System.out.println("Item removed successfully");
        } catch (KnowledgeBase.KnowledgeBaseError e) {
            System.out.println("Failed to remove item.");
        }
    }

    public static void editItem(Item item) {

    }
}
