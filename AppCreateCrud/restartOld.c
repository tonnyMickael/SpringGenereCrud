#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <dirent.h>

#define MAX_PATHS 100

// Function to read paths from a file and return an array of strings
char** read_paths_from_file(const char* filename, int* num_paths) {
    FILE* file = fopen(filename, "r");
    if (!file) {
        perror("Error opening file");
        return NULL;
    }

    char** paths = (char**)malloc(MAX_PATHS * sizeof(char*));
    if (!paths) {
        perror("Memory allocation failed");
        fclose(file);
        return NULL;
    }

    char buffer[256]; // Adjust buffer size as needed
    int count = 0;

    while (fgets(buffer, sizeof(buffer), file)) {
        // Remove newline character if present
        size_t len = strlen(buffer);
        if (len > 0 && buffer[len - 1] == '\n') {
            buffer[len - 1] = '\0';
        }

        // Allocate memory for the path and copy it
        paths[count] = strdup(buffer);
        if (!paths[count]) {
            perror("Memory allocation failed");
            break;
        }

        count++;
        if (count >= MAX_PATHS) {
            break; // Reached the maximum number of paths
        }
    }

    fclose(file);
    *num_paths = count;
    return paths;
}

void deleteFilesExceptInPath(const char* folderPath, const char* filePath) {
    DIR* dir;
    struct dirent* entry;

    // Open the directory
    dir = opendir(folderPath);
    if (!dir) {
        perror("Error opening directory");
        exit(EXIT_FAILURE);
    }

    // Iterate through directory entries
    while ((entry = readdir(dir)) != NULL) {
        if (entry->d_type == DT_REG) { // Regular file
            char fullFilePath[256];
            snprintf(fullFilePath, sizeof(fullFilePath), "%s/%s", folderPath, entry->d_name);

            // Check if the file path matches the specified filePath
            if (strcmp(fullFilePath, filePath) != 0) {
                // Delete the file
                if (remove(fullFilePath) == 0) {
                    printf("Deleted: %s\n", fullFilePath);
                } else {
                    perror("Error deleting file");
                }
            }
        }
    }

    closedir(dir);
}

int main() {
    const char* dirToKeep = "fileToKeep.txt"; // Specify your file name here
    const char* fileDirectories = "fileDirectories.txt"; // Specify your file name here
    int num_pathsToKeep;
    int num_pathDirectories;
    char** pathsToKeep = read_paths_from_file(dirToKeep, &num_pathsToKeep);
    char** pathsDirectories = read_paths_from_file(fileDirectories, &num_pathDirectories);


    if (!pathsDirectories) {
        printf("Error reading paths from file.\n"); 
        return 1;
    }

    for (int i = 0; i < num_pathsToKeep; i++)
    {
        for(int j = 0; j<num_pathDirectories; j++){
         deleteFilesExceptInPath(pathsDirectories[j],pathsToKeep[i]);
        }
    }

        // Print the read paths (for demonstration purposes)
    printf("Read %d paths from the file:\n", num_pathsToKeep);
    for (int i = 0; i < num_pathsToKeep; i++) {
        printf("%d: %s\n", i + 1, pathsToKeep[i]);
    }

    // Print the read paths (for demonstration purposes)
    printf("Read %d paths from the file:\n", num_pathDirectories);
    for (int i = 0; i < num_pathDirectories; i++) {
        printf("%d: %s\n", i + 1, pathsDirectories[i]);
    }

    // Clean up: free allocated memory
    for (int i = 0; i < num_pathDirectories; i++) {
        free(pathsDirectories[i]);
    }
    free(pathsDirectories);

    return 0;
    // const char* folderPath = "C:/Documents/files";
    // const char* filePath1 = "C:/Documents/files/index.html";
    // const char* filePath2 = "C:/Documents/files/video/one_day.MP4";

    // deleteFilesExceptInPath(folderPath, filePath1);
    // deleteFilesExceptInPath(folderPath, filePath2);

    // return 0;
}
