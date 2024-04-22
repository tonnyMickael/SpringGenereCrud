package model;

import java.io.File;

import config.Config;
import config.ConfigSystem;
import function.FunctionUtils;

/**
 * GenerateRepository
 */
public class GenerateRepository {

    private Repository repository;

    public GenerateRepository(Repository repository) {
        this.repository = repository;
    }

    public Repository getRepository() {
        return repository;
    }

    public void setRepository(Repository repository) {
        this.repository = repository;
    }
    
    private String []listRealValues(){
        String className = FunctionUtils.firstLetterToUpperCase(this.repository.getClassName());
        String idType = this.repository.getIdType();
        String [] values = {className,idType};
        return values;
    }

    private String formatClassNameRepository(){
        return this.getRepository().getClassName()+"Repository";
    }

    public void createRepositoryEntity(){
        File destinationFile = new File(ConfigSystem.path + Config.REPOSITORY_DESTINATION_FOLDER_PATH, FunctionUtils.formatToFileJava(formatClassNameRepository()));
        
        if(!destinationFile.exists()){
            FunctionUtils.replacePholders(FunctionUtils.formatToFileJava(formatClassNameRepository()), Config.placeHoldersRepository, listRealValues(),ConfigSystem.path + Config.TEMPLATE_SOURCE_FOLDER_PATH,Config.TEMPLATE_REPOSITORY_SOURCE_FILE_NAME,ConfigSystem.path + Config.REPOSITORY_DESTINATION_FOLDER_PATH);
        }
        else{
            System.out.println(FunctionUtils.formatToFileJava(formatClassNameRepository())+" :File Already exist...");
        }  
    }
}