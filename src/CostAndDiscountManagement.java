import Helpers.KnowledgeBase;
import Helpers.MyScanner;
import KnowledgeBaseObjects.LoyaltyDiscounts;
import KnowledgeBaseObjects.ShippingCost;
import KnowledgeBaseObjects.CategoryDiscount;

import java.util.ArrayList;

public class CostAndDiscountManagement {
    public static void manage(MyScanner userInput) {

        while (true) {
            System.out.println("Cost and Discount Management:");
            System.out.println("0 - Go back.");
            System.out.println("1 - Check shipping costs.");
            System.out.println("2 - Check category discounts.");
            System.out.println("3 - Check loyalty discounts.");
            System.out.println("4 - Add shipping.");
            System.out.println("5 - Remove shipping.");
            System.out.println("6 - Edit shipping.");
            System.out.println("7 - Add category discounts.");
            System.out.println("8 - Remove category discounts.");
            System.out.println("9 - Edit category discounts.");
            System.out.println("10 - Add loyalty discounts.");
            System.out.println("11 - Remove loyalty discounts.");
            System.out.println("12 - Modify loyalty discounts.");

            int choice = userInput.nextInt();

            switch (choice) {
                case 0:
                    return;
                case 1:
                    var shippings = getShippingCosts();
                    for (var shipping : shippings)
                        System.out.println(shipping);
                    break;
                case 2:
                    var categoryDiscounts = getCategoryDiscounts();
                    for (var category : categoryDiscounts)
                        System.out.println(category);
                    break;
                case 3:
                    var loyaltyDiscounts = getLoyaltyDiscounts();
                    for (var loyalty : loyaltyDiscounts)
                        System.out.println(loyalty);
                    break;
                case 4:
                    System.out.print("Enter district: ");
                    String district = userInput.nextLine();
                    System.out.print("Enter cost: ");
                    int cost = userInput.nextInt();
                    addShippingCost(district, cost);
                    break;
                case 5:
                    System.out.print("Enter district: ");
                    String districtToRemove = userInput.nextLine();
                    removeShippingCost(districtToRemove);
                    break;
                case 6:
                    System.out.print("Enter district: ");
                    String districtToEdit = userInput.nextLine();
                    System.out.print("Enter new cost: ");
                    int newCost = userInput.nextInt();
                    editShippingCost(districtToEdit, newCost);
                    break;
                case 7:
                    System.out.print("Enter category: ");
                    String category = userInput.nextLine();
                    System.out.print("Enter discount: ");
                    float discount = userInput.nextFloat();
                    addCategoryDiscount(category, discount);
                    break;
                case 8:
                    System.out.print("Enter category: ");
                    String categoryToRemove = userInput.nextLine();
                    removeCategoryDiscount(categoryToRemove);
                    break;
                case 9:
                    System.out.print("Enter category: ");
                    String categoryToEdit = userInput.nextLine();
                    System.out.print("Enter new discount: ");
                    float discountToEdit = userInput.nextFloat();
                    editCategoryDiscount(categoryToEdit, discountToEdit);
                    break;
                case 10:
                    System.out.print("Enter loyalty years: ");
                    int loyaltyYears = userInput.nextInt();
                    System.out.print("Enter discount: ");
                    float loyaltyDiscount = userInput.nextFloat();
                    addLoyaltyDiscount(loyaltyYears, loyaltyDiscount);
                    break;
                case 11:
                    System.out.print("Enter loyalty years: ");
                    int loyaltyYearsToRemove = userInput.nextInt();
                    removeLoyaltyDiscount(loyaltyYearsToRemove);
                    break;
                case 12:
                    System.out.print("Enter loyalty years: ");
                    int loyaltyYearsToEdit = userInput.nextInt();
                    System.out.print("Enter new discount: ");
                    float newLoyaltyDiscount = userInput.nextFloat();
                    editLoyaltyDiscount(loyaltyYearsToEdit, newLoyaltyDiscount);
                    break;
                default:
                    System.out.println("Invalid choice");
            }
            System.out.println();
        }
    }

    public static ArrayList<ShippingCost> getShippingCosts() {
       ArrayList<ShippingCost> shippingCosts = new ArrayList<>();

        try {
            String query = "shipping_cost(District, Cost)";
            var results = KnowledgeBase.fetchQuery(query);

            for (var result : results) {
                String district = result.get("District").toString();
                float cost = result.get("Cost").floatValue();
                shippingCosts.add(new ShippingCost(district, cost));
            }
        } catch (KnowledgeBase.KnowledgeBaseError e){
            System.out.println(e.getMessage());
        }
       return shippingCosts;
    }

    public static ArrayList<CategoryDiscount> getCategoryDiscounts() {
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

    public static ArrayList<LoyaltyDiscounts> getLoyaltyDiscounts() {
       ArrayList<LoyaltyDiscounts> loyaltyDiscounts = new ArrayList<>();

       try {
            String query = "loyalty_discount(Year, Discount)";
            var results = KnowledgeBase.fetchQuery(query);

            for (var result: results) {
                int year = result.get("Year").intValue();
                float discount = result.get("Discount").floatValue();
                loyaltyDiscounts.add(new LoyaltyDiscounts(year,discount));
            }
        } catch (KnowledgeBase.KnowledgeBaseError e) {
            System.out.println(e.getMessage());
        }
       return loyaltyDiscounts;
    }

    public static void addShippingCost(String district, float cost) {
        String queryStr = String.format("assertz(shipping_cost('%s', %f))", district, cost);

        try {
            KnowledgeBase.addQuery(queryStr);
            System.out.println("Shipping cost added successfully.");
        } catch (KnowledgeBase.KnowledgeBaseError e) {
            System.out.println("Failed to add shipping cost.");
        }
    }

    public static void removeShippingCost(String district) {
        String queryStr = String.format("retractall(shipping_cost('%s', _))", district);

        try {
            KnowledgeBase.addQuery(queryStr);
            System.out.printf("Shipping cost for %s removed successfully.\n", district);
        } catch (KnowledgeBase.KnowledgeBaseError e) {
            System.out.printf("Failed to remove shipping cost for %s\n.", district);
        }
    }

    public static void editShippingCost(String district, float cost) {
        removeShippingCost(district);
        addShippingCost(district, cost);
    }

    public static void addCategoryDiscount(String category, float discount) {
        InventoryManagement.addCategory(category,discount);
    }

    public static void removeCategoryDiscount(String category) {
        InventoryManagement.removeCategory(category);
    }

    public static void editCategoryDiscount(String category, float discount) {
        InventoryManagement.editCategory(category,discount);
    }

    public static void addLoyaltyDiscount(int year, float discount) {
        String queryStr = String.format("assertz(loyalty_discount(%d, %f))", year, discount);

        try {
            KnowledgeBase.addQuery(queryStr);
            System.out.println("Loyalty discount added successfully.");
        } catch (KnowledgeBase.KnowledgeBaseError e) {
            System.out.println("Failed to add loyalty discount.");
        }
    }

    public static void removeLoyaltyDiscount(int year) {
        String queryStr = String.format("retractall(loyalty_discount(%d, _))", year);

        try {
            KnowledgeBase.addQuery(queryStr);
            System.out.printf("Loyalty discount for year %d removed successfully.\n", year);
        } catch (KnowledgeBase.KnowledgeBaseError e) {
            System.out.printf("Failed to remove loyalty discount for year %d\n.", year);
        }
    }

    public static void editLoyaltyDiscount(int year, float discount) {
        removeLoyaltyDiscount(year);
        addLoyaltyDiscount(year, discount);
    }}

