import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class PurchaseHistory {
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
                "history(Client, '%s', Value, CatDisc, LoyalDisc, Shipping, FinPrice)", date
        );

        try {
            var resp = KnowledgeBase.fetchQuery(queryStr);
            for (var respObj : resp) {
                int clientId = respObj.get("Client").intValue();
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

    public static ArrayList<String> getTotalsByDistrict(String district) {
        ArrayList<String> purchaseHistory = new ArrayList<>();

        return purchaseHistory;
    }

    public static ArrayList<String> getTotalsByDate(String date) {
        ArrayList<String> purchaseHistory = new ArrayList<>();

        return purchaseHistory;
    }

    public static String getMostDiscountedDistrict() {
        String district = "";

        return district;
    }
}
