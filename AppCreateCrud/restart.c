#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define MAX_PATH_LENGTH 256
#define MAX_FILES 100

// Read the file and return an array of char* containing absolute paths
char** readFilesToRemove(const char* filename, int* numFiles) {
    FILE* file = fopen(filename, "r");
    if (!file) {
        perror("Error opening file");
        exit(EXIT_FAILURE);
    }

    char** paths = (char**)malloc(MAX_FILES * sizeof(char*));
    if (!paths) {
        perror("Memory allocation failed");
        exit(EXIT_FAILURE);
    }

    char buffer[MAX_PATH_LENGTH];
    int count = 0;
    while (fgets(buffer, sizeof(buffer), file)) {
        buffer[strcspn(buffer, "\n")] = '\0'; // Remove newline character
        paths[count] = strdup(buffer); // Allocate memory for each path
        count++;
    }

    fclose(file);
    *numFiles = count;
    return paths;
}

// Remove each file from the array of absolute paths
void removeFiles(char** paths, int numFiles) {
    for (int i = 0; i < numFiles; i++) {
        if (remove(paths[i]) == 0) {
            printf("Removed file: %s\n", paths[i]);
        } else {
            perror("Error removing file");
        }
        free(paths[i]); // Free memory for each path
    }
}

int main() {
    const char* filename = "filesToRemove.txt";
    int numFiles;
    char** paths = readFilesToRemove(filename, &numFiles);

    // Perform any additional processing if needed (e.g., validation)

    removeFiles(paths, numFiles);

    free(paths); // Free memory for the array of paths
    return 0;
}
