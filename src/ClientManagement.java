import org.jpl7.Term;

import java.util.ArrayList;
import java.util.Map;

public class ClientManagement {
    public static ArrayList<Client> getClients() {
        ArrayList<Client> clients = new ArrayList<>();

        try {
            String query = "client(Id, Name, District, Loyalty)";
            ArrayList<Map<String, Term>> results = KnowledgeBase.fetchQuery(query);

            for (var result : results) {
                int id = result.get("Id").intValue();
                String name = result.get("Name").toString();
                String district = result.get("District").toString();
                int loyalty = result.get("Loyalty").intValue();

                // Remove os ';
                // Ex: 'Alice' -> Alice
                name = name.substring(1, name.length() - 1);
                district = district.substring(1, district.length() - 1);

                Client client = new Client(id, name, district, loyalty);
                clients.add(client);
            }
        } catch (KnowledgeBase.KnowledgeBaseError e) {
            System.out.println(e.getMessage());
        }

        return clients;
    }

    public static ArrayList<Client> getClientsByDistrict(String district) {
        ArrayList<Client> clients = new ArrayList<>();

        try {
            String query = String.format("client(Id, Name, %s, Loyalty)", district);
            ArrayList<Map<String, Term>> results = KnowledgeBase.fetchQuery(query);

        for (var result : results) {
            int id = result.get("Id").intValue();
            String name = result.get("Name").toString();
            int loyalty = result.get("Loyalty").intValue();

            // Remove os ';
            // Ex: 'Alice' -> Alice
            name = name.substring(1, name.length() - 1);

            Client client = new Client(id, name, district, loyalty);
            clients.add(client);
        }

        } catch (KnowledgeBase.KnowledgeBaseError e) {
            System.out.println(e.getMessage());
        }
        return clients;
    }

    public static ArrayList<Client> getClientByLoyaltyMin(int loyaltyMin) {
        ArrayList<Client> clients = new ArrayList<>();

        return clients;
    }

    public static void addClient(Client client) {

    }

    public static void removeClient(Client client) {

    }

    public static void editClient(Client client, Client newClient) {

    }
}
