import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import Creating.ConstructionHTML;
import MetaData.Meta;
import MetaData.MetaTable;
import config.ConfigORM;
import config.ConfigSystem;
import controller.ControllerEntity;
import controller.GenerateControllerEntity;
import controller.GenerateControllerEntityWithSecurity;
import db.DBConnection;
import db.DBManager;
import function.FunctionUtils;
import model.EntityField;
import model.EntityTable;
import model.GenerateEntity;
import model.GenerateRepository;
import model.GenerateService;
import model.Repository;
import model.Service;
import view.ViewDetail;
import view.ViewDetailGenerate;
import view.ViewList;
import view.ViewListGenerate;
import view.ViewModelsListGenerate;
import view.ViewModife;
import view.ViewModifeGenerate;
import view.ViewModifieChildInParent;
import view.ViewModifieChildInParentGenerate;
import view.ViewModifieParentInChild;
import view.ViewModifieParentInChildGenerate;
public class App {
    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        Scanner system = new Scanner(System.in);
        System.out.println("Emplacement du projet:");
        String path = system.nextLine();
        ConfigSystem.path = path;

        // Configuration BDD
        System.out.println("Le port de la base de donnée");
        System.out.println("Port standard pour postgres: 5432");
        System.out.println("Port personnaliser: votre port");
        System.out.println("Port personnaliser: votre port personnalisé");
        System.out.println("Le port:");
        String port = system.nextLine();
        System.out.println("La base de donnée à utiliser:");
        String database = system.nextLine();
        System.out.println("L'utilisateur de la base de donnée:");
        String user = system.nextLine();
        System.out.println("Le mot de passe de la base de donnée:");
        String password = system.nextLine();

        System.out.println("CONFIGURATION DATABASE:");
        Scanner saisie = new Scanner(System.in);

        // Ajouter les modele a proteger
        List <String> modelToSecure = new ArrayList<>();
        System.out.println("Voulez-vous ajouter des configurations de sécurité pour des entités? Oui or Non?");
        String userResponse = saisie.nextLine();
        // Check if the user wants to add configurations
        if (userResponse.equalsIgnoreCase("Oui")) {
            System.out.println("Entrer les nom des entités (séparé par ', '):");
            String input = saisie.nextLine();

            // Split the input by spaces and store in a String array
            String[] entities = input.split(", ");
            
            // Print the entities
            System.out.println("Entities:");
            for (String entity : entities) {
                modelToSecure.add(entity);
                // modelToSecure.add(entities)
                System.out.println(entity);
            }
        } else {
            System.out.println("Aucun configurations a ajouté.");
        }

        String continued = "No";
        List<ConfigORM> initConfiguration = new ArrayList<>();
        while (continued.equals("No")) {
            // saisie de la table parent qui a la cle primaire/ primary key
            System.out.print("Table parent:");
            String TableParent = saisie.nextLine();
        
            // saisie de la table child qui a la cle etrangére/ foreign key de la table parent
            System.out.print("Table child:");
            String Tablechild = saisie.nextLine(); 
            
            //saisie de l'association entre la table parent et la table child
            System.out.println("Association => table parent: "+TableParent+", table child: "+Tablechild);
            System.out.println(".1-N => relation table: one to many");
            System.out.println(".1-1 => relation table: one to one");
            System.out.println("Choisissez: 1-N ou 1-1");
            String associateParentChild = saisie.nextLine();
        
            //saisie de l'association entre la table child et la table child
            System.out.println("Association => table child: "+Tablechild+", table parent: "+TableParent);
            System.out.println(".1-N => relation table: one to many");
            System.out.println(".1-1 => relation table: one to one");
            System.out.println("Choisissez: 1-N ou 1-1");
            String associateChildParent = saisie.nextLine();
        
            //saisie specification relation des table
            System.out.println("Relation between table parent and table child");
            System.out.println("Si la relation est unidirectional");
            System.out.println("=> unidirectional:true et bidirectional:false");
            System.err.println("Si la relation est bidirectionnal");
            System.out.println("=> unidirectional:false et bidirectional:true");
            System.out.println("unidirectionnal:");
            String unidirectionalStr = saisie.nextLine();
            boolean unidirectional = Boolean.parseBoolean(unidirectionalStr);
            System.out.println("bidirectional:");
            String bidirectionalStr = saisie.nextLine();
            boolean bidirectional = Boolean.parseBoolean(bidirectionalStr);
                    
            String sense ="";
            if (unidirectional == true) {
                
                //saisie du sens de unidirectional entre la table parent et la table child
                System.out.println("choose sense unidirectional:");
                System.out.println("1: if you want acces the "+TableParent+" from a "+Tablechild);
                System.out.println("2: if you want acces the "+Tablechild+" from a "+TableParent);
                System.err.println("choose: 1 or 2");
                sense = saisie.nextLine();
            }
            
            //saisie de la cascade pour les tables
            System.out.println("Choisissez les types de cascades pour les tables");
            System.out.println("Choix: ALL or PERSIST or MERGE or REMOVE or none");
            System.out.println("Nombre de cascade utiliser");
            int nbr = saisie.nextInt();
            String[] cascade = new String[nbr];
            for (int i = 0; i < cascade.length; i++) {
                System.out.println("Choisissez le(s) type(s) de cascade(s)");
                cascade[i] = saisie.next();
                if (cascade[i].equals("none")) {
                    cascade[i] = "";
                }
            }
            
            for (String cascades : cascade) {
                System.out.println(cascades);
            }
        
            //list des configuration de table
            initConfiguration.add(new ConfigORM(TableParent, Tablechild, associateParentChild, associateChildParent, unidirectional, bidirectional, cascade, sense,"","2"));
            
            //condition de fin de la configuration
            System.out.println("Avez vous fini de configurer? Oui ou No");
            String poursuivre = saisie.next();
            continued = poursuivre;
        }
        system.close();
        saisie.close();


        // ConfigORM (Config des relations des tables)C
        // ConfigORM configORM = new ConfigORM();
        ConfigORM[] listConfigORM = initConfiguration.toArray(new ConfigORM[0]);
        // ConfigORM[] listConfigORM = configORM.listORMConfig();

        //Configuration des modeles de securite
        String[] modeleToSecureInString = modelToSecure.toArray(new String [0]);
        
        DBConnection dbConnection = new DBConnection(port,database,user,password);
        // DBConnection dbConnection = new DBConnection("5432","dbconfigcrudgenere","postgres","1234");
        DBManager dbManager = new DBManager(dbConnection);

        try {
            // Formulaire
            Meta data = new Meta();
            ArrayList<MetaTable> dataTable = data.metaDataBase(dbConnection.getConnection());
            ConstructionHTML htmlPage = new ConstructionHTML(dataTable,dbManager, listConfigORM);

            // List view, service, repository, model, controller
            String[] tables = dbManager.allTables();
            System.out.println("Tables in the database:");
            EntityTable entity = new EntityTable(dbConnection);
            GenerateEntity generateEntity = new GenerateEntity(entity);
            GenerateRepository generateRepository = new GenerateRepository(null);
            GenerateService generateService = new GenerateService();
            for (String table : tables) {
                entity.setName(table);
                entity.columnsToEntityField();
                Repository repository = new Repository();
                repository.setClassName(entity.getName());
                Service service = new Service();
                service.setName(entity.getName());
                EntityField[] entityFields = entity.getEntityFields();
                // Generate Model, Repository ,Service
                
                for (EntityField entityField : entityFields) {
                    if(entityField.getName().equals("id")){
                        repository.setIdType(FunctionUtils.primitifToObject(entityField.getType()));
                        service.setIdType(FunctionUtils.primitifToObject(entityField.getType()));
                        break;
                    }
                }

                // Creation of repository
                generateRepository = new GenerateRepository(repository);
                generateRepository.createRepositoryEntity();

                // Creation of services
                generateService.setService(service);
                String valueMethodInUpdate = generateService.generateMethodInUpdate(entityFields);

                // Generate Views
                // List
                ViewList viewList = new ViewList(entity.getName(), entityFields,"detail","update","delete");
                ViewListGenerate viewListGenerate = new ViewListGenerate(viewList);
                viewListGenerate.createViewListEntity();

                // Generate ViewsUpdate
                ViewModife viewmodife = new ViewModife(entity.getName(), entityFields);
                ViewModifeGenerate viewModifeGenerate = new ViewModifeGenerate(viewmodife,dbManager);
                // viewModifeGenerate.createViewModifeEntity();

                // Generate ViewsUpdateChildInParent
                ViewModifieChildInParent viewModifieChildInParent = new ViewModifieChildInParent(entity.getName(), entityFields);
                ViewModifieChildInParentGenerate viewModifieChildInParentGenerate = new ViewModifieChildInParentGenerate(viewModifieChildInParent, dbManager);
                
                // Generate ViewsUpdateParentInChild
                ViewModifieParentInChild viewModifieParentInChild = new ViewModifieParentInChild(entity.getName(), entityFields);
                ViewModifieParentInChildGenerate viewModifieParentInChildGenerate = new ViewModifieParentInChildGenerate(viewModifieParentInChild, dbManager);

                // Detail
                ViewDetail viewDetail = new ViewDetail(table, entityFields);
                ViewDetailGenerate viewDetailGenerate = new ViewDetailGenerate(viewDetail,dbManager);

                // // Generate Controller
                ControllerEntity controllerEnity = new ControllerEntity(entity.getName());
                controllerEnity.setIdTypEntity(service.getIdType());
                controllerEnity.setEntityFields(entityFields);
                GenerateControllerEntity generateControllerEntity = new GenerateControllerEntity(controllerEnity);
                GenerateControllerEntityWithSecurity GenerateControllerEntityWithSecurity = new GenerateControllerEntityWithSecurity(controllerEnity);
                // generateControllerEntity.createControllerEntity(); 

                for (int i = 0; i < listConfigORM.length; i++) {
                    ConfigORM configOrm = listConfigORM[i];
                    
                    // Creation of entity class or the entity
                    generateEntity.createAndWriteClass(configOrm);

                    // Creation view detail list
                    viewDetailGenerate.createViewDetailEntity(configOrm);

                    // Creation view update 
                    viewModifeGenerate.createViewModifeEntity(configOrm);
                    viewModifieChildInParentGenerate.createViewModifeChildInParent(configOrm);
                    viewModifieParentInChildGenerate.createViewModifeParentInChild(configOrm);

                    // Creation service 
                    generateService.createServiceEntity(configOrm,valueMethodInUpdate);

                    // Creation Controller 
                    for (int j = 0; j < modeleToSecureInString.length; j++) {
                        String modelIndexToSecure = modeleToSecureInString[i];
                        if(table.equals(modelIndexToSecure)){
                            GenerateControllerEntityWithSecurity.createControllerEntitySecurity(configOrm);
                        }
                        else {
                            generateControllerEntity.createControllerEntity(configOrm);
                        }
                    }
                }
            }  
            ViewModelsListGenerate viewModelsListGenerate = new ViewModelsListGenerate(tables);
            viewModelsListGenerate.createViewModelsList();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
}
