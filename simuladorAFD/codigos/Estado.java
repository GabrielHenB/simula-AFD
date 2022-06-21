package trabalhoFTC;

public class Estado {

	int id;
	String apelido;
	boolean inicial;
	boolean aceite;
	
	Estado(){
		id = -1;
		apelido = "-1";
		inicial = false;
		aceite = false;
	}
	
	Estado(String a, String b){
		id = Integer.parseInt(a);
		apelido = b;
		inicial = false;
		aceite = false;
	}
}
