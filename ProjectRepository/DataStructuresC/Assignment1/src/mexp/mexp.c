#include <stdio.h>
#include <stdlib.h>

void multiply_matrices(int **a, int **b, int **result, int k) {
    for (int i = 0; i < k; i++) {
        for (int j = 0; j < k; j++) {
            result[i][j] = 0; /*for now keep as because itll change*/
            for (int l = 0; l < k; l++) {
                result[i][j] += a[i][l] * b[l][j];
            }
        }
    }
}

int **create_imatrix(int k) {
    int **identity = malloc(k * sizeof(int *)); /**/
    for (int i = 0; i < k; i++) {
        identity[i] = malloc(k * sizeof(int));
        for (int j = 0; j < k; j++) {
            if (i == j) {
                identity[i][j] = 1;
            } else {
                identity[i][j] = 0;
            }
        }
    }
    return identity;
}

int **matrix_powern(int **matrix, int n, int k) {
    if (n == 0) { return create_imatrix(k); }
    if (n == 1) { return matrix; }
    int **result = malloc(k * sizeof(int *));
    for (int i = 0; i < k; i++) {
        result[i] = malloc(k * sizeof(int));
        for (int j = 0; j < k; j++) {
            result[i][j] = matrix[i][j];
        }
    }
    for (int i = 1; i < n; i++) {
        int **temp = malloc(k * sizeof(int *));
        for (int j = 0; j < k; j++) {
            temp[j] = malloc(k * sizeof(int));
        }
        multiply_matrices(result, matrix, temp, k);
        for (int j = 0; j < k; j++) {
            free(result[j]);
            result[j] = temp[j];
        }
        free(temp);
    }
    return result;
}

void print_final(int **matrix, int k) {
    for (int i = 0; i < k; i++) {
        for (int j = 0; j < k; j++) {
            printf("%d", matrix[i][j]);
            if (j < k - 1) {
                printf(" "); 
            }
        }
        printf("\n"); 
    }
}

int main(int argc, char *argv[]) {
	/*check arguments*/
    if (argc != 2) {
        printf("ARGUMENT ERROR");
        return EXIT_FAILURE;
    }
	/*check file*/
    FILE *file = fopen(argv[1], "r");
    if (!file) {
        perror("Error opening file");
        return EXIT_FAILURE;
    }
	/*find k and n*/
    int k, n;
    fscanf(file, "%d", &k);
    int **matrix = malloc(k * sizeof(int *));
	/*read the file*/
    for (int i = 0; i < k; i++) {
        matrix[i] = malloc(k * sizeof(int));
        for (int j = 0; j < k; j++) {
            fscanf(file, "%d", &matrix[i][j]);
        }
    }
    fscanf(file, "%d", &n);
    fclose(file);
    int **result = matrix_powern(matrix, n, k);
    print_final(result, k);

	/*let them memory free*/
    for (int i = 0; i < k; i++) {
        free(matrix[i]);
        free(result[i]);
    }
    free(matrix);
    free(result);

    return EXIT_SUCCESS;
}
