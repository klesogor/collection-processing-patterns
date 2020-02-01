import java.util.ArrayList;

interface Reducer<Tacc, Tin> {
    public Tacc step(Tacc accamulator, Tin element);
}

interface Transducer<Tacc, Tin, Tout> {
    public Reducer<Tacc, Tin> wrap(Reducer<Tacc, Tout> next);
}

interface Func<Tin, Tout> {
    public Tout func(Tin param);
}


interface Predicate<Tin> extends Func<Tin, Boolean> {
}

class Transducers {
    static <Tacc, Tin, Tout> Transducer<Tacc, Tin, Tout> map(Func<Tin, Tout> func) {
        return new MappingTransducer<Tacc, Tin, Tout>(func);
    }

    static <Tacc, Tin> Transducer<Tacc, Tin, Tin> filter(Predicate<Tin> func) {
        return new FilteringTransducer<Tacc, Tin>(func);
    }

    // Identity
    static <Tacc, Tin> Reducer<Tacc, Tin> reduce(Reducer<Tacc, Tin> reducer) {
        return reducer;
    }

    static <Tin> Reducer<ArrayList<Tin>, Tin> toArrayList() {
        return (ArrayList<Tin> accamulator, Tin element) -> {
            accamulator.add(element);
            return accamulator;
        };
    }

    static <A, B, R> Reducer<R, A> compose(Transducer<R, A, B> a, Reducer<R, B> b) {
        return a.wrap(b);
    }

    static <A, B, C, R> Reducer<R, A> compose(Transducer<R, A, B> a, Transducer<R, B, C> b, Reducer<R, C> c) {
        return a.wrap(b.wrap(c));
    }

    static <A, B, C, D, R> Reducer<R, A> compose(Transducer<R, A, B> a, Transducer<R, B, C> b, Transducer<R, C, D> c,
            Reducer<R, D> d) {
        return a.wrap(b.wrap(c.wrap(d)));
    }

    static <Tin, Tout> Tout runReducers(Reducer<Tout, Tin> reducer, Tout init, Iterable<Tin> source) {
        Tout acc = init;
        for (Tin element : source) {
            acc = reducer.step(acc, element);
        }

        return acc;
    }
}

final class MappingTransducer<Tacc, Tin, Tout> implements Transducer<Tacc, Tin, Tout> {
    private Func<Tin, Tout> func;

    public MappingTransducer(Func<Tin, Tout> func) {
        this.func = func;
    }

    @Override
    public Reducer<Tacc, Tin> wrap(Reducer<Tacc, Tout> next) {
        return (Tacc accamulator, Tin input) -> next.step(accamulator, func.func(input));
    }
}

final class FilteringTransducer<Tacc, Tin> implements Transducer<Tacc, Tin, Tin> {
    private Predicate<Tin> func;

    public FilteringTransducer(Predicate<Tin> func) {
        this.func = func;
    }

    @Override
    public Reducer<Tacc, Tin> wrap(Reducer<Tacc, Tin> next) {
        return (Tacc accamulator, Tin input) -> {
            if (func.func(input))
                return next.step(accamulator, input);
            return accamulator;
        };
    }
}

class Driver {
    public static void main(String[] args) {
        // Arrange
        ArrayList<Integer> source = new ArrayList<>();
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

        // Act
        Reducer<ArrayList<Integer>, Integer> reducer = Transducers.compose(
                Transducers.<ArrayList<Integer>, Integer, Integer>map(x -> x + 1), 
                Transducers.filter(x -> x % 2 == 0),
                Transducers.map(x -> x * x), 
                Transducers.<Integer>toArrayList()
        );

        System.out.println(Transducers.runReducers(reducer, new ArrayList<>(), source));
    }
}