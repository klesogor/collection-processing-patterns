import java.util.ArrayList;

//General reducer
interface Reducer<Tacc, Tin> {
    public Tacc step(Tacc accamulator, Tin element);
}

//morphism a -> b
interface Fun<Tin, Tout> {
    public Tout apply(Tin param);
}

// Predicate(or morphhism a -> Boolean)
interface Predicate<Tin> extends Fun<Tin, Boolean> {
}

class Reducers {
    public static <Tin, Tout> Tout reduce(Reducer<Tout, Tin> reducer, Tout init, Iterable<Tin> iterable) {
        Tout acc = init;
        for (Tin elem : iterable) {
            acc = reducer.step(acc, elem);
        }

        return acc;
    }

    public static <Tin, Tout> Iterable<Tout> map(Fun<Tin, Tout> nt, Iterable<Tin> iterable) {
        return reduce(
            (list, element) -> {
                list.add(nt.apply(element));
                return list;
            }, 
            new ArrayList<Tout>(), 
            iterable
        );
    }

    public static <Tin> Iterable<Tin> filter(Predicate<Tin> predicate, Iterable<Tin> iterable){
        return reduce(
            (list, element) -> {
                if(predicate.apply(element)){
                    list.add(element);
                }
                return list;
            }, 
            new ArrayList<Tin>(), 
            iterable
        );
    }
}

class Reducable<T>{
    private Iterable<T> _iterable;
    
    public Reducable(Iterable<T> iterable) {
        _iterable = iterable; 
    }


    public static<Tin> Reducable<Tin> of(Tin... values){
        ArrayList<Tin> iterable = new ArrayList<>();
        for(Tin value : values){
            iterable.add(value);
        }

        return new Reducable<Tin>(iterable);
    }

    public <Tout>Tout reduce(Reducer<Tout, T> reducer, Tout init){ 
        return Reducers.reduce(reducer, init, _iterable);
    }

    public <Tout>Reducable<Tout>map(Fun<T, Tout> nt){
        return new Reducable<Tout>(Reducers.map(nt, _iterable));
    }

    public Reducable<T> filter(Predicate<T> predicate){
        return new Reducable<T>(Reducers.filter(predicate, _iterable));
    }

    public Iterable<T> unwrap(){
        return _iterable;
    }
}

class Driver{
    public static void main(String[] args) {
        Reducable<Integer> iterable = Reducable.of(1,2,3,4,5,6,7,8,9,10);
        Integer sumOfEvenSquares = iterable
            .filter(item -> item % 2 == 0)
            .map(item -> item * item)
            .reduce((a,b) -> a + b, 0);
        System.out.println(sumOfEvenSquares);
    }
}