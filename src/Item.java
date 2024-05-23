public class Item {
    private final int id;
    private final String name;
    private final String category;
    private final float price;
    private int stock;

    /**
     * Construtor para criar um novo item.
     * @param id Id único do item.
     * @param name Nome do item.
     * @param category Categoria à qual o item pertence.
     * @param price Preço do item.
     * @param stock Stock disponível
     */
    public Item(int id, String name, String category, float price, int stock) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.stock = stock;
    }

    /**
     * Obtém o id do iten.
     * @return O id do iten.
     */
    public int getId() {
        return id;
    }

    public void decrementStock(int value) throws Exception {
        if (this.stock - value < 0) {
            throw new Exception("Not enough stock!");
        }

        this.stock -= value;
    }

    /**
     * Retorna a representação em string do item, formatada para exibição.
     * @return Uma string formatada contendo id, nome, categoria e preço do item.
     */
    @Override
    public String toString() {
        return String.format("%-3d %-25s %-17s %.2f€", id, name, category, price);    }


    // Os métodos abaixo foram criados para esta classe ser usada num hashmap na classe ShoppingCart

    /**
     * Verifica a igualdade entre este item e outro objeto.
     * @param o O objeto com o qual comparar.
     * @return true se o objeto for um Item com o mesmo id, caso contrário false.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Item item = (Item) o;
        return id == item.id;
    }

    /**
     * Gera um valor de hash baseado no id do item.
     * @return Um valor de hash para o item.
     */
    @Override
    public int hashCode() {
        return id;
    }
}