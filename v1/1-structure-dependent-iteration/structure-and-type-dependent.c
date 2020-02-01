#include <stdio.h>

int sum(int *array, size_t size);
double sumDouble(double *array, size_t size);

int main()
{
    int arr[] = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
    double arrD[] = {1., 2., 3., 4., 5., 6., 7., 8., 9., 10.};
    printf("%d\n", sum(arr, 10));
    printf("%f\n", sumDouble(arrD, 10));
}

int sum(int *array, size_t size)
{
    int sum = 0;
    for (int i = 0; i < size; i++)
    {
        sum += array[i];
    }

    return sum;
}

double sumDouble(double *array, size_t size)
{
    double sum = 0;
    for (int i = 0; i < size; i++)
    {
        sum += array[i];
    }

    return sum;
}