
class StructureDependent {
    public static void main(String[] args) {
        System.out.println(SensorDataProcessor.sum(new Integer[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 }));
        System.out.println(SensorDataProcessor.sum(new Double[] { 1., 2., 3., 4., 5., 6., 7., 8., 9., 10. }));
    }
}

static class SensorDataProcessor{
     public static <T extends Number> Double sum(T[] array) {
        Double sum = 0d;
        for (int i = 0; i < array.length; i++) {
            sum += array[i].doubleValue();
        }

        return sum;
    }
}