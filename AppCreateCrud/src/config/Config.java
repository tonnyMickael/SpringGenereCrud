package config;

public class Config {
    /*
     * File Configuration
     * TEMPLATE_SOURCE_FOLDER_PATH (Folder all templates (file.tmpl) in your computer)
     * MODEL_DESTINATION_FOLDER_PATH (Folder all model to generate in your computer)
     * Soloina selon ny anarany ilay folder an'il PC ireo config Misy hoe (PATH)
     */

    // utiliser le fichier MetaDataTypeHTML pour la convertion en type de champ html des input 
    public static final String DATATYPE_HTML_PATH = "\\AppCreateCrud\\lib"; 

    // Template
    public static final String TEMPLATE_SOURCE_FOLDER_PATH="\\AppCreateCrud\\src\\template";
    // Model
    public static final String MODEL_DESTINATION_FOLDER_PATH="\\AppCreateCrud\\template_crud\\src\\main\\java\\com\\dev\\template_crud\\model";
    public static final String TEMPLATE_MODEL_SOURCE_FILE_NAME="Model.tmpl";
    // Repository
    public static final String REPOSITORY_DESTINATION_FOLDER_PATH="\\AppCreateCrud\\template_crud\\src\\main\\java\\com\\dev\\template_crud\\repository";
    public static final String TEMPLATE_REPOSITORY_SOURCE_FILE_NAME="Repository.tmpl";
    //Service
    public static final String SERVICE_DESTINATION_FOLDER_PATH="\\AppCreateCrud\\template_crud\\src\\main\\java\\com\\dev\\template_crud\\service";
    public static final String TEMPLATE_SERVICE_SOURCE_FILE_NAME="Service.tmpl";

    //Views
    // List
    public static final String VIEWSLIST_DESTINATION_FOLDER_PATH="\\AppCreateCrud\\template_crud\\src\\main\\resources\\templates";
    public static final String TEMPLATE_VIEWSLIST_SOURCE_FILE_NAME="ViewListe.tmpl";  
    public static final String TEMPLATE_VIEWSMODELSLIST_SOURCE_FILE_NAME="ViewModelsList.tmpl";  
    // Form
    public static final String TEMPLATE_FORMULAIRE_PAGE_PATH = "\\AppCreateCrud\\lib\\template";// utilise le fichier formulaire.txt dans template pour generer le contenu des formulaire et place les fichier creer dans ce repertoire 
    public static final String VIEWFORM_PAGE_DESTINATION_FOLDER_PATH = "\\AppCreateCrud\\template_crud\\src\\main\\resources\\templates";
    public static final String TEMPLATE_VIEWFORM_SOURCE_FILE_NAME="formulaire.txt";

    // ViewsUpdate
    public static final String VIEWSUPDATE_DESTINATION_FOLDER_PATH="\\AppCreateCrud\\template_crud\\src\\main\\resources\\templates";
    public static final String TEMPLATE_VIEWSUPDATE_SOURCE_FILE_NAME="ViewModifier.tmpl";  
    public static final String TEMPLATE_FORMULAIRE_PAGE1_PATH = "\\AppCreateCrud\\lib\\template";// utilise le fichier formulaire.txt dans template pour generer le contenu des formulaire et place les fichier creer dans ce repertoire 
    public static final String VIEWFORM_PAGE_DESTINATION1_FOLDER_PATH = "\\AppCreateCrud\\template_crud\\src\\main\\resources\\templates";
    // // ViewsUpdateInParent
    public static final String TEMPLATE_VIEWSUPDATE_CHILD_IN_PARENT_SOURCE_FILE_NAME="ViewModifieChildInParent.tmpl";  
    public static final String TEMPLATE_VIEWSUPDATE_PARENT_IN_CHILD_SOURCE_FILE_NAME="ViewModifieParentInChild.tmpl";  
    
    // Detail
    public static final String VIEWDETAIL_DESTINATION_FOLDER_PATH="\\AppCreateCrud\\template_crud\\src\\main\\resources\\templates";
    public static final String TEMPLATE_VIEWDETAIL_SOURCE_FILE_NAME="DetailFromListe.tmpl";

    //Controller 
    public static final String CONTROLLER_DESTINATION_FOLDER_PATH="\\AppCreateCrud\\template_crud\\src\\main\\java\\com\\dev\\template_crud\\controller";
    public static final String TEMPLATE_CONTROLLER_SOURCE_FILE_NAME="Controller.tmpl";  
    public static final String TEMPLATE_CONTROLLER_WITH_SECURITY_SOURCE_FILE_NAME="ControllerSecurity.tmpl";  
    // Tonny
    public static final String TEMPLATE_CONTROLLER_PAGE_PATH = "\\AppCreateCrud\\template_crud\\src\\main\\java\\com\\dev\\template_crud\\controller";   // utilise le fichier model.txt pour generer le contenu des controller et place les fichier creer dans ce repertoire

    /*
     * Place Holders in Model.tmpl
     */
    public static final String [] placeHoldersModel = {"[TableName]", "[EntityName]", "[EntityFields]", "[EntityMethods]"};

    /*
     * Place holders in Repository.tmpl
     */
    public static final String [] placeHoldersRepository = {"[ClassName]","[IdType]"};

    /*
     * Place holders in Service.tmpl
     */
    public static final String [] placeHoldersService = {"[ClassName]","[varClassName]","[add]","[fetch]","[update]","[delete]","[detail]"};

    /*
     * Place holders in ViewListe.tmpl
     */
    public static final String [] placeHoldersViewList = {"[nameEntity]","[columns]","[rows]","[detailName]","[updateName]","[deleteName]"};

    /*
     * Place holders in Controller.tmpl
     */
    public static final String [] placeHoldersController = {"[nameEntityMaj]","[nameEntity]","[idType]","[isAnyFieldNullOrEmpty]","[AutowiredchildEntity]","[RequestParamChildEntity]", "[AttributeChildEntity]", "[setChildEntity]","[supplementMethods]"};

    /*
     * Place holders in ViewDetail.tmpl
     */
    public static final String [] placeHoldersViewDetail = {"[nameEntity]","[fieldsTable]","[fieldChildTable]","[linkAddChild]"};

    /*
     * place holders in ViewUpdate.tmpl
     */
    public static final String [] placeHoldersViewUpdate = {"[nameEntity]","[inputs]","[inputFK]"};

    /*
     * place holders in ViewModifieChildInParent.tmpl
     */
    public static final String [] placeHoldersViewUpdateChildInParent ={"[childEntity]","[parentEntity]","[inputs]","[secondFieldParent]"};

    /*
     * place holders in ViewModifieChildInParent.tmpl
     */
    public static final String [] placeHoldersViewUpdateParentInChild ={"[childEntity]","[parentEntity]","[parentEntityMaj]","[fieldsTable]","[parentEntitySecondField]"};
    
    /*
     * place holders in ViewModifieChildInParent.tmpl
     */
    public static final String [] placeHoldersViewModelsList ={"[links]"};

}
