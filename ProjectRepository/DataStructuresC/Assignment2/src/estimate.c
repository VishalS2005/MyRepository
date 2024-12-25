#include <stdio.h>
#include <stdlib.h>



double** multiply(double **a, double **b, int rowa, int cola, int rowb, int colb, double **result) {
    if (cola != rowb) {
        printf("ERROR IN MULTIPLY");
        return NULL;
    }
    for (int i = 0; i < rowa; i++) {
        for (int j = 0; j < colb; j++) {
            result[i][j] = 0;  // Set initial value to 0
        }
    }

    for (int i = 0; i < rowa; i++) { /*cola?*/
        for (int j = 0; j < colb; j++) {  /*rowb?*/
            for (int k = 0; k < cola; k++) {
                result[i][j] += a[i][k] * b[k][j];
            }   /*multiply the ith row from a with the jth column from b*/
        }
    }
    return result;
}



double** transpose(double **a, int col, int row, double **transpose) {
    for (int i = 0; i < row; i ++) {
        for (int j = 0; j < col; j++) {
            transpose[j][i] = a[i][j];
        }
    }
    return transpose;
}



double **create_imatrix(int k) {
    double **identity = malloc(k * sizeof(double *)); /*epic stuff going on over here with dynamic memory allocation of rows*/
    for (int i = 0; i < k; i++) {
        identity[i] = malloc(k * sizeof(double)); /*creates for each column*/
        for (int j = 0; j < k; j++) {
            identity[i][j] = (i==j);
        }
    }
    return identity;
}



double **invert(double **matrix, int n, double **identity) {
    if (!matrix) {
        printf("ERROR IN INVERT");
        return NULL;
    }
    for (int p = 0; p < n; p++) { /*this loop goes through each row*/
        double f = matrix[p][p];
        if (!matrix[p]) {
            printf("ERROR IN INVERT");
            return NULL;
        }
        for (int k = 0; k < n; k++) { /*loops through each column*/
            matrix[p][k] = (matrix[p][k])/f; /*divide the entire row by a constant*/
            identity[p][k] = (identity[p][k])/f;
        }

        for(int i = p + 1; i < n; i++) { 
            f = matrix[i][p];
            for (int l = 0; l < n; l++) { /*loops through each column*/
                matrix[i][l] -= (matrix[p][l])*f; /*divide the entire row i by the product of the corresponding value in "matrix" and f*/
                identity[i][l] -= (identity[p][l])*f;
            }
        }
    }
    /*upper triangular matrix solved*/
    for (int p = n - 1; p >= 0; p--) {
        for (int i = p - 1; i >= 0; i--) {
            double f = matrix[i][p];
            for (int l = 0; l < n; l++) { /*loops through each column*/
                matrix[i][l] -= (matrix[p][l])*f; /*divide the entire row i by the product of the corresponding value in "matrix" and f*/
                identity[i][l] -= (identity[p][l])*f;
            }
        }
    }
    return identity;
}



void print_matrix(double **matrix, int row, int col) {
    for (int i = 0; i < row; i++) {
        for (int j = 0; j < col; j++) {
            printf("%.0f\n", matrix[i][j]);
            if (j < col - 1) {
                printf(" "); 
            }
        }
    }
}



double **create_matrix(int row, int col) {
    double **matrix = malloc(row * sizeof(double *));
    if (!matrix) {
        fprintf(stderr, "ERROR: Memory allocation failed for rows\n");
        return NULL;
    }

    for (int i = 0; i < row; i++) {
        matrix[i] = malloc(col * sizeof(double));
        if (!matrix[i]) {
            fprintf(stderr, "ERROR: Memory allocation failed for row %d\n", i);
            for (int j = 0; j < i; j++) {
                free(matrix[j]);
            }
            free(matrix);
            return NULL;
        }
        for (int j = 0; j < col; j++) {
            matrix[i][j] = 0.0;
        }
    }
    return matrix;
}





int main(int argc, char *argv[]) {
	/*check arguments*/
    if (argc != 3) {
        printf("ARGUMENT ERROR");
        return EXIT_FAILURE;
    }
	/*check file*/
    FILE *train = fopen(argv[1], "r"); /*opens the training file and double checks*/
    if (!train) {
        perror("Error opening file");
        return EXIT_FAILURE;
    }
	/*find k and n*/
    /*X column is index (-1 through n-2) first column in X is 1
    y column is n-1*/
    int k;
    int n; /*k is the number of attributes X rows is n columns is k+1 */
    char s[6];
    fscanf(train, "%5s", s);
    fscanf(train, "%d", &k);
    fscanf(train, "%d", &n);
    double **X = malloc(n * sizeof(double *)); /*initialized rows for X (column 0)*/
    double **Y = malloc(n * sizeof(double *)); /*initialized rows for Y (column 0)*/
    if (!X || !Y) {
        fclose(train);
        return EXIT_FAILURE;
    }

    /*example of reading a matrix value : fscanf(train, "%lf", &matrix[i][j])*/

	/*read the file*/
    for (int i = 0; i < n; i++) { /*traverse each row*/
        X[i] = malloc((k+1) * sizeof(double)); /*creates k+1 columns*/
        X[i][0] = 1;
        
        Y[i] = malloc(sizeof(double));         /*creates 1 column for Y because Y is still a 2D Matrix*/
        for (int j = 1; j <= k; j++) { /*traverse each column if we imagine the matrix to be 2 more columns than usual*/
            fscanf(train, "%lf", &X[i][j]); 
        }
        fscanf(train, "%lf", &Y[i][0]);
    }
    fclose(train);

    /*
    Now we calculate the weights using what is given for X and Y
    X 
        rows is n 
        columns is k+1
    Y
        rows is n
        columns is 1
    Xt
        rows is k+1
        columns is n
    calc1
        rows is n
        columns is n
    calc2
        rows is k+1
        columns 1
    W
        rows is k+1
        columns is 1

    W = (XtX)-1(XtY)
    */
    
   
    double **Xt = transpose(X, k+1, n, create_matrix(k+1, n));
    /*result is a k+1 by n matrix*/

    double **calc1 = multiply(Xt , X, k+1, n, n, k+1, create_matrix(k+1, k+1));
    /*result is a k+1 by k+1 square matrix*/
    
    double **calc1inv = invert(calc1, k+1, create_imatrix(k+1));
    /*result is a k+1 by k+1 square matrix*/

    double **calc2 = multiply(calc1inv, Xt, k+1, k+1, k+1, n, create_matrix(k+1, n)); 
    /*result is a k+1 by n matrix */

    double **W = multiply(calc2, Y, k+1, n, n, 1, create_matrix(k+1, 1));
    /*result is a k+1 by 1 matrix*/

    /*open data file*/
    FILE *data = fopen(argv[2], "r"); /*opens the training file and double checks */
    if (!data) {
        perror("Error opening file");
        return EXIT_FAILURE;
    }
    
    char c[6];
    fscanf(data, "%5c", c);
    int kdata;
    fscanf(data, "%d", &kdata);
    int m;
    fscanf(data, "%d", &m);
    /*
    Xdata
        rows is m
        columns is kdata
    */

    double **Xdata = malloc(m * sizeof(double *));
    for (int i = 0; i < m; i++) {
        Xdata[i] = malloc((kdata+1) * sizeof(double));
        Xdata[i][0] = 1;
        for (int j = 1; j <= kdata; j++) {
            fscanf(data, "%lf", &Xdata[i][j]);
        }
    }

    double **Yfinal = multiply(Xdata, W, m, kdata+1, k+1, 1, create_matrix(m, 1));

    print_matrix(Yfinal, m, 1);
    fclose(data);
    /*let the memory freeeee!!!*/
    for (int i = 0; i < n; i++) {
        free(X[i]);
        free(Y[i]);
    }
    for (int i = 0; i < k+1; i++) {
        free(W[i]);
        free(calc1[i]);
        free(calc1inv[i]);
        free(Xt[i]);
        free(calc2[i]);
    }
    for (int i = 0; i < m; i++) {
        free(Xdata[i]);
        free(Yfinal[i]);
    }
    free(X);
    free(Y);
    free(W);
    free(calc1);
    free(calc1inv);
    free(Xt);
    free(calc2);
    free(Xdata);
    free(Yfinal);
    /*Huzzah!!!*/
    return EXIT_SUCCESS;
}
