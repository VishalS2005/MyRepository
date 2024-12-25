#include <stdio.h>
#include <stdlib.h>

typedef struct node {
    int data;
    struct node *left;
    struct node *right;
} Node;

Node* create_node(int data) {
    Node *new_node = (Node*)malloc(sizeof(Node));
    if (!new_node) exit(EXIT_FAILURE); // Check for malloc failure
    new_node->data = data;
    new_node->left = NULL;
    new_node->right = NULL;
    return new_node;
}

void insert(Node** root, int data) {
    if (*root == NULL) {
        *root = create_node(data);
        printf("inserted\n");
        return;
    }
    Node* prev = NULL;
    Node* ptr = *root;
    while (ptr != NULL) {
        prev = ptr;
        if (data < ptr->data) {
            ptr = ptr->left;
        } else if (data > ptr->data) {
            ptr = ptr->right;
        } else {
            printf("not inserted\n");
            return;
        }
    }

    if (data < prev->data) {
        prev->left = create_node(data);
    } else {
        prev->right = create_node(data);
    }
    printf("inserted\n");
}

void search(Node* ptr, int n) {
    if (ptr == NULL) {
        printf("absent\n");
        return;
    }
    if (ptr->data > n) {
        search(ptr->left, n);
    } else if (ptr->data == n) {
        printf("present\n");
    } else {
        search(ptr->right, n);
    }
}

void print_tree(Node* root) {
    if (root == NULL) return;
    printf("(");
    print_tree(root->left);
    printf("%d", root->data);
    print_tree(root->right);
    printf(")");
}

Node* find_max(Node* root) {
    while (root->right != NULL) { root = root->right; }
    return root;
}

Node* delete_node(Node* root, int data) {
    if (root == NULL) {
        printf("absent\n");
        return root;
    }
    if (data < root->data) {
        root->left = delete_node(root->left, data);
    } else if (data > root->data) {
        root->right = delete_node(root->right, data);
    } else {
        // Node found
        if (root->left == NULL) {
            Node* temp = root->right;
            free(root);
            printf("deleted\n");
            return temp;
        } else if (root->right == NULL) {
            Node* temp = root->left;
            free(root);
            printf("deleted\n");
            return temp;
        } else {
            Node* temp = find_max(root->left);
            root->data = temp->data;
            root->left = delete_node(root->left, temp->data);
        }
    }
    return root;
}

void free_memory(Node* root) {
    if (root == NULL) return;
    free_memory(root->left);
    free_memory(root->right);
    free(root);
}

int main() {
    Node* root = NULL;
    char instruction;

    while (scanf(" %c", &instruction) != EOF) {
        if (instruction == 'i') {
            int data;
            scanf("%d", &data);
            insert(&root, data); 
        } else if (instruction == 'd') {
            int data;
            scanf("%d", &data);
            root = delete_node(root, data); 
        } else if (instruction == 's') {
            int data;
            scanf("%d", &data);
            search(root, data); 
        } else if (instruction == 'p') {
            print_tree(root); 
            printf("\n");
        }
    }
    free_memory(root);
    return EXIT_SUCCESS;
}
