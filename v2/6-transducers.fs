open System.Collections.Generic

exception ProtocolError of string

type TransducerProtocolInput<'Acc, 'In> = 
  | Init
  | Process of acc: 'Acc * value: 'In
  | Finalize of acc: 'Acc
 
type TransducerProtocolResult<'Acc> =
  | Terminate of acc: 'Acc
  | Continue of acc: 'Acc

type Reducer<'Acc, 'In> = TransducerProtocolInput<'Acc, 'In> -> TransducerProtocolResult<'Acc>

type Trasnducer<'Acc, 'In, 'Out> = Reducer<'Acc, 'Out> -> Reducer<'Acc, 'In>

let map (func: 'In -> 'Out) (next: Reducer<'Acc, 'Out>) (transducable: TransducerProtocolInput<'Acc, 'In>) =
  match transducable with
  | Init -> next(Init)
  | Process(acc, value) -> next (Process(acc, func value))
  | Finalize(acc) -> next (Finalize(acc))

let filter (pred: 'In -> bool) (next: Reducer<'Acc, 'In>) (transducable: TransducerProtocolInput<'Acc, 'In>) =
  match transducable with
  | Init -> next(Init)
  | Process(acc, value) when pred value  -> next transducable
  | Process(acc, _) -> Continue(acc)
  | Finalize(acc) -> next (Finalize(acc))

let reduce (func: 'Acc -> 'In -> 'Acc) (init: 'Acc) (transducable: TransducerProtocolInput<'Acc, 'In>) =
  match transducable with
  | Init -> Continue(init)
  | Process(acc, value) -> Continue(func acc value)
  | Finalize(acc) -> Terminate(acc)

let take limit (next: Reducer<'Acc, 'In>) = 
    let counter = ref 0
    fun (transducable: TransducerProtocolInput<'Acc, 'In>) ->
        match transducable with
        | Init -> 
            counter := 0
            next(Init)
        | Process(acc, value) when !counter < limit -> 
            counter := !counter + 1
            next (Process(acc, value))
        | Process(acc, _) -> Terminate(acc)
        | Finalize(acc) -> next (Finalize(acc))

let transduce (reducer: Reducer<'Acc, 'In>) (seq: IEnumerable<'In>) = 
    let acc = reducer Init
    let enumerator = seq.GetEnumerator()
    let rec recurInner (enumerator: IEnumerator<'In>) acc =
        match acc with
        | Terminate(acc) -> 
          let wrapedRes = reducer(Finalize(acc))
          match wrapedRes with
          | Terminate(res) -> res
          | Continue(res) -> res
        | Continue(acc) ->
            if enumerator.MoveNext() then
                recurInner enumerator (reducer(Process(acc, enumerator.Current)))
            else
                match reducer(Finalize acc) with
                | Terminate(res) -> res
                | Continue(res) -> res
    recurInner enumerator acc

let asList = reduce (fun (acc: 'a list) (input: 'a) -> acc @ [input]) ([])
let asSum = reduce (fun a b -> a + b) 0

let isEven x = (x % 2) = 0
let lte a b = b <= a
let square x = x * x
let sum a b = a + b
let SquaredEvensLTE8 = (filter (lte 8) << filter isEven << map square) asList
let SquaredEvensLTE8Sum = (filter (lte 8) << filter isEven << map square) asSum

printfn "List: %A" (transduce (SquaredEvensLTE8) (seq {1 .. 10}))
printfn "Sum %d"(transduce (SquaredEvensLTE8Sum) (seq {1 .. 10}))
