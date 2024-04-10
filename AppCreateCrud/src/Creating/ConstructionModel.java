package Creating;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import MetaData.MetaTable;
import config.Config;

public class ConstructionModel {

    public ConstructionModel(ArrayList<MetaTable> data) throws IOException{
        createModel(data);
    }

    private void createModel(ArrayList<MetaTable> data) throws IOException{
        
        for (int i = 0; i < data.size(); i++) {
            //rendre majuscule premier character
            StringBuilder sb = new StringBuilder(data.get(i).getNameTable());
            sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
            String nameModel = sb.toString()+"Controller";

            // Créer un objet File représentant le fichier à créer.
            File fichier = new File(Config.TEMPLATE_FORMULAIRE_PAGE_PATH+"/"+nameModel+".java");

            // Si le fichier n'existe pas, le créer.
            if (!fichier.exists()) {
                fichier.createNewFile();    
            }
            WriteModel(nameModel,data.get(i).getNameTable());
        }            
    } 

    private void WriteModel(String nameClass, String url) throws IOException{
        // File paths (replace with your actual paths)
        String templatePath = Config.DATATYPE_HTML_PATH+"/Model/model.txt";
        String outputPath = Config.TEMPLATE_CONTROLLER_PAGE_PATH+"/"+nameClass+".java";

        // Replacements (adjust key-value pairs as needed)
        String[][] replacements = {
                {"package", "package com.dev.template_crud.controller;"},
                {"nameClass", nameClass}, // Replace with your desired class name
                {"urlclass", url},  // Replace with your class name (lowercase, hyphen-separated)
                {"navigationUrlForm", url},  // Replace with your form navigation URL segment
                {"urlForm", url+"-form"},         // Replace with your form template file name
                {"navigationUrlList", url}, // Replace with your list navigation URL segment
                {"UrlList", url+"-list"}        // Replace with your list template file name
        };

        // Read the template file
        String templateContent = readFromFile(templatePath);

        // Apply replacements
        for (String[] replacement : replacements) {
            templateContent = templateContent.replaceAll("\\{" + replacement[0] + "\\}", replacement[1]);
        }

        // Write the modified content to a new file
        writeToFile(outputPath, templateContent);

        // System.out.println("Template processed and output file created successfully!");
    }

    private static String readFromFile(String filePath) throws IOException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        }
        return content.toString();
    }

    private static void writeToFile(String filePath, String content) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(content);
        }
    }
}
