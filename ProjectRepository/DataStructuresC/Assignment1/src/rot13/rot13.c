#include <stdio.h>
#include <stdlib.h>
#include <string.h>


int main(int argc, char *argv[]) {
	if (argc != 2) return 1;
	char *inputString = argv[1];
	for (int i = 0; i < 100; i++) {
		if (inputString[i] >= 'a' && inputString[i] <= 'z') {
			inputString[i] = (inputString[i] - 'a' + 13) % 26 + 'a';	
		} else if (inputString[i] >= 'A' && inputString[i] <= 'Z') {
			inputString[i] = (inputString[i] - 'A' + 13) % 26 + 'A';
		}
	}
	printf("%s\n", inputString);
	return EXIT_SUCCESS;
}