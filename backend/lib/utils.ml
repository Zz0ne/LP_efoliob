open DbParser

(* Retorna uma lista de items coms os seus detalhes a partir dos IDs. *)
let get_items_details itemIDs available_items =
    List.fold_right (fun itemId acc ->
        match List.find_opt (fun (id, _, _, _, _) -> id = itemId) available_items with
        | Some item_detail -> item_detail :: acc
        | None -> acc
    ) itemIDs []

