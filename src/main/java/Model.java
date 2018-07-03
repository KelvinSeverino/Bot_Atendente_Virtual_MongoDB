import java.net.PasswordAuthentication;
import java.time.format.TextStyle;import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.bson.Document;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;

public class Model implements Subject {

	private List<Observer> observers = new LinkedList<Observer>();
	private List<Ingrediente> ingreLanches = new LinkedList<Ingrediente>();
	private List<Lanche> lanchesComprados = new LinkedList<Lanche>();

	private static Model uniqueInstance;
	SendResponse sendResponse;
	private View view = new View(null);

	public static Model getInstance() {
		if (uniqueInstance == null) {
			uniqueInstance = new Model();
		}
		return uniqueInstance;
	}

	public void registerObserver(Observer observer) {
		observers.add(observer);
	}

	public void notifyObservers(long chatId, String studentsData) {
		for (Observer observer : observers) {
			observer.update(chatId, studentsData);
		}
	}


	public void searchLanche(Update update) {
		DBCursor cursor = colecao.getCollection("Lanches").find();
		while (cursor.hasNext()) {
			if (cursor.next().get("Lanche").equals(update.message().text())) {
				Lanche lanche = new Lanche((String) cursor.curr().get("Lanche"), (Double) cursor.curr().get("Preço"), (String) cursor.curr().get("Ingrediente1"),(String) cursor.curr().get("Ingrediente2"), (String) cursor.curr().get("Ingrediente3"), (String) cursor.curr().get("Ingrediente4"));
				lanchesComprados.add(lanche);
				acheiLanche = true;
				this.notifyObservers(update.message().chat().id(), "Lanche adicionado !");
			}
		}

	}
	boolean acheiLanche = false;
	boolean achei = false;

	public void searchIngredientes(Update update) {
		mostrar(update);
		if (achei == false) {
			this.notifyObservers(update.message().chat().id(), "Ingrediente não cadastrado !");
			view.novamente = true;
		} else {
			view.searchBehaviour = false;
		}
	}

	DB BaseDados;
	DBCollection colecao;
	BasicDBObject document = new BasicDBObject();

	public void Conectar() {
		Mongo mongo = new Mongo("localhost");
		BaseDados = mongo.getDB("DBLanches");
		colecao = BaseDados.getCollection("Ingredientes");
		colecao = BaseDados.getCollection("Lanches");
		System.out.println("Conexão efetuada com sucesso !");
	}

	public boolean Inserir() {
		/*
		 * document.put("Ingrediente","Cebola"); document.put("Preço", 0.25);
		 * colecao.insert(document); document.clear();
		 * document.put("Ingrediente","Hamburguer"); document.put("Preço", 0.50);
		 * colecao.insert(document); document.clear();
		 * document.put("Ingrediente","Cheedar"); document.put("Preço", 0.20);
		 * colecao.insert(document); document.clear();
		 * document.put("Ingrediente","Tomate"); document.put("Preço", 0.30);
		 * colecao.insert(document); document.clear();
		 * document.put("Ingrediente","Alface"); document.put("Preço", 0.15);
		 * colecao.insert(document); document.clear();
		 * document.put("Ingrediente","Queijo"); document.put("Preço", 0.50);
		 * colecao.insert(document); document.clear(); lan.add(new
		 * BasicDBObject("Lanche", "X-Burguer").append("Ingrediente1", "Hamburguer")
		 * .append("Ingrediente2", "Queijo").append("Ingrediente3",
		 * "Cheedar").append("Ingrediente4", "Tomate") .append("Preço", (Double) 8.50));
		 * System.out.println("Inserido com Sucesso !\n");
		 * 
		 * lan.add(new BasicDBObject("Lanche", "X-Salada") .append("Ingrediente1",
		 * "Hamburguer") .append("Ingrediente2", "Alface") .append("Ingrediente3",
		 * "Tomate") .append("Ingrediente4", "Queijo") .append("Preço", (Double)
		 * 12.50)); colecao.getCollection("Lanches").insert(lan);
		 * System.out.println("Inserido com Sucesso !");
		 */
		return true;
	}

	@SuppressWarnings("deprecation")
	public void mostrar(Update update) {
		DBCursor cursor = colecao.getCollection("Ingredientes").find();
		while (cursor.hasNext()) {
			if (cursor.next().get("Ingrediente").equals(update.message().text())) {
				Double gambiarra = (Double) cursor.curr().get("Preço");
				ingreLanches.add(new Ingrediente(update.message().text(), gambiarra));
				view.novamente = true;
				achei = true;
				this.notifyObservers(update.message().chat().id(), "Ingrediente adicionado !");
			}
		}
	}

	public List<Ingrediente> getIngreLanches() {
		return ingreLanches;
	}

	public void setIngreLanches(List<Ingrediente> ingreLanches) {
		this.ingreLanches = ingreLanches;
	}

	public List<Lanche> getLanchesComprados() {
		return lanchesComprados;
	}

	public void setLanchesComprados(List<Lanche> lanchesComprados) {
		this.lanchesComprados = lanchesComprados;
	}

}
