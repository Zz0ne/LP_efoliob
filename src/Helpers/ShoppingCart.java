package Helpers;

import KnowledgeBaseObjects.Item;

import java.util.ArrayList;

public class ShoppingCart {
    private final ArrayList<Item> items;

    public ShoppingCart() {
        this.items = new ArrayList<>();
    }

    /**
     * Adiciona um item ao carrinho ou incrementa a sua quantidade.
     * @param item O item a ser adicionado.
     */
    public void addItem(Item item) {
        if (items.contains(item)) {
            int index = items.indexOf(item);
            Item itemToIncrement = items.get(index);
            itemToIncrement.incrementQuantity(item.getQuantity());
        } else {
            items.add(item);
        }
    }

    public ArrayList<Item> getItems() {
       return items;
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    /**
     * Retorna uma representação textual do conteúdo do carrinho de compras.
     * @return Uma string detalhando os itens e suas quantidades no carrinho.
     */
    @Override
    public String toString() {
        StringBuilder toReturn = new StringBuilder();
        for (Item item: items) {
            toReturn.append(item).append("\n");
        }
        return toReturn.toString();
    }
}