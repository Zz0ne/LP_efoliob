import org.jpl7.*;

public class PrologDataBase {

    public void testQuery() {
        Query query = new Query(
                "consult",
                new Term[] {new Atom("src/dataBase/store.pl")}
        );

        System.out.println("consult " + (query.hasSolution() ? "success" : "unsuccessfull"));
    }
}
