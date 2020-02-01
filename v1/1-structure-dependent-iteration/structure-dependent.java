
class StructureDependent {
    public static void main(String[] args) {
        System.out.println(sum(new Integer[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 }));
        System.out.println(sum(new Double[] { 1., 2., 3., 4., 5., 6., 7., 8., 9., 10. }));
    }

    public static <T extends Number> T sum(T[] array) {
        Double sum = 0d;
        for (int i = 0; i < array.length; i++) {
            sum += array[i].doubleValue();
        }

        return (T) sum;
    }
}