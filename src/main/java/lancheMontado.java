import java.util.LinkedList;
import java.util.List;

public class lancheMontado {
	private String nome;
	private Double preço;
	private List <Ingrediente> ingredientes = new LinkedList <> ();
	
	public lancheMontado(String nome, Double preço, List<Ingrediente> ingredientes) {
		super();
		this.nome = nome;
		this.preço = preço;
		this.ingredientes = ingredientes;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Double getPreço() {
		return preço;
	}

	public void setPreço(Double preço) {
		this.preço = preço;
	}

	public List<Ingrediente> getIngredientes() {
		return ingredientes;
	}

	public void setIngredientes(List<Ingrediente> ingredientes) {
		this.ingredientes = ingredientes;
	}
	
	

}
