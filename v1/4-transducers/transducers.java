import java.util.ArrayList;

interface Reducer<Tacc, Tin> {
    public Tacc step(Tacc accamulator, Tin element);
}

interface Transducer<Tacc, Tin> extends Reducer<Tacc, Tin> {
    public Tacc init();
}

// Natural transformation(or morphism a -> b)
interface NT<Tin, Tout> {
    public Tout func(Tin param);
}

// Predicate(or morphhism a -> Boolean)
interface Predicate<Tin> extends NT<Tin, Boolean> {
}

final class TransducingDSL<R, Tin> {
    private Transducer<R, Tin> transducer;

    private TransducingDSL(Transducer<R, Tin> transducer) {
        this.transducer = transducer;
    }

    public <T> TransducingDSL<R, T> map(NT<T, Tin> nt){
        Transducer<R, T> map = new Map<T, Tin, R>(nt, transducer);
        return new TransducingDSL<R, T>(map);
    }

    public TransducingDSL<R, Tin> filter(Predicate<Tin> predicate){
        Transducer<R, Tin> filter = new Filter<Tin, R>(predicate, transducer);
        return new TransducingDSL<R, Tin>(filter);
    }

    public R run(Iterable<Tin> iterable){
        R accamulator = transducer.init();
        for(Tin element : iterable){
            accamulator = transducer.step(accamulator, element);
        }

        return accamulator;
    }

    public static <R, Tin> TransducingDSL<R, Tin> asFold(Transducer<R, Tin> transducer) {
        return new TransducingDSL<R, Tin>(transducer);
    }

    public static <R, Tin> TransducingDSL<R, Tin> asFold(Reducer<R, Tin> reducer, R init) {
        return new TransducingDSL<R, Tin>(new Reduce<R, Tin>(reducer, init));
    }

    public static <Tin> TransducingDSL<ArrayList<Tin>, Tin> asArrayList(){
        Transducer<ArrayList<Tin>, Tin> concat = new Reduce<ArrayList<Tin>, Tin>(
            (acc, element) -> {
                acc.add(element);
                return acc;
            },
            new ArrayList<>()
        );
        return new TransducingDSL<ArrayList<Tin>, Tin>(concat);
    }
}

final class Map<Tin, Tout, R> implements Transducer<R, Tin> {
    private NT<Tin, Tout> nt;
    private Transducer<R, Tout> next;

    public Map(NT<Tin, Tout> nt, Transducer<R, Tout> next) {
        this.nt = nt;
        this.next = next;
    }

    @Override
    public R step(R accamulator, Tin element) {
        return next.step(accamulator, nt.func(element));
    }

    @Override
    public R init() {
        return next.init();
    }
}

final class Filter<Tin, R> implements Transducer<R, Tin> {
    private Predicate<Tin> predicate;
    private Transducer<R, Tin> next;

    public Filter(Predicate<Tin> predicate, Transducer<R, Tin> next) {
        this.predicate = predicate;
        this.next = next;
    }

    @Override
    public R step(R accamulator, Tin element) {
        if (predicate.func(element)) {
            return next.step(accamulator, element);
        }

        return accamulator;
    }

    @Override
    public R init() {
        return next.init();
    }
}

final class Reduce<R, Tin> implements Transducer<R, Tin> {
    private Reducer<R, Tin> reducer;
    private R initVal;

    public Reduce(Reducer<R, Tin> reducer, R init) {
        this.reducer = reducer;
        this.initVal = init;
    }

    @Override
    public R step(R accamulator, Tin element) {
        return reducer.step(accamulator, element);
    }

    @Override
    public R init() {
        return initVal;
    }
}

class Driver{
    public static void main(String[] args) {
        Iterable<Integer> set = sampleSet();
        System.out.println(
            TransducingDSL.
                <Integer>asArrayList()
                .<Integer>map(x -> x * x)
                .filter(x -> x % 2 == 0)
                .<Integer>map(x -> x + 1)
                .run(set)
        );
        System.out.println(
            TransducingDSL.
                asFold((Integer a,Integer b) -> a + b, 0)
                .<Integer>map(x -> x * x)
                .filter(x -> x % 2 == 0)
                .<Integer>map(x -> x + 1)
                .run(set)
        );
    }

    public static Iterable<Integer> sampleSet(){
        ArrayList<Integer> res = new ArrayList<>();
        res.add(1);
        res.add(2);
        res.add(3);
        res.add(4);
        res.add(5);
        res.add(6);
        res.add(7);
        res.add(8);
        res.add(9);
        res.add(10);

        return res;
    }
}