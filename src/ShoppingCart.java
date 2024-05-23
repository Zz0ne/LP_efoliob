import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class ShoppingCart {
    // Mapa que armazena itens e as suas quantidades
    private final Map<Item, Integer> items;

    public ShoppingCart() {
        this.items = new HashMap<>();
    }

    /**
     * Adiciona um item ao carrinho ou incrementa a sua quantidade.
     * @param item O item a ser adicionado.
     */
    public void addItem(Item item) {
        if (items.containsKey(item)) {
            items.put(item, items.get(item) + 1);
        } else {
            items.put(item, 1);
        }
    }

    /**
     * Retorna uma representação textual do conteúdo do carrinho de compras.
     * @return Uma string detalhando os itens e suas quantidades no carrinho.
     */
    @Override
    public String toString() {
        StringBuilder toReturn = new StringBuilder();
        for (Map.Entry<Item, Integer> entry : items.entrySet()) {
            String itemString = entry.getKey() + " -------- Quantity: " + entry.getValue() + "\n";
            toReturn.append(itemString);
        }
        return toReturn.toString();
    }

    /**
     * Retorna uma lista contendo os IDs de cada item no carrinho, considerando suas quantidades.
     * Este método existe para simplificar o envio de dados para o backend
     * @return Uma lista de inteiros representando os IDs dos itens no carrinho.
     */
    public ArrayList<Integer> getItemsID() {
        ArrayList<Integer> toReturn = new ArrayList<>();
        for (Map.Entry<Item, Integer> entry : items.entrySet()) {
            int id = entry.getKey().getId();
            int quantity = entry.getValue();
            for (int i = 0; i < quantity; i++) {
                toReturn.add(id);
            }
        }
        return toReturn;
    }
}