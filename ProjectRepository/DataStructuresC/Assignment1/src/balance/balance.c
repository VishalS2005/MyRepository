#include <stdio.h>
#include <stdlib.h>
#include <string.h>

typedef struct node {
    char data;
    struct node *next;
} node_t;

typedef struct {
    node_t *head;
} stack_t;

void push(stack_t *stack, char c) {
    node_t *new_node = malloc(sizeof(node_t));
    if (!new_node) { puts("Error"); exit(EXIT_FAILURE); }
    new_node->data = c;
    new_node->next = stack->head;
    stack->head = new_node;
}

char pop(stack_t *S, char *dest) {
    if (S->head == NULL) return '\0'; // Return null char if stack is empty
    node_t *remove = S->head;
    *dest = remove->data;
    S->head = remove->next;
    free(remove);
    return *dest;
}

char peek(stack_t *S) {
    if (S->head == NULL) return '\0'; // Return null char if stack is empty
    return S->head->data;
}

void print_stack(stack_t *S) {
    node_t *current = S->head;
    while (current != NULL) {
        if (current->data == '(') {
            printf("%c", ')');
        } else if (current->data == '[') {
            printf("%c", ']');
        } else if (current->data == '{') {
            printf("%c", '}');
        }
        current = current->next;
    }
    printf("\n");
}

void free_stack(stack_t *S) {
    node_t *current = S->head;
    while (current != NULL) {
        node_t *next = current->next;
        free(current);
        current = next;
    }
    S->head = NULL;
}

int main(int argc, char *argv[]) {
    if (argc != 2) return EXIT_FAILURE;

    char *input = argv[1];
    int n = strlen(input);
    stack_t s;
    s.head = NULL;

    for (int i = 0; i < n; i++) {
        if (input[i] == '(' || input[i] == '[' || input[i] == '{') {
            push(&s, input[i]);
        } else if (input[i] == ')') {
            char temp = peek(&s);
            if (temp != '(') {
                printf("%d: %c\n", i, input[i]);
                free_stack(&s);
                return EXIT_FAILURE;
            }
            pop(&s, &temp);
        } else if (input[i] == ']') {
            char temp = peek(&s);
            if (temp != '[') {
                printf("%d: %c\n", i, input[i]);
                free_stack(&s);
                return EXIT_FAILURE;
            }
            pop(&s, &temp);
        } else if (input[i] == '}') {
            char temp = peek(&s);
            if (temp != '{') {
                printf("%d: %c\n", i, input[i]);
                free_stack(&s);
                return EXIT_FAILURE;
            }
            pop(&s, &temp);
        }
    }

    if (s.head != NULL) {
        printf("open: ");
        print_stack(&s);
        free_stack(&s);
        return EXIT_FAILURE;
    }

    free_stack(&s);
    return EXIT_SUCCESS;
}
