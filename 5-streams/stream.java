import java.util.ArrayList;
import java.util.Iterator;

interface Fun<A, B> {
    B apply(A arg);
}

interface Pred<A> extends Fun<A, Boolean> {
}

interface StreamPipeline<T> extends Iterator<T> {
}

interface Reducer<Tacc, Tin> {
    public Tacc step(Tacc accamulator, Tin element);
}

final class MapStream<Tsource, Tresult> implements StreamPipeline<Tresult> {
    private Fun<Tsource, Tresult> func;
    private StreamPipeline<Tsource> source;

    public MapStream(final Fun<Tsource, Tresult> func, final StreamPipeline<Tsource> source) {
        this.func = func;
        this.source = source;
    }

    @Override
    public boolean hasNext() {
        return source.hasNext();
    }

    @Override
    public Tresult next() {
        return func.apply(source.next());
    }
}

// Not idempotent. If caller doesn't follow iterator protocal will cause UB
final class FilterStream<Tsource> implements StreamPipeline<Tsource> {
    private Pred<Tsource> pred;
    private StreamPipeline<Tsource> source;
    private Tsource next;

    public FilterStream(final Pred<Tsource> pred, final StreamPipeline<Tsource> source) {
        this.pred = pred;
        this.source = source;
    }

    // according to iterator protocol will be called before next()
    @Override
    public boolean hasNext() {
        while (source.hasNext()) {
            Tsource possibleNext = source.next();
            if (pred.apply(possibleNext)) {
                next = possibleNext;
                return true;
            }
        }

        return false;
    }

    @Override
    public Tsource next() {
        return next;
    }
}

final class IteratorStream<Tsource> implements StreamPipeline<Tsource> {

    private Iterator<Tsource> iterator;

    public IteratorStream(Iterable<Tsource> iterable) {
        iterator = iterable.iterator();
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public Tsource next() {
        return iterator.next();
    }

}

class TakeStream<Tsource> implements StreamPipeline<Tsource> {
    private Long counter;
    private final StreamPipeline<Tsource> source;

    public TakeStream(final Long count, final StreamPipeline<Tsource> source) {
        this.counter = count;
        this.source = source;
    }

    @Override
    public boolean hasNext() {
        return counter > 0 && source.hasNext();
    }

    @Override
    public Tsource next() {
        counter--;
        return source.next();
    }
}

final class Stream<T> implements Iterable<T> {
    private final StreamPipeline<T> pipeline;

    public Stream(final StreamPipeline<T> pipeline) {
        this.pipeline = pipeline;
    }

    public static <T> Stream<T> of(final Iterable<T> pipeline) {
        return new Stream<>(new IteratorStream<>(pipeline));
    }

    public <Tout> Stream<Tout> map(final Fun<T, Tout> f) {
        return new Stream<>(new MapStream<T, Tout>(f, pipeline));
    }

    public Stream<T> filter(final Pred<T> pred) {
        return new Stream<>(new FilterStream<T>(pred, pipeline));
    }

    public Stream<T> take(final Long count) {
        return new Stream<>(new TakeStream<T>(count, pipeline));
    }

    public <Tresult> Tresult reduce(final Reducer<Tresult, T> reducer, final Tresult initial) {
        Tresult acc = initial;
        while (pipeline.hasNext()) {
            acc = reducer.step(acc, pipeline.next());
        }

        return acc;
    }

    public ArrayList<T> toArrayList() {
        return this.<ArrayList<T>>reduce((final ArrayList<T> acc, final T current) -> {
            acc.add(current);
            return acc;
        }, new ArrayList<T>());
    }

    @Override
    public Iterator<T> iterator() {
        return pipeline;
    }
}

class Driver {
    public static void main(final String[] args) {
        // Arrange
        final ArrayList<Integer> source = new ArrayList<>();
        source.add(1);
        source.add(2);
        source.add(3);
        source.add(4);
        source.add(5);
        source.add(6);
        source.add(7);
        source.add(8);
        source.add(9);
        source.add(10);
        // Act and Assert
        Stream
            .of(source)
            .take(9l)
            .map(x -> x * x)
            .filter(x -> x > 20)
            .reduce((a, b) -> a + b, 0);
        Stream
            .of(source)
            .take(9l)
            .map(x -> x * x)
            .filter(x -> x > 20)
            .toArrayList();
        System.out.println();
        System.out.println();
    }
}