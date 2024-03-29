import java.util.ArrayList;

import Creating.ConstructionHTML;
import MetaData.Meta;
import MetaData.MetaTable;
import controller.ControllerEntity;
import controller.GenerateControllerEntity;
import db.DBConnection;
import db.DBManager;
import function.FunctionUtils;
import model.EntityField;
import model.EntityTable;
import model.GenerateEntity;
import model.GenerateEntity2;
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
            GenerateEntity2 generateEntity2 = new GenerateEntity2(entity);
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
                    // System.out.println("Columns: "+entityField.getName()+", type:"+entityField.getType()); 
                }
                // Creation of entity class or the entity
                // generateEntity.createAndWriteClass();
                generateEntity2.createAndWriteClass();
                // // Creation of repository
                // generateRepository = new GenerateRepository(repository);
                // generateRepository.createRepositoryEntity();
                // // Creation of services
                // generateService.setService(service);
                // String valueMethodInUpdate = generateService.generateMethodInUpdate(entityFields);
                // generateService.createServiceEntity(valueMethodInUpdate);

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
            }   
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
}
