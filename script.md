## Preview slide
## Intro slide

## Data processin pipelines

Data processing is widely common in computer programming. Since first programms, programmers were challenged with mapping, filtering and reducing data. Let's take a look how data processing looked like those days and how it differs from how we process data ourdays

## Structure dependent iteration

Data structures exists for a long time, each of them have their own internal structure. For example, array is simply pointer to some memory address where data of certain type is stored. Linked list consists of nodes, each of them have value and pointer to next node. Binary tree is same as linked list, but have two children instead of one. Each of this structures have own iteration mechanism, let's have a look at examples with array.

## Type dependency

It's also worth mentioning that in C example we create distinct functions for each type. In java example we insted create generic function that works for any type that conforms with the *Number* interface.

## Iterator pattern

Iterator is common mechanism to encapsulate the logic required to get next element. The simplest example of iterator should have thhe following methods:
 - next(): T
 - hasNext(): boolean
This two methods allow to iterate over some entity with hidden mechanics of getting next element. This can be API call, tree enumaration, etc.

Let's take a look at tree iterator implementation