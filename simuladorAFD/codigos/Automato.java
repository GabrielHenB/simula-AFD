package trabalhoFTC;

import java.util.ArrayList;

public class Automato {

	ArrayList<Estado> E;
	ArrayList<Transicao> T;
	
	Automato(ArrayList<Estado> a, ArrayList<Transicao> b){
		E = a;
		T = b;
	}
	
	Automato(){
		E = null;
		T = null;
	}
}
