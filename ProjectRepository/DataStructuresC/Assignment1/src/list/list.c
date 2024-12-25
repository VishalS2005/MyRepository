#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdbool.h>

typedef struct node{
	int data;
	struct node *next;
} node_t;

void insert(node_t** head, int data, int* length) {
	node_t *new_node = (node_t*)malloc(sizeof(node_t));
	if (!new_node) return;
	new_node->data = data;
	new_node->next = NULL;

	if (*head == NULL || (*head)->data >= data) {
		if (*head != NULL && (*head)->data == data) {
			free(new_node);
			return;
		}
		new_node->next = *head;
		*head = new_node;
		(*length)++;
		return;
	}

	node_t* ptr = *head;
	while (ptr->next != NULL && ptr->next->data < data) {
		ptr = ptr->next;
	}

	if (ptr->next != NULL && ptr->next->data == data) {
		free(new_node);
		return;
	}

	new_node->next = ptr->next;
	ptr->next = new_node;
	(*length)++;
}

bool delete(node_t** head, int data, int* length) {
	if (*head == NULL) return false;
	node_t* ptr = *head;
	node_t* prev = NULL;

	if (ptr->data == data) {
		*head = ptr->next;
		free(ptr);
		(*length)--;
		return true;
	}

	while (ptr != NULL && ptr->data != data) {
		prev = ptr;
		ptr = ptr->next;
	}

	if (ptr == NULL) return false;

	prev->next = ptr->next;
	free(ptr);
	(*length)--;
	return true;
}

void print_list(node_t* head, int len) {
	if (head == NULL) {
		printf("0 :\n"); 
		return;
	}
	node_t* ptr = head; 
	printf("%d :", len);
	while (ptr != NULL) {
		printf(" %d", ptr->data); 
		ptr = ptr->next;
	}
	printf("\n"); 
}

int main() {
	node_t* head = NULL;
	char instruction;
	int value = 0; 
	int length = 0; 

	while (scanf("%c %d", &instruction, &value) != EOF) { 
		if (instruction == 'i') {
			insert(&head, value, &length);
			print_list(head, length);
		} else if (instruction == 'd') {
			delete(&head, value, &length);
			print_list(head, length);
		}
	}

	node_t* current = head;
	while (current != NULL) {
		node_t* next = current->next;
		free(current);
		current = next;
	}
	return EXIT_SUCCESS;
}