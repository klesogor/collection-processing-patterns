## Preview slide
## Intro slide

## Data processin pipelines

Data processing is widely common in computer programming. Since first programms, programmers were challenged with mapping, filtering and reducing data. Let's take a look how data processing looked like those days and how it differs from how we process data ourdays

## Structure dependent iteration

Data structures exists for a long time, each of them have their own internal structure. For example, array is simply pointer to some memory address where data of certain type is stored. Linked list consists of nodes, each of them have value and pointer to next node. Binary tree is the same as linked list, but have two children instead of one. Each of this structures have own iteration mechanism, let's have a look at examples with array.

## Type dependency

It's also worth mentioning that in C example we create distinct functions for each type. In java example we instead create generic function that works for any type that conforms with the *Number* interface.

## Iterator pattern

Iterator is common mechanism to encapsulate the logic required to get next element. The simplest example of iterator should have following methods:
 - next(): T
 - hasNext(): boolean
This two methods allow to iterate over some entity with hidden(encapsulated) mechanism of aquiring next element. This can be API call, tree enumaration, etc.

Let's take a look at tree iterator implementation;

## Patterns in iteration

Let's take a look at some examples with iterators *examples from simple to more complex*

As you can see, we can easily notice common pattern:
- *Reducing* - using accamulator and some function we change accamulator that we received on te previous step and return it;

Also, we can see some supesets of reduce:
- *Mapping* - applying natural transformation(morphism a -> b) to element and concating result to collection we can create new collection without mutating original iterable;
- *Filtering* - applyingg predicate(morphhism a -> Boolean) to element and concating original element to collection iff predicate returns true;

Let's take a look at possible reducers implementation;

## Reducer examples, code, some theoryd

## What can be better?

So we have created simple yet powerful interface for collection reducing. But it's far from perfection. Even if it's more declarative then simple use of iterator, it have a major drawback: it's *eager*. This means that we will create temporary iterable for each reducing function and we will iterate collection multiple times. However, if we use iterator, we iterate collection only once and don't use any additional memory for temporary collections. But can we keep efficency of iterator and declarativity of reducers? The answer is yes, if we will make *lazy* reducers. Transducers for rescue. 

## Building transducers step by step from interfaces to implementations, protocol overview

## Results

If you were following very closely, by now you can imagine how powerful even simple transducers are. They are little bit unusual to use(data flow builds from bottom to top and generics need some additional typing), but overall they preserve declarativity of reducers and also are as fast as simple interation over object

## Benchmarks on 1 million ints

## Production ready libs

## Debreifing and whatever