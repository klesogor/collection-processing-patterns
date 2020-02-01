package v2;

import java.util.Iterator;

class Array<T> implements Iterable<T> {
    private T[] _source;

    Array(T[] params) {
        _source = params;
    }

    @Override
    public Iterator<T> iterator() {
        return new ArrayIterator<>(_source);
    }
}

class ArrayIterator<T> implements Iterator<T>{
    private T[] _source;
    private int index;
    ArrayIterator(T[] source) {
        _source = source;
        index = 0;
    }

    @Override
    public boolean hasNext() {
        return index < _source.length;
    }

    @Override
    public T next() {
        return _source[index++];
    }
}

class Main{
    public static void main(String[] args) {
        Array<Integer> myArray = new Array<Integer>(new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10});
        
        Integer sumIterator = 0;
        Iterator<Integer> iterator = myArray.iterator();
        while(iterator.hasNext()){
            sumIterator+= iterator.next();
        }
        System.out.println(sumIterator);

        Integer sum = 0;
        for(Integer val : myArray){
            sum += val;
        }
        System.out.println(sum);
    }
}