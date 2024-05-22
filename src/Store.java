public class Store {
    public static void main (String[] args) {

        var kb = new KnowledgeBase();
        try {
            kb.init();
        } catch (KnowledgeBase.KnowledgeBaseError e) {
            System.out.println(e.getMessage());
        }
    }
}
