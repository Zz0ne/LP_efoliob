import Helpers.*;
import org.jpl7.Term;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Map;

public class PurchaseHistory {
    public static void consult(MyScanner userInput) {
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

    public static void add(int id ,float totalPrice, float catDisc, float loyalDisc, float shipping, float finPrice) {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String formattedDate = currentDate.format(formatter);

        String queryStr = String.format(
                "assertz(history(%d, '%s', %f, %f, %f, %f, %f))",
                id, formattedDate, totalPrice, catDisc, loyalDisc, shipping, finPrice
        );

        try {
            KnowledgeBase.addQuery(queryStr);
            System.out.println("Purchase history added successfully.");
        } catch (KnowledgeBase.KnowledgeBaseError e) {
            System.out.println("Failed to add purchase history.");
        }
    }

    public static ArrayList<String> getByDate(String date) {
        ArrayList<String> purchaseHistory = new ArrayList<>();

        String queryStr = String.format(
                "history(KnowledgeBaseObjects.Client, '%s', Value, CatDisc, LoyalDisc, Shipping, FinPrice)", date
        );

        try {
            var resp = KnowledgeBase.fetchQuery(queryStr);
            for (var respObj : resp) {
                int clientId = respObj.get("KnowledgeBaseObjects.Client").intValue();
                int value = respObj.get("Value").intValue();
                int catDisc = respObj.get("CatDisc").intValue();
                int loyalDisc = respObj.get("LoyalDisc").intValue();
                int shipping = respObj.get("Shipping").intValue();
                int finPrice = respObj.get("FinPrice").intValue();

                purchaseHistory.add(String.format(
                   "%d, %s, %d, %d, %d, %d, %d", clientId, date, value, catDisc, loyalDisc, shipping, finPrice
                ));
            }
        } catch (KnowledgeBase.KnowledgeBaseError e) {
            purchaseHistory.add("No results found.");
        }

        return purchaseHistory;
    }

    public static ArrayList<String> getByClient(int clientId) {
        ArrayList<String> purchaseHistory = new ArrayList<>();

        String queryStr = String.format(
                "history(%d, Date, Value, CatDisc, LoyalDisc, Shipping, FinPrice)", clientId
        );

        try {
            var resp = KnowledgeBase.fetchQuery(queryStr);
            for (var respObj : resp) {
                int value = respObj.get("Value").intValue();
                String date = respObj.get("Date").toString();
                int catDisc = respObj.get("CatDisc").intValue();
                int loyalDisc = respObj.get("LoyalDisc").intValue();
                int shipping = respObj.get("Shipping").intValue();
                int finPrice = respObj.get("FinPrice").intValue();

                purchaseHistory.add(String.format(
                        "%d, %s, %d, %d, %d, %d, %d", clientId, date, value, catDisc, loyalDisc, shipping, finPrice
                ));
            }
        } catch (KnowledgeBase.KnowledgeBaseError e) {
            purchaseHistory.add("No results found.");
        }
        return purchaseHistory;
    }

    public static ArrayList<String> getByDistrict(String district) {
        ArrayList<String> purchaseHistory = new ArrayList<>();

        String queryStr = String.format(
                "purchase_history_by_district('%s', ClientId, Date, TotalPrice, CatDisc, LoyalDisc, Shipping, FinPrice)", district
        );

        try {
            var resp = KnowledgeBase.fetchQuery(queryStr);
            for (var respObj : resp) {
                int clientId = respObj.get("ClientId").intValue();
                String date = respObj.get("Date").toString();
                float totalPrice = respObj.get("TotalPrice").floatValue();
                float catDisc = respObj.get("CatDisc").floatValue();
                float loyalDisc = respObj.get("LoyalDisc").floatValue();
                float shipping = respObj.get("Shipping").floatValue();
                float finPrice = respObj.get("FinPrice").floatValue();

                purchaseHistory.add(String.format(
                        "%d, %s, %.2f, %.2f, %.2f, %.2f, %.2f", clientId, date, totalPrice, catDisc, loyalDisc, shipping, finPrice
                ));
            }
        } catch (KnowledgeBase.KnowledgeBaseError e) {
            purchaseHistory.add("No results found.");
        }
        return purchaseHistory;
    }

    public static String getTotalsByDistrict(String district) {
        String queryStr = String.format(
                "purchase_history_totals_by_district('%s', TotalSum, CatDiscSum, LoyalDiscSum, ShippingSum, FinPriceSum)", district
        );

        try {
            var arrResp = KnowledgeBase.fetchQuery(queryStr);
            Map<String, Term> resp = arrResp.get(0);
            float totalSum = resp.get("TotalSum").floatValue();
            float catDiscSum = resp.get("CatDiscSum").floatValue();
            float loyalDiscSum = resp.get("LoyalDiscSum").floatValue();
            float shippingSum = resp.get("ShippingSum").floatValue();
            float finPriceSum = resp.get("FinPriceSum").floatValue();

            return String.format(
                    "Total Sum: %.2f\nCategory Discount Sum: %.2f\nLoyalty Discount Sum: %.2f\nShipping Sum: %.2f\nFinal Price Sum: %.2f",
                    totalSum, catDiscSum, loyalDiscSum, shippingSum, finPriceSum
            );
        } catch (KnowledgeBase.KnowledgeBaseError e) {
            return "No results found.";
        }
    }

    public static String getTotalsByDate(String date) {
        String queryStr = String.format(
                "purchase_history_totals_by_date('%s', TotalSum, CatDiscSum, LoyalDiscSum, ShippingSum, FinPriceSum)", date
        );

        try {
            var arrResp = KnowledgeBase.fetchQuery(queryStr);
            Map<String, Term> resp = arrResp.get(0);
            float totalSum = resp.get("TotalSum").floatValue();
            float catDiscSum = resp.get("CatDiscSum").floatValue();
            float loyalDiscSum = resp.get("LoyalDiscSum").floatValue();
            float shippingSum = resp.get("ShippingSum").floatValue();
            float finPriceSum = resp.get("FinPriceSum").floatValue();

            return String.format(
                    "Total Sum: %.2f\nCategory Discount Sum: %.2f\nLoyalty Discount Sum: %.2f\nShipping Sum: %.2f\nFinal Price Sum: %.2f",
                    totalSum, catDiscSum, loyalDiscSum, shippingSum, finPriceSum
            );
        } catch (KnowledgeBase.KnowledgeBaseError e) {
            return "No results found.";
        }
    }

    public static String getMostDiscountedDistrict() {
        String district = "";

        return district;
    }
}
