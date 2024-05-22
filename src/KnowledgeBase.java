import org.jpl7.*;

public class KnowledgeBase {

    public void init() throws KnowledgeBaseError {
        Query consultQuery = new Query("consult('src/dataBase/store.pl')");
        if( consultQuery.hasSolution()) {
            System.out.println("Knowledge base initialized.");
        } else {
            throw new KnowledgeBaseError("Failed to initialize Knowledge base!");
        }
    }

    public static class KnowledgeBaseError extends Exception {
        public KnowledgeBaseError(String message) {
            super(message);
        }
    }
}
