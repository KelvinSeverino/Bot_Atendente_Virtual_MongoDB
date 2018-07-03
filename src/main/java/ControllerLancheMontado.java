import com.pengrad.telegrambot.model.Update;

public class ControllerLancheMontado implements ControllerSearch{

	private Model model;
	private View view;
	
	public ControllerLancheMontado(Model model, View view) {
		this.model = model;
		this.view = view;
	}

	@Override
	public void search(Update update) {
		view.sendTypingMessage(update);
		model.searchLanche(update);
	}

}
