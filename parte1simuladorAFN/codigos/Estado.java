package trabalhoFTC;

public class Estado {

	int id;
	String id2;
	String apelido;
	boolean inicial;
	boolean aceite;
	
	Estado(){
		id2 = "";
		id = -1;
		apelido = "-1";
		inicial = false;
		aceite = false;
	}
	
	Estado(String a, String b){
		id2 = "";
		id = Integer.parseInt(a);
		apelido = b;
		inicial = false;
		aceite = false;
	}
	
	Estado(String a, String b, String c){
		id = Integer.parseInt(a);
		id2 = c;
		apelido = b;
		inicial = false;
		aceite = false;
	}
}
