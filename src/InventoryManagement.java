import jdk.jfr.Category;
import org.jpl7.Term;

import java.util.ArrayList;
import java.util.HashMap;
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

    public static ArrayList<CategoryDiscount> getCategories() {
        ArrayList<CategoryDiscount> categories = new ArrayList<>();

        try {
            String query = "discount(Category, Discount)";
            var results = KnowledgeBase.fetchQuery(query);

            for (var result: results) {
                String category = result.get("Category").toString();
                float discount = result.get("Discount").floatValue();
                categories.add(new CategoryDiscount(category,discount));
            }
        } catch (KnowledgeBase.KnowledgeBaseError e) {
            System.out.println(e.getMessage());
        }
        return categories;
    }

    public static Map<String, Float> getCategory(String category) {
        Map<String, Float> categoryResult = new HashMap<>();
        try {
            String query = String.format("discount(%s, Value)", category);
            var resultArr = KnowledgeBase.fetchQuery(query);
            var result = resultArr.getFirst();

            float discount = result.get("Value").floatValue();
            categoryResult.put(category, discount);
        } catch (KnowledgeBase.KnowledgeBaseError e) {
            System.out.println(e.getMessage());
        }
        return categoryResult;
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

    public static void addItem(String name, String category, float price, int stock) {
        if (!categoryExists(category)) {
            System.out.println("Invalid category.");
            return;
        }

        try {
            String idQueryStr = "next_item_id(NextID)";
            var result = KnowledgeBase.fetchQuery(idQueryStr);
            int id = result.getFirst().get("NextID").intValue();

            String queryStr = String.format(
                    "assertz(item(%d, '%s', '%s', %f, %d))",
                    id, name, category, price, stock
            );

            KnowledgeBase.addQuery(queryStr);
            System.out.printf("%s added successfully.\n", name);
        } catch (KnowledgeBase.KnowledgeBaseError e) {
            System.out.printf("Failed to add %s\n.", name);
        }
    }

    public static void removeItem(int id) {
        String queryStr = String.format("retractall(item(%d, _, _, _, _))", id);

        try {
            KnowledgeBase.addQuery(queryStr);
            System.out.println("Item removed successfully");
        } catch (KnowledgeBase.KnowledgeBaseError e) {
            System.out.println("Failed to remove item.");
        }
    }

    public static void editItem(int id, String name, String category, float price, int stock) {
        if (!categoryExists(category)) {
            System.out.println("Invalid category.");
            return;
        }

        removeItem(id);
        try {
            String queryStr = String.format(
                    "assertz(item(%d, '%s', '%s', %f, %d))",
                    id, name, category, price, stock
            );

            KnowledgeBase.addQuery(queryStr);
            System.out.printf("%s added successfully.\n", name);
        } catch (KnowledgeBase.KnowledgeBaseError e) {
            System.out.printf("Failed to add %s\n.", name);
        }
    }

    private static boolean categoryExists(String category) {
       var categories = getCategories();

       for (var cat: categories) {
           if(category.equalsIgnoreCase(cat.getCategory())) {
               return true;
           }
       }
       return false;
    }
}
