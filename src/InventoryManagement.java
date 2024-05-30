import Helpers.KnowledgeBase;
import Helpers.MyScanner;
import KnowledgeBaseObjects.CategoryDiscount;
import KnowledgeBaseObjects.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class InventoryManagement {
    public static void manage(MyScanner userInput) {

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
                    System.out.println("Items:");
                    for (var item : itemList)
                        System.out.println(item);
                    System.out.println();
                    break;
                case 2:
                    System.out.print("Insert category: ");
                    String category = userInput.nextLine();
                    var itemByCatList = InventoryManagement.getItemsByCategory(category);
                    System.out.println("Items:");
                    for (Item item : itemByCatList)
                        System.out.println(item);
                    System.out.println();
                    break;
                case 3:
                    var categoryList = InventoryManagement.getCategories();
                    System.out.println("Categories:");
                    for (var cat : categoryList)
                        System.out.println(cat);
                    System.out.println();
                    break;
                case 4:
                    System.out.print("Enter new category name: ");
                    String catName = userInput.nextLine();
                    System.out.print("Enter category discount: ");
                    float discount = userInput.nextFloat();
                    if (discount == -1) break;
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
                    if (newDiscount == -1) break;
                    InventoryManagement.editCategory(catToEdit, newDiscount);
                    break;
                case 7:
                    System.out.print("Enter item name: ");
                    String itemName = userInput.nextLine();
                    System.out.print("Enter category: ");
                    String itemCategory = userInput.nextLine();
                    System.out.print("Enter price: ");
                    float itemPrice = userInput.nextFloat();
                    if (itemPrice == -1) break;
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
                    if (newPrice== -1) break;
                    System.out.print("Enter Stock: ");
                    int newStock = userInput.nextInt();
                    InventoryManagement.editItem(idToEdit, newName, newCategory, newPrice, newStock);
                    break;
                default:
                    System.out.println("Invalid input.");
            }
            System.out.println();
        }
    }

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
        return CostAndDiscountManagement.getCategoryDiscounts();
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
            System.out.printf("%s edited successfully.\n", name);
        } catch (KnowledgeBase.KnowledgeBaseError e) {
            System.out.printf("Failed to edit %s\n.", name);
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
