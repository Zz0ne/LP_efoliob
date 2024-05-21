import org.jpl7.*;

public class KnowledgeBase {
    private boolean loadNewData() {

       return true;
    }

    public void init() {
        Query consultQuery = new Query("consult('src/dataBase/store.pl')");
        if( consultQuery.hasSolution()) {
            System.out.println("Knowledge base initialized.");
        } else {
            System.out.println("Failed to initialize Knowledge base!");
            // TODO: Trow exeption
        }

        System.out.print("Loading new Data...");
        if (loadNewData()) {
            System.out.println("Done.");
        }else {
            System.out.println("Failed.");
            // TODO: Trow exeption
        }
    }
}
