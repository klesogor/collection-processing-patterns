

let rec sum list =
   match list with
   | head :: tail -> head + sum tail
   | [] -> 0

let rec sumTCO list acc = 
  match list with 
  | head :: tail -> sumTCO tail acc + head
  | [] -> acc

let list = [1; 2; 3; 4; 5; 6; 7; 8; 9; 10]

printfn "Sum: %d:\n %A" (sum list) list
printfn "SumTCO: %d:\n %A" (sumTCO list 0) list