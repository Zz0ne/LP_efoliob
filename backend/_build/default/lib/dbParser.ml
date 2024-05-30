open Str
open Sys

let read_file filename =
  let lines = ref [] in
  let channel = open_in filename in
  try
    while true do
      lines := input_line channel :: !lines
    done; []
  with End_of_file ->
    close_in channel;
    List.rev !lines

let parse_discount line =
  let regexp = Str.regexp "discount(\\(.*\\), \\(.*\\))." in
  if Str.string_match regexp line 0 then
    Some (Str.matched_group 1 line, float_of_string (Str.matched_group 2 line))
  else
    None

let parse_item line =
  let regexp = Str.regexp "item(\\(.*\\), '\\(.*\\)', '\\(.*\\)', \\(.*\\), \\(.*\\))." in
  if Str.string_match regexp line 0 then
    Some (int_of_string (Str.matched_group 1 line), Str.matched_group 2 line, Str.matched_group 3 line, float_of_string (Str.matched_group 4 line), int_of_string (Str.matched_group 5 line))
  else
    None

let parse_loyalty_discount line =
  let regexp = Str.regexp "loyalty_discount(\\(.*\\), \\(.*\\))." in
  if Str.string_match regexp line 0 then
    Some (Str.matched_group 1 line, float_of_string (Str.matched_group 2 line))
  else
    None

let parse_shipping_cost line =
  let regexp = Str.regexp "shipping_cost('\\(.*\\)', \\(.*\\))." in
  if Str.string_match regexp line 0 then
    Some (Str.matched_group 1 line, float_of_string (Str.matched_group 2 line))
  else
    None

(* let () = *)
(*   let absolute_path = compute_absolute_path "/dataBase/store.pl" in *)
(*   let file_lines = read_file absolute_path in *)
(*   let discounts = List.filter_map parse_discount file_lines in *)
(*   let items = List.filter_map parse_item file_lines in *)
(*   let loyalty_discounts = List.filter_map parse_loyalty_discount file_lines in *)
(*   let shipping_costs = List.filter_map parse_shipping_cost file_lines in *)
(*   (* Print the parsed information *) *)
(*   List.iter (fun (category, discount) -> Printf.printf "Discount for category %s: %f\n" category discount) discounts; *)
(*   List.iter (fun (id, name, category, price, quantity) -> Printf.printf "KnowledgeBaseObjects.Item: %d, %s, %s, %f, %d\n" id name category price quantity) items; *)
(*   List.iter (fun (years, discount) -> Printf.printf "Loyalty discount for %s year(s): %f\n" years discount) loyalty_discounts; *)
(*   List.iter (fun (district, cost) -> Printf.printf "Shipping cost to district %s: %f\n" district cost) shipping_costs *)
