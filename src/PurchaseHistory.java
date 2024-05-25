import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class PurchaseHistory {
    public static void add(int id ,float totalPrice, float catDisc, float loyalDisc, float shipping, float finPrice) {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String formattedDate = currentDate.format(formatter);

        String prologQuery = String.format(
                "assertz(history(%d, '%s', %f, %f, %f, %f, %f))",
                id, formattedDate, totalPrice, catDisc, loyalDisc, shipping, finPrice
        );

        try {
            KnowledgeBase.addQuery(prologQuery);
            System.out.println("Purchase history added successfully.");
        } catch (KnowledgeBase.KnowledgeBaseError e) {
            System.out.println("Failed to add purchase history.");
        }
    }
}
