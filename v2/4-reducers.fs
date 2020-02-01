
type Reducer<'Acc, 'In> = 'Acc -> 'In -> 'Acc

let mapReducer func acc input = acc @ [func input]

let filterReducer predicate acc input = if predicate input then acc @ [input] else acc

let isEven x = (x % 2) = 0
let lte a b = b <= a
let square x = x * x
let sum a b = a + b

printfn "%A" (
  [1; 2; 3; 4; 5; 6; 7; 8; 9; 10]
  |> List.fold (filterReducer isEven) []
  |> List.fold (filterReducer (lte 5)) []
  |> List.fold (mapReducer square) []
)

printfn "%d" (
  [1; 2; 3; 4; 5; 6; 7; 8; 9; 10]
  |> List.fold (filterReducer isEven) []
  |> List.fold (filterReducer (lte 5)) []
  |> List.fold (mapReducer square) []
  |> List.fold sum 0
)