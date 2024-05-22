% Item em inventário
item(1, 'Potion of Healing', 'potions', 10.0, 50).
item(2, 'Wand of Fireball', 'wands', 20.0, 30).
item(3, 'Enchanted Spellbook', 'enchanted_books', 30.0, 20).
item(4, 'Crystal of Clairvoyance', 'crystals', 15.0, 40).
item(5, 'Amulet of Protection', 'amulets', 25.0, 25).
item(6, 'Standard Wand', 'wands', 20.0, 100).
item(7, 'Love Potion', 'potions', 10.0, 50).
item(8, 'Advanced Spellbook', 'enchanted_Books', 15.0, 30).
item(9, 'Crystal of Magic Vision', 'crystals', 30.0, 20).
item(10, 'Flying Broomstick', 'accessories', 50.0, 10).
item(11, 'Enchanted Scroll', 'scrolls', 8.0, 50).
item(12, 'Fairy Dust', 'ingredients', 5.0, 100).

% Clientes
client(1, 'Alice', 'Aveiro', 3).
client(2, 'Beatriz', 'Braga', 1).
client(3, 'Carlos', 'Coimbra', 2).
client(4, 'Diogo', 'Lisboa', 4).
client(5, 'Eva', 'Porto', 1).
client(6, 'Francisca', 'Faro', 3).
client(7, 'Guilhermina', 'Viseu', 5).

% Descontos por categoria de item
discount('potions', 0.03).
discount('wands', 0.2).
discount('enchanted_books', 0.3).
discount('crystals', 0.15).
discount('amulets', 0.25).
discount('accessories', 0.0).
discount('scrolls', 0.05).
discount('ingredients', 0.25).

% Desconto de lealdade por ano
loyalty_discount(1, 0.05).
loyalty_discount(2, 0.1).
loyalty_discount(3, 0.15).
loyalty_discount(4, 0.2).
loyalty_discount(5, 0.25).
loyalty_discount(5, 0.3).

% Custos de envio por distrito
shipping_cost('Aveiro', 5.0).
shipping_cost('Lisboa', 7.0).
shipping_cost('Porto', 10.0).
shipping_cost('Braga', 2,5).
shipping_cost('Coimbra', 5.0).
shipping_cost('Faro', 15.0).
shipping_cost('Viseu', 3.0).

% Histórico de compras
history(1, '20/05/2024', 50, 5, 0, 5, 50).
history(2, '21/05/2024', 30, 3, 1, 3, 29).
history(3, '22/05/2024', 40, 4, 0, 4, 40).
history(4, '23/05/2024', 60, 6, 2.5, 6, 57.5).
history(5, '23/05/2024', 25, 2.5, 0, 2.5, 25).
history(6, '25/05/2024', 35, 3.5, 2, 3.5, 33).
history(7, '26/05/2024', 75, 7.5, 0, 7.5, 75).
history(3, '27/05/2024', 45, 4.5, 0, 4.5, 45).
history(4, '28/05/2024', 55, 5.5, 10, 5, 44.5).
history(1, '28/05/2024', 60, 6, 0, 6, 60).
