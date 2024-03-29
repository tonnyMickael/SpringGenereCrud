import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import config.ConfigORM;

import Creating.ConstructionHTML;
import MetaData.Meta;
import MetaData.MetaTable;
import config.ConfigORM;
import controller.ControllerEntity;
import controller.GenerateControllerEntity;
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
import view.ViewModife;
import view.ViewModifeGenerate;
public class App {
    public static void main(String[] args) throws Exception {
        Scanner saisie = new Scanner(System.in);
        String continued = "no";
        List<ConfigORM> initConfiguration = new ArrayList<>();
        while (continued.equals("no")) {
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
            System.out.println("choose: 1-N or 1-1");
            String associateParentChild = saisie.nextLine();
        
            //saisie de l'association entre la table child et la table child
            System.out.println("Association => table child: "+Tablechild+", table parent: "+TableParent);
            System.out.println(".1-N => relation table: one to many");
            System.out.println(".1-1 => relation table: one to one");
            System.out.println("choose: 1-N or 1-1");
            String associateChildParent = saisie.nextLine();
        
            //saisie specification relation des table
            System.out.println("Relation between table parent and table child");
            System.out.println("if relation is unidirectional");
            System.out.println("=> unidirectional:true and bidirectional:false");
            System.err.println("else relation is bidirectionnal");
            System.out.println("=> unidirectional:false and bidirectional:true");
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
            System.out.println("choose cascade for the tables");
            System.out.println("choice: ALL or presist or merge or delete or none");
            String cascade = saisie.nextLine();
            if (cascade.equals("none")) {
                cascade = "";
            }
        
            //list des configuration de table
            initConfiguration.add(new ConfigORM(TableParent, Tablechild, associateParentChild, associateChildParent, unidirectional, bidirectional, cascade, sense));
        
            //condition de fin de la configuration
            System.out.println("Configuration is finished ? choose yes or no");
            String poursuivre = saisie.nextLine();
            continued = poursuivre;
        }
        saisie.close();
   

        // // ConfigORM (Config des relations des tables)
        // ConfigORM configORM = new ConfigORM();
        ConfigORM[] listConfigORM = initConfiguration.toArray(new ConfigORM[0]);
        
        DBConnection dbConnection = new DBConnection("5432","dbconfigcrudgenere","postgres","1234");
        DBManager dbManager = new DBManager(dbConnection);

        try {
            // Formulaire
            Meta data = new Meta();
            ArrayList<MetaTable> dataTable = data.metaDataBase(dbConnection.getConnection());
            ConstructionHTML htmlPage = new ConstructionHTML(dataTable,dbManager);

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

                // // Creation of repository
                // generateRepository = new GenerateRepository(repository);
                // generateRepository.createRepositoryEntity();

                // Creation of services
                // generateService.setService(service);
                // String valueMethodInUpdate = generateService.generateMethodInUpdate(entityFields);

                // // Generate Views
                // // List
                // ViewList viewList = new ViewList(entity.getName(), entityFields,"detail","update","delete");
                // ViewListGenerate viewListGenerate = new ViewListGenerate(viewList);
                // viewListGenerate.createViewListEntity();

                // // Generate ViewsUpdate
                // ViewModife viewmodife = new ViewModife(entity.getName(), entityFields);
                // ViewModifeGenerate viewListModifeGenerate = new ViewModifeGenerate(viewmodife);
                // viewListModifeGenerate.createViewModifeEntity();

                
                // // Detail
                // ViewDetail viewDetail = new ViewDetail(table, entityFields);
                // ViewDetailGenerate viewDetailGenerate = new ViewDetailGenerate(viewDetail,dbManager);
                // viewDetailGenerate.createViewDetailEntity();

                // // Generate Controller
                // ControllerEntity controllerEnity = new ControllerEntity(entity.getName());
                // controllerEnity.setIdTypEntity(service.getIdType());
                // controllerEnity.setEntityFields(entityFields);
                // GenerateControllerEntity generateControllerEntity = new GenerateControllerEntity(controllerEnity);
                // generateControllerEntity.createControllerEntity(); 

                for (int i = 0; i < listConfigORM.length; i++) {
                    ConfigORM configOrm = listConfigORM[i];
                    
                    // Creation of entity class or the entity
                    generateEntity.createAndWriteClass(configOrm);

                    // Creation service 
                    // generateService.createServiceEntity(configOrm,valueMethodInUpdate);
                }
            }   
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
}
