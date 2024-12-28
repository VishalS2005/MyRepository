#include <stdio.h>
#include <stdlib.h>
#include <string.h>

/*
   file has 
    linked list where each line is a node and each 
    n inputs means 2^n lines
    DO NOT USE fgets()
    no white space follows last column
    columns separated by space and | between input and output vars
    temporary variables must occur exactly once as output parameter and zero or more times as input param

    INPUT 3 a b c
    OUTPUT 1 d
    AND a b x
    AND c x d
    
    0 0 0 |0
    0 0 1 |0
    0 1 0 |0
    0 1 1 |0
    1 0 0 |0
    1 0 1 |0
    1 1 0 |0
    1 1 1 |1

    Parsing notes:
    check for:
        directive name, int, input param, output param
    _ may be used as output of a gate (output is discarded)*/
    

typedef struct VarNode {
    char name[17];  
    int val;
    struct VarNode* next;
} VarNode;  

VarNode* create_node(const char* name, int val) {
    VarNode* new_node = malloc(sizeof(VarNode));
    if (new_node == NULL) {
        fprintf(stderr, "Memory allocation failed\n");
        exit(EXIT_FAILURE);  
    }
    strncpy(new_node->name, name, 16);
    new_node->name[16] = '\0';
    new_node->val = val;
    new_node->next = NULL;

    return new_node;
}

void add_node(VarNode** head, VarNode* new_node) {
    if (*head == NULL) {
        *head = new_node;
        return;
    }
    VarNode* current = *head;
    while (current->next != NULL) {
        current = current->next;
    }
    current->next = new_node;
}

void print_var_names(VarNode* head) {
    if (head == NULL) {
        printf("Error: The variable list is empty.\n");
        return;
    }

    VarNode* current = head;
    printf("Variables in the linked list:\n");
    while (current != NULL) {
        printf("%s", current->name);
        printf(" %d\n ",current->val);
        current = current->next;
    }
}

int find_node(VarNode* head, char name[]) {
    VarNode* current = head;
    while (current != NULL) {
        if (strcmp(current->name, name) == 0) {
            return current->val; 
        }
        current = current->next;
    }
    return -1;
}

void change_node(VarNode* head, char name[], int valChange) {
    VarNode* current = head;
    while (current != NULL) {
        if (strcmp(current->name, name) == 0) {
            current->val = valChange;
            return;
        }
        current = current->next;
    }
    printf("CHANGE FAILURE!!! CHANGE_NODE METHOD");
    return;
}

void free_var_list(VarNode* head) {
    VarNode* current = head;
    while (current != NULL) {
        VarNode* temp = current;
        current = current->next;
        free(temp);
    }
}


int main(int argc, char* argv[]) {



    if (argc != 2) {
        printf("ARGUMENT ERROR");
        return EXIT_FAILURE;
    }

    FILE* file = fopen(argv[1], "r");
    if (file == NULL) {
        printf("Error opening file");
        return EXIT_FAILURE;  
    }


    VarNode* var_list = NULL; /*HOLDS EVERYTHING THAT WE NEED EXTREMELY IMPORTANT*/

    char directive[17];  /*null terminator at the end*/
    int inputSize;
     

    /*read first line*/
    if (fscanf(file, " %16s %d", directive, &inputSize) != 2 || 
        strcmp(directive, "INPUT") != 0) {
        printf("missing INPUT directive\n");
        fclose(file);
        return EXIT_FAILURE;  
    }


    //this for loop will run 2^n times for each line
    //print then change
    //file pointer is now at the first input variable
    for (int i = 0; i < (1 << inputSize) + 1; i++) {


        /*line 1*/

        if (i == 0) {
            for (int j = 0; j < inputSize; j++) {
                char var_name[17];
                /*error check and add input variable*/
                if (fscanf(file, " %16s", var_name) != 1) {
                    printf("Error reading input variable\n");
                    fclose(file);
                    free_var_list(var_list);
                    return EXIT_FAILURE;  
                }
                VarNode* new_node = create_node(var_name, 0); /*add a variable that holds the bit 0*/
                add_node(&var_list, new_node);
            }

             

            /* now that every input variable read go down add output variables to the linked list*/


            /*read the output line*/
            int outputSize; 
            if (fscanf(file, " %16s %d", directive, &outputSize) != 2 || 
                strcmp(directive, "OUTPUT") != 0) {
                printf("missing OUTPUT directive in i = 0 portion\n");
                fclose(file);
                return EXIT_FAILURE;  
            }

             
            /*parse through the output variables*/
            for (int k = 0; k < outputSize; k++) {
                char var_name[17];
                /*error check and add output variable*/
                if (fscanf(file, " %16s", var_name) != 1) {
                    printf("Error reading output variable\n");
                    fclose(file);
                    free_var_list(var_list);
                    return EXIT_FAILURE;  
                }
                VarNode* new_node = create_node(var_name, 0); /*add a variable that holds a bit to be changed later on*/
                add_node(&var_list, new_node);
            }
            
        }
        
        else {

             
            /*reads the first string and the int*/
            char tempInput[17];
            int tempInputSize;
            /*read first line*/
            if (fscanf(file, " %16s %d", tempInput, &tempInputSize) != 2 || 
                strcmp(tempInput, "INPUT") != 0) {
                printf("missing INPUT directive in else portion\n");
                fclose(file);
                return EXIT_FAILURE;  
            }

            for (int j = inputSize - 1; j >= 0; j--) {
                /*looking through the file to find the input name*/
                char inputName[17];
                fscanf(file, " %16s", inputName);
                /*use the method to find the node you are looking for and print its value*/

                 
                int printBit = find_node(var_list, inputName);
                 

                /*LINE TO PRINT INPUT IS BELOW*/



                printf("%d ", printBit);

                /*calculate the new bit based on i and j*/
                int bit = ((i & (1 << j)) != 0);
                change_node(var_list, inputName, bit);
            }
            printf("|");

            /*CHANGE THIS LINE*/

            /*output time!!!*/
            /*read the output line*/
            char tempOutput[17];
            int tempOutputSize; 
            if (fscanf(file, " %16s %d", tempOutput, &tempOutputSize) != 2 || 
                strcmp(directive, "OUTPUT") != 0) {
                printf("missing OUTPUT directive in else portion\n");
                fclose(file);
                return EXIT_FAILURE;  
            }
            /*parse through the output variables*/

            /*JUST FOR THE SAKE OF READING*/
            for (int k = 0; k < tempOutputSize; k++) {
                char output_name[17];
                /*error check and add output variable*/
                if (fscanf(file, " %16s", output_name) != 1) {
                    printf("Error reading output variable\n");
                    fclose(file);
                    free_var_list(var_list);
                    return EXIT_FAILURE;  
                }
                int printbit = find_node(var_list,output_name);
                

                /*LINE TO PRINT OUTPUT IS BELOW*/


                printf(" %d",printbit);
            }
        }

        
        
        char gate[17];
        while (fscanf(file, "%s", gate) != EOF) {
                  
            if (strcmp(gate, "AND") == 0) {

                /*read in the arguments for the specified gate*/
                char input1[17], input2[17], output1[17];
                fscanf(file, " %16s %16s %16s", input1, input2, output1);

                int bit1 = -1;
                int bit2 = -1;
                
                int temp1, temp2;
                if(sscanf(input1, "%d", &temp1) == 1) //if you can convert input1 to an integer, store it in temp1
                   { bit1 = temp1; }    //if you can convert input1 to an integer, store that value in bit1
                 else if(sscanf(input1, "%d", &temp1) == 0) //if you can't convert input1 to an integer find the value corresponding to the input
                   { bit1 = find_node(var_list, input1); }
                

                if (sscanf(input2, "%d", &temp2) == 1)  
                   { bit2 = temp2; }
                else if (sscanf(input2, "%d", &temp2) == 0)
                   { bit2 = find_node(var_list, input2); }

                if (find_node(var_list, output1) == -1) { //check output to see
                    VarNode* new_node = create_node(output1, 0); /*add a variable that holds a bit to be changed later on*/
                    add_node(&var_list, new_node);
                }

                /*ADD GATE IMPLEMENTATION HERE*/
                int bitOperation = (bit1 & bit2);
                 
                change_node(var_list, output1, bitOperation);
            } 
            
            else if (strcmp(gate, "OR") == 0) {
                /*read in the arguments for the specified gate*/
                char input1[17], input2[17], output1[17];
                fscanf(file, " %16s %16s %16s", input1, input2, output1);

                int bit1 = -1;
                int bit2 = -1;
                
                int temp1, temp2;
                if(sscanf(input1, "%d", &temp1) == 1) //if you can convert input1 to an integer, store it in temp1
                   { bit1 = temp1; }    //if you can convert input1 to an integer, store that value in bit1
                 else if(sscanf(input1, "%d", &temp1) == 0) //if you can't convert input1 to an integer find the value corresponding to the input
                   { bit1 = find_node(var_list, input1); }
                

                if (sscanf(input2, "%d", &temp2) == 1)  
                   { bit2 = temp2; }
                else if (sscanf(input2, "%d", &temp2) == 0)
                   { bit2 = find_node(var_list, input2); }

                if (find_node(var_list, output1) == -1) { //check output to see
                    VarNode* new_node = create_node(output1, 0); /*add a variable that holds a bit to be changed later on*/
                    add_node(&var_list, new_node);
                }

                /*ADD GATE IMPLEMENTATION HERE*/
                int bitOperation = (bit1 || bit2);
                 
                change_node(var_list, output1, bitOperation);
                 

            } 
            
            else if (strcmp(gate, "NAND") == 0) {
                /*read in the arguments for the specified gate*/
                char input1[17], input2[17], output1[17];
                fscanf(file, " %16s %16s %16s", input1, input2, output1);

                int bit1 = -1;
                int bit2 = -1;
                
                int temp1, temp2;
                if(sscanf(input1, "%d", &temp1) == 1) //if you can convert input1 to an integer, store it in temp1
                   { bit1 = temp1; }    //if you can convert input1 to an integer, store that value in bit1
                 else if(sscanf(input1, "%d", &temp1) == 0) //if you can't convert input1 to an integer find the value corresponding to the input
                   { bit1 = find_node(var_list, input1); }
                

                if (sscanf(input2, "%d", &temp2) == 1)  
                   { bit2 = temp2; }
                else if (sscanf(input2, "%d", &temp2) == 0)
                   { bit2 = find_node(var_list, input2); }

                if (find_node(var_list, output1) == -1) { //check output to see
                    VarNode* new_node = create_node(output1, 0); /*add a variable that holds a bit to be changed later on*/
                    add_node(&var_list, new_node);
                }

                /*ADD GATE IMPLEMENTATION HERE*/
                int bitOperation = !(bit1 & bit2);
                 
                change_node(var_list, output1, bitOperation);
                 
            } 
            
            else if (strcmp(gate, "NOR") == 0) {
                /*read in the arguments for the specified gate*/
                char input1[17], input2[17], output1[17];
                fscanf(file, " %16s %16s %16s", input1, input2, output1);

                int bit1 = -1;
                int bit2 = -1;
                
                int temp1, temp2;
                if(sscanf(input1, "%d", &temp1) == 1) //if you can convert input1 to an integer, store it in temp1
                   { bit1 = temp1; }    //if you can convert input1 to an integer, store that value in bit1
                 else if(sscanf(input1, "%d", &temp1) == 0) //if you can't convert input1 to an integer find the value corresponding to the input
                   { bit1 = find_node(var_list, input1); }
                

                if (sscanf(input2, "%d", &temp2) == 1)  
                   { bit2 = temp2; }
                else if (sscanf(input2, "%d", &temp2) == 0)
                   { bit2 = find_node(var_list, input2); }

                if (find_node(var_list, output1) == -1) { //check output to see
                    VarNode* new_node = create_node(output1, 0); /*add a variable that holds a bit to be changed later on*/
                    add_node(&var_list, new_node);
                }

                /*ADD GATE IMPLEMENTATION HERE*/
                int bitOperation = !(bit1 || bit2);
                  
                change_node(var_list, output1, bitOperation);
                 
            } 
            
            else if (strcmp(gate, "XOR") == 0) {
                /*read in the arguments for the specified gate*/
                char input1[17], input2[17], output1[17];
                fscanf(file, " %16s %16s %16s", input1, input2, output1);

                int bit1 = -1;
                int bit2 = -1;
                
                int temp1, temp2;
                if(sscanf(input1, "%d", &temp1) == 1) //if you can convert input1 to an integer, store it in temp1
                   { bit1 = temp1; }    //if you can convert input1 to an integer, store that value in bit1
                 else if(sscanf(input1, "%d", &temp1) == 0) //if you can't convert input1 to an integer find the value corresponding to the input
                   { bit1 = find_node(var_list, input1); }
                

                if (sscanf(input2, "%d", &temp2) == 1)  
                   { bit2 = temp2; }
                else if (sscanf(input2, "%d", &temp2) == 0)
                   { bit2 = find_node(var_list, input2); }

                if (find_node(var_list, output1) == -1) { //check output to see
                    VarNode* new_node = create_node(output1, 0); /*add a variable that holds a bit to be changed later on*/
                    add_node(&var_list, new_node);
                }
                /*ADD GATE IMPLEMENTATION HERE*/
                int bitOperation = !(bit1 == bit2);
                 
                change_node(var_list, output1, bitOperation);
                
            } 
            
            else if (strcmp(gate, "NOT") == 0) {
                /*read in the arguments for the specified gate*/
                char input1[17], output1[17];
                fscanf(file, " %16s %16s", input1, output1);

                int bit1 = -1;
                
                int temp1;
                if(sscanf(input1, "%d", &temp1) == 1) //if you can convert input1 to an integer, store it in temp1
                   { bit1 = temp1; }    //if you can convert input1 to an integer, store that value in bit1
                 else if(sscanf(input1, "%d", &temp1) == 0) //if you can't convert input1 to an integer find the value corresponding to the input
                   { bit1 = find_node(var_list, input1); }

                if (find_node(var_list, output1) == -1) { //check output to see
                    VarNode* new_node = create_node(output1, 0); /*add a variable that holds a bit to be changed later on*/
                    add_node(&var_list, new_node);
                }

                /*ADD GATE IMPLEMENTATION HERE*/
                int bitOperation = !(bit1);
                
                change_node(var_list, output1, bitOperation);
                 
            } 
            
            else if (strcmp(gate, "PASS") == 0) {
                /*read in the arguments for the specified gate*/
                char input1[17], output1[17];
                fscanf(file, " %16s %16s", input1, output1);

                int bit1 = -1;
                
                int temp1;
                if(sscanf(input1, "%d", &temp1) == 1) //if you can convert input1 to an integer, store it in temp1
                   { bit1 = temp1; }    //if you can convert input1 to an integer, store that value in bit1
                 else if(sscanf(input1, "%d", &temp1) == 0) //if you can't convert input1 to an integer find the value corresponding to the input
                   { bit1 = find_node(var_list, input1); }

                if (find_node(var_list, output1) == -1) { //check output to see
                    VarNode* new_node = create_node(output1, 0); /*add a variable that holds a bit to be changed later on*/
                    add_node(&var_list, new_node);
                }
                
                change_node(var_list, output1, bit1);
            } 
            
            else if (strcmp(gate, "DECODER") == 0) {
                int decoderInput; 
                fscanf(file, "%d", &decoderInput); 
                
                char **inputVars = malloc(decoderInput * sizeof(char*));

                /*
                read each input --> no need to add it to linked list just use the array for this
                array that holds each INPUT variable
                inputVars[i] represents a single input variable
                */

                for (int i = 0; i < decoderInput; i++) {
                    inputVars[i] = malloc(17 * sizeof(char)); 
                    fscanf(file, " %16s", inputVars[i]);
                }


                int decoderOutput = 1 << decoderInput; // 2^n
                char **outputVars = malloc(decoderOutput * sizeof(char*)); 

                /*
                traverse through each output checking if it is a temp variable each time
                array that holds each output variable
                outputVars[i] represents a single output variable
                */

                for (int i = 0; i < decoderOutput; i++) {
                    outputVars[i] = malloc(17 * sizeof(char)); 
                    fscanf(file, " %16s", outputVars[i]);

                    /*check if temp var*/
                    if (find_node(var_list, outputVars[i]) == -1) {
                        VarNode* new_node = create_node(outputVars[i], 0);
                        add_node(&var_list, new_node);
                    }
                }


                /*at this point 
                    inputVars holds all the input variables and
                    outputVars holds all the output variables
                */

                



                int index = 0;
                for (int i = 0; i < decoderInput; i++) { //is decoderInput the right variable to use here?
                    int bit = 0;

                    if(strcmp(inputVars[i], "0") == 0) {
                        bit = 0;
                    } else if (strcmp(inputVars[i], "1") == 0) {
                        bit = 1;
                    } else {
                        bit = find_node(var_list, inputVars[i]);
                    }

                    /*
                    left-shifting and add current pbit
                    converts binary input to decimal index
                    */
                    index = (index << 1) | bit; 
                }

                //output
                for (int i = 0; i < decoderOutput; i++) {
                    /*index-th input is set to 1 and all others are 0*/
                    int valChange = (i == index) ? 1 : 0;
                    change_node(var_list, outputVars[i], valChange);
                }

                /* let it go...let it go...can't hold it back anymore */
                for (int i = 0; i < decoderInput; i++) {
                    free(inputVars[i]);
                }
                free(inputVars);

                for (int i = 0; i < decoderOutput; i++) {
                    free(outputVars[i]);
                }
                free(outputVars);
            } 

            else if (strcmp(gate, "MULTIPLEXER") == 0) {
                
                int muxN; //muxN = selectLines
                fscanf(file, "%d", &muxN);

                int multiInput1 = 1 << muxN; /*2^n inputLines*/ 


                char **inputVars1 = malloc(multiInput1 * sizeof(char*)); 
                for (int i = 0; i < multiInput1; i++) {
                    inputVars1[i] = malloc(17 * sizeof(char)); 
                    fscanf(file, " %16s", inputVars1[i]);
                }

                /*
                input1 has been read
                inputVars1[i] represents name of input variable
                */


                char multiOutput[17];
                char **inputVars2 = malloc(muxN * sizeof(char*)); /*inputVars2 = selectInputs*/

                for (int i = 0; i < muxN; i++) {
                    inputVars2[i] = malloc(17 * sizeof(char)); // Allocate memory for each string
                    fscanf(file, " %16s", inputVars2[i]);
                }

                /*
                input2 has been read
                inputVars2[2] represents name of input variable
                */

                fscanf(file, " %16s", multiOutput);
                /*check if it is a temp variable and add to linked list if needed*/
                if (find_node(var_list, multiOutput) == -1) {
                    VarNode* new_node = create_node(multiOutput, 0);
                    add_node(&var_list, new_node);
                }

                /*
                at this point all inputs/outputs have been read for the gate
                inputVars1[], inputVars2[], multiOutput
                
                */




                // Calculate the index based on select line values
                int index = 0;
                for (int s = 0; s < muxN; s++) {
                    int bit;
                    if (strcmp(inputVars2[s], "0") == 0) {
                        bit = 0;  
                    } else if (strcmp(inputVars2[s], "1") == 0) {
                        bit = 1;  
                    } else {
                        bit = find_node(var_list, inputVars2[s]);
                    }
                    index = (index << 1) | bit;
                }

                // Get the value of the selected input
                int selected_input_value;
                if (strcmp(inputVars1[index], "0") == 0) {
                    selected_input_value = 0;
                } else if (strcmp(inputVars1[index], "1") == 0) {
                    selected_input_value = 1;
                } else {
                    selected_input_value = find_node(var_list, inputVars1[index]);
                }

                // Set the output to the selected input value
                change_node(var_list, multiOutput, selected_input_value);

                // Free memory after use
                for (int i = 0; i < multiInput1; i++) {
                    free(inputVars1[i]);
                }
                free(inputVars1);

                for (int i = 0; i < muxN; i++) {
                    free(inputVars2[i]);
                }
                free(inputVars2);
            }
        }


    if (i != 0) printf("\n");
    rewind(file);

    }
    fclose(file);
    free_var_list(var_list);
    return 0;
     
}




/*there's no way*/