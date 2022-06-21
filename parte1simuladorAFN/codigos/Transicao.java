package trabalhoFTC;

public class Transicao {

	int fonte;
	int destino;
	String fonte2;
	String destino2;
	String simbolo;
	
	Transicao(){
		// -1 significa nenhum
		fonte = -1;
		destino = -1;
		simbolo = null;
	}
	Transicao(int a, int b, String c){
		fonte = a;
		destino = b;
		simbolo = c;
	}
	Transicao(String a, String b, String c){
		fonte = -2;
		destino = -2;
		fonte2 = a;
		destino2 = b;
		simbolo = c;
	}
}
