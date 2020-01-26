#include <stdio.h>

int sum(int *array, size_t size);
double sumDouble(double *array, size_t size);
double sumGeneric(SENSOR_METRIC_TYPES type,void *array, size_t size);

typedef enum {
    DOULBE,
    INT
} SENSOR_METRIC_TYPES;

int main()
{
    int arr[] = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
    double arrD[] = {1., 2., 3., 4., 5., 6., 7., 8., 9., 10.};
    printf("%d\n", sumGeneric(INT, arr, 10));
    printf("%d\n", sumGeneric(DOULBE, arrD, 10));
}

double sumGeneric(SENSOR_METRIC_TYPES type,void *array, size_t size){
    switch(type){
        case DOULBE:
            return sumDouble((double*) array, size);
        case INT: 
            return (double)sum((int*) array, size);
        default:
            return -1;
    }
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