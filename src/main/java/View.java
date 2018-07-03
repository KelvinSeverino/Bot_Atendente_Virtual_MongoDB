import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.stream.events.StartDocument;

import org.bson.Document;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.TelegramBotAdapter;
import com.pengrad.telegrambot.impl.UpdatesHandler;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ChatAction;
import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.GetUpdates;
import com.pengrad.telegrambot.request.SendChatAction;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.BaseResponse;
import com.pengrad.telegrambot.response.GetUpdatesResponse;
import com.pengrad.telegrambot.response.SendResponse;

public class View implements Observer{

	TelegramBot bot = TelegramBotAdapter.build("441684592:AAEZnUA2CUeJXfPWfpWaGIhPq6hXEyUs4wQ");

	// Object that receives messages
	GetUpdatesResponse updatesResponse;
	// Object that send responses
	SendResponse sendResponse;
	// Object that manage chat actions like "typing action"
	BaseResponse baseResponse;

	int queuesIndex = 0;

	ControllerSearch controllerSearch; // Strategy Pattern -- connection View -> Controller

	boolean searchBehaviour = false;
	static boolean novamente = false;
	private Model model;
	
	List<Update> updates;

	private String ingre = "";

	public void receiveMessageUser() {
		while (true) {
			updatesResponse = bot.execute(new GetUpdates().limit(100).offset(queuesIndex));

			updates = updatesResponse.updates();
			String resposta;

			for (Update update : updates) {
				queuesIndex = update.updateId()+1;
				resposta = update.message().text();
				
				if (this.searchBehaviour == true) {
					this.callController(update);
				} 
				else if (update.message().text().equals("Pagar")) {
					valorFinal(update);
				} else if (resposta.equals("Montar")) {
					montar(update);
				} else if (resposta.equals("Comprar")) {
					comprar(update);
				}
			}
		}
	}
	
	private void comprar(Update update) {
		setControllerSearch(new ControllerSearchLanche(model, this));
		int cont = 0;
		String ingre = "";
		sendResponse = bot.execute(new SendMessage(update.message().chat().id(),
				"Essas são as opções de lanches: "));
		DBCursor cursor = model.colecao.getCollection("Lanches").find();
		while (cursor.hasNext()) {
			cursor.next();
			ingre = cursor.curr().get("Ingrediente1") + ", " + cursor.curr().get("Ingrediente2") + ", " + cursor.curr().get("Ingrediente3") + ", " + cursor.curr().get("Ingrediente4");
			sendResponse = bot.execute(new SendMessage(update.message().chat().id(),
					cont + "-)Lanche:" + cursor.curr().get("Lanche") + "\nIngredientes:" + ingre + ".\nPreço: R$" + cursor.curr().get("Preço")));
			cont++;
		}
		sendResponse = bot.execute(new SendMessage(update.message().chat().id(),
				"Digite o nome do respectivo lanche que deseja comprar: "));
		this.searchBehaviour = true;
		
	}

	private void montar(Update update) {
		setControllerSearch(new ControllerSearchIngrediente(model, this));
		int cont = 0;
		sendResponse = bot.execute(new SendMessage(update.message().chat().id(),
				"Essas são as opções de ingredientes que você pode incrementar no seu lanche: "));
		
		DBCursor cursor = model.colecao.getCollection("Ingredientes").find();
		while (cursor.hasNext()) {
			sendResponse = bot.execute(new SendMessage(update.message().chat().id(),
					cont + "-)Ingrediente:" + cursor.next().get("Ingrediente") + "\nPreço: R$" + cursor.curr().get("Preço")));
			cont++;
		}
		
		sendResponse = bot.execute(new SendMessage(update.message().chat().id(),
				"Digite o nome do respectivo ingrediente que quer adicionar ao lanche: "));
		this.searchBehaviour = true;
	}
	
	public void valorFinal(Update update) {
		double valor = 0;
		int cont = 0;
		if (model.achei == true) {
			sendResponse = bot
					.execute(new SendMessage(update.message().chat().id(), "Os lanches comprados são: "));
			for (Lanche lanche : model.getLanchesComprados()) {
				if (model.getLanchesComprados().isEmpty()) {
					sendResponse = bot.execute(new SendMessage(update.message().chat().id(),
							"Você não comprou lanche, se quiser pagar compre primeiro ! :)"));
					break;
				}
				valor += lanche.getPreco();
				cont++;
				ingre += lanche.getName() + ", ";
			}
			sendResponse = bot.execute(new SendMessage(update.message().chat().id(),
					"Os lanches comprados são: " + ingre + "\nO preço total é: R$" + valor));
		}
		else if (model.acheiLanche == true) {
			
		}
	}

	public View(Model model) {
		this.model = model;
	}

	public void setControllerSearch(ControllerSearch controllerSearch) { // Strategy Pattern
		this.controllerSearch = controllerSearch;
	}

	public void callController(Update update) {
		this.controllerSearch.search(update);
	}

	public void update(long chatId, String studentsData) {
		sendResponse = bot.execute(new SendMessage(chatId, studentsData));
		this.searchBehaviour = false;
	}

	public void sendTypingMessage(Update update) {
		baseResponse = bot.execute(new SendChatAction(update.message().chat().id(), ChatAction.typing.name()));
	}

}