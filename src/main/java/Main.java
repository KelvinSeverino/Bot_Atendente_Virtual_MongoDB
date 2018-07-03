import com.mongodb.Mongo;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

public class Main {

	private static Model model;
	
	public static void main(String[] args) {

		model = Model.getInstance();
		initializeModel(model);
		View view = new View(model);
		model.registerObserver(view); //connection Model -> View
		model.Conectar();
		
		view.receiveMessageUser();

	}
	
	public static void initializeModel(Model model){
		
	}
}
