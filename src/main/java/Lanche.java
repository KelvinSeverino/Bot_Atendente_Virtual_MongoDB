import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Lanche {
	
	private String name;
	private Double preco;
	private String ingrediente1, ingrediente2, ingrediente3, ingrediente4;
	
	
	
	public Lanche(String name, Double preco, String ingrediente1, String ingrediente2, String ingrediente3,
			String ingrediente4) {
		this.name = name;
		this.preco = preco;
		this.ingrediente1 = ingrediente1;
		this.ingrediente2 = ingrediente2;
		this.ingrediente3 = ingrediente3;
		this.ingrediente4 = ingrediente4;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public Double getPreco() {
		return preco;
	}

	public void setPreco(Double preco) {
		this.preco = preco;
	}	
}
