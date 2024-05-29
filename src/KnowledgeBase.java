import org.jpl7.*;

import java.util.ArrayList;
import java.util.Map;

public class KnowledgeBase {

    public static void init() throws KnowledgeBaseError {
        Query consultQuery = new Query("consult('backend/dataBase/store.pl')");
        if (consultQuery.hasSolution()) {
            System.out.println("Knowledge base initialized.");
        } else {
            throw new KnowledgeBaseError("Failed to initialize Knowledge base!");
        }
    }

    public static ArrayList<Map<String, Term>> fetchQuery(String queryStr) throws KnowledgeBaseError {
        Query query = new Query(queryStr);

        ArrayList<Map<String, Term>> results = new ArrayList<>();

        if (!query.hasSolution()) {
            throw new KnowledgeBaseError("Failed to query knowledge base!");
        }

        while (query.hasMoreSolutions()) {
            results.add(query.nextSolution());
        }

        return results;
    }

    public static void addQuery(String queryStr) throws KnowledgeBaseError {
        Query query = new Query(queryStr);

        if (!query.hasSolution()) {
            throw new KnowledgeBaseError("Failed to query knowledge base!");
        }
    }

    public static void updateStock(Item item) {
        String prologQuery = String.format(
                "update_stock(%d, %d)",
                item.getId(), item.getQuantity());

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
