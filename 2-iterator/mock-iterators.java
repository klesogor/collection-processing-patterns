import sun.reflect.generics.reflectiveObjects.NotImplementedException;

class ArrayIterator<T> implements Iterator<T>{
    private Integer index = 0;
    private T[] array;
    public ArrayIterator(T[] array){
        this.array = array;
    }

    @Override
    public boolean hasNext() {
        return this.index < this.array.length;
    }

    @Override
    public T next() {
        return this.array[index++];
    }
}

class RemoteResource{}
class RemoteProxyIterator implements Iterator<RemoteResource>{
    private Integer resourceCount;
    private Integer resourceProcessed = 0;
    public RemoteProxyIterator(String url) {
        resourceCount = getResourceCount(url);
    }
    private Integer getResourceCount(String url){
        throw new NotImplementedException("This is mock example");
    }

    private RemoteResource getResource(Integer number){
        throw new NotImplementedException("This is mock example");
    }

    @Override
    public boolean hasNext() {
        return this.resourceProcessed < this.resourceCount;
    }

    @Override
    public T next() {
        return this.getResource(resourceProcessed++);
    }
}
