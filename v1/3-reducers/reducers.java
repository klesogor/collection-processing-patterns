import java.util.ArrayList;

//General reducer
interface Reducer<Tacc, Tin> {
    public Tacc step(Tacc accamulator, Tin element);
}

// Natural transformation(or morphism a -> b)
interface NT<Tin, Tout> {
    public Tout func(Tin param);
}

// Predicate(or morphhism a -> Boolean)
interface Predicate<Tin> extends NT<Tin, Boolean> {
}

class MagicReducers {
    public static <Tin, Tout> Tout reduce(Reducer<Tout, Tin> reducer, Tout init, Iterable<Tin> iterable) {
        Tout acc = init;
        for (Tin elem : iterable) {
            acc = reducer.step(acc, elem);
        }

        return acc;
    }

    public static <Tin, Tout> Iterable<Tout> map(NT<Tin, Tout> nt, Iterable<Tin> iterable) {
        return reduce(
            (list, element) -> {
                list.add(nt.func(element));
                return list;
            }, 
            new ArrayList<Tout>(), 
            iterable
        );
    }

    public static <Tin> Iterable<Tin> filter(Predicate<Tin> predicate, Iterable<Tin> iterable){
        return reduce(
            (list, element) -> {
                if(predicate.func(element)){
                    list.add(element);
                }
                return list;
            }, 
            new ArrayList<Tin>(), 
            iterable
        );
    }
}

class MagicIterable<T>{
    private Iterable<T> _iterable;
    
    public MagicIterable(Iterable<T> iterable) {
        _iterable = iterable; 
    }


    public static<Tin> MagicIterable<Tin> of(Tin... values){
        ArrayList<Tin> iterable = new ArrayList<>();
        for(Tin value : values){
            iterable.add(value);
        }

        return new MagicIterable<Tin>(iterable);
    }

    public <Tout>Tout reduce(Reducer<Tout, T> reducer, Tout init){ 
        return MagicReducers.reduce(reducer, init, _iterable);
    }

    public <Tout>MagicIterable<Tout>map(NT<T, Tout> nt){
        return new MagicIterable<Tout>(MagicReducers.map(nt, _iterable));
    }

    public MagicIterable<T> filter(Predicate<T> predicate){
        return new MagicIterable<T>(MagicReducers.filter(predicate, _iterable));
    }

    public Iterable<T> unwrap(){
        return _iterable;
    }
}

class Driver{
    public static void main(String[] args) {
        MagicIterable<Integer> iterable = MagicIterable.of(1,2,3,4,5,6,7,8,9,10);
        Integer sumOfEvenSquares = iterable
            .filter(item -> item % 2 == 0)
            .map(item -> item * item)
            .reduce((a,b) -> a + b, 0);
        System.out.println(sumOfEvenSquares);
    }
}