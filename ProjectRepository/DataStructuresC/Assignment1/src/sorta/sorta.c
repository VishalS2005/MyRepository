#include <stdio.h>
#include <stdlib.h>
#include <string.h>

int main(int argc, char *argv[]) {
    // Bubble sort for strings
    for (int i = 1; i < argc - 1; i++) {
        for (int j = 1; j < argc - i; j++) {  
            if (strcmp(argv[j], argv[j + 1]) > 0) {
                char *temp = argv[j];
                argv[j] = argv[j + 1];
                argv[j + 1] = temp;
            }
        }
    }
    // Print sorted arguments
    for (int i = 1; i < argc; i++) {
        printf("%s\n", argv[i]);
    }
    return EXIT_SUCCESS;
}
