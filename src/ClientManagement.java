import Helpers.KnowledgeBase;
import Helpers.MyScanner;
import KnowledgeBaseObjects.Client;
import org.jpl7.Term;

import java.util.ArrayList;
import java.util.Map;

public class ClientManagement {

    public static void manage(MyScanner userInput) {
        while (true) {
            System.out.println("KnowledgeBaseObjects.Client Management: ");
            System.out.println("0 - Go Back.");
            System.out.println("1 - Check clients.");
            System.out.println("2 - Check clients by district.");
            System.out.println("3 - Check client purchases.");
            System.out.println("4 - Check clients by over x loyalty years.");
            System.out.println("5 - Add client.");
            System.out.println("6 - Remove client.");
            System.out.println("7 - Edit client.");

            int choice = userInput.nextInt();

            switch (choice) {
                case 0:
                    return;
                case 1:
                    var clients = getClients();
                    for (var client : clients)
                        System.out.println(client);
                    break;
                case 2:
                    System.out.print("Enter District: ");
                    String district = userInput.nextLine();
                    var clientsByDistrict = getClientsByDistrict(district);
                    for (var client : clientsByDistrict)
                        System.out.println(client);
                    break;
                case 3:
                    System.out.print("Enter KnowledgeBaseObjects.Client ID: ");
                    int clientId = userInput.nextInt();
                    var purchases = PurchaseHistory.getByClient(clientId);
                    for (var purchase : purchases)
                        System.out.println(purchase);
                    break;
                case 4:
                    System.out.print("Enter loyalty years: ");
                    int loyaltyYears = userInput.nextInt();
                    var clientsByLoyalty = getClientOverXLoyalty(loyaltyYears);
                    for(var client: clientsByLoyalty)
                        System.out.println(client);
                    break;
                case 5:
                    System.out.print("Enter client's name: ");
                    String clientName = userInput.nextLine();
                    System.out.print("Enter district: ");
                    String districtName = userInput.nextLine();
                    System.out.print("Enter loyalty years: ");
                    int loyalty = userInput.nextInt();
                    addClient(clientName, districtName, loyalty);
                    break;
                case 6:
                    System.out.print("Enter client id: ");
                    int clientIdToRemove = userInput.nextInt();
                    removeClient(clientIdToRemove);
                    break;
                case 7:
                    System.out.print("Enter client id: ");
                    int clientIdToEdit = userInput.nextInt();
                    System.out.print("Enter new client Name: ");
                    String newClientName = userInput.nextLine();
                    System.out.print("Enter new district: ");
                    String newDistrict = userInput.nextLine();
                    System.out.print("Enter new loyalty years value: ");
                    int newLoyaltyYears = userInput.nextInt();
                    editClient(clientIdToEdit, newClientName, newDistrict, newLoyaltyYears);
                    break;
                default:
                    System.out.println("Invalid input.");
            }
            System.out.println();
        }

    }

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
            String query = String.format("client(Id, Name, '%s', Loyalty)", district);
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

    public static ArrayList<Client> getClientOverXLoyalty(int loyaltyMin) {
        ArrayList<Client> clients = new ArrayList<>();

        String query = String.format(
                "clients_over_x_loyalty_years(%d, Id, Name, District, LoyaltyYears)",
                loyaltyMin
        );
        try {
            ArrayList<Map<String, Term>> results = KnowledgeBase.fetchQuery(query);

            for (var result : results) {
                int id = result.get("Id").intValue();
                String name = result.get("Name").toString();
                String district = result.get("District").toString();
                int loyalty = result.get("LoyaltyYears").intValue();

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

    public static void addClient(String name, String district, int loyalty) {

        try {
            String idQueryStr = "next_client_id(NextID)";
            var result = KnowledgeBase.fetchQuery(idQueryStr);
            int id = result.getFirst().get("NextID").intValue();

            String queryStr = String.format(
                    "assertz(client(%d, '%s', '%s', %d))",
                    id, name, district, loyalty
            );

            KnowledgeBase.addQuery(queryStr);
            System.out.printf("%s added successfully.\n", name);
        } catch (KnowledgeBase.KnowledgeBaseError e) {
            System.out.printf("Failed to add %s\n.", name);
        }
    }

    public static void removeClient(int id) {

        String queryStr = String.format("retractall(client(%d, _, _, _))", id);

        try {
            KnowledgeBase.addQuery(queryStr);
            System.out.println("KnowledgeBaseObjects.Item removed successfully");
        } catch (KnowledgeBase.KnowledgeBaseError e) {
            System.out.println("Failed to remove item.");
        }
    }

    public static void editClient(int id, String name, String district, int loyalty) {
        removeClient(id);

        try {
            String queryStr = String.format(
                    "assertz(client(%d, '%s', '%s', %d))",
                    id, name, district, loyalty
            );

            KnowledgeBase.addQuery(queryStr);
            System.out.printf("%s edited successfully.\n", name);
        } catch (KnowledgeBase.KnowledgeBaseError e) {
            System.out.printf("Failed to edit %s\n.", name);
        }
    }
}
