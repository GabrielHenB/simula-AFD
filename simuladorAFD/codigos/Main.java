package trabalhoFTC;


import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Main {
	
	public static void apresenta() {
		System.out.println("----------[Simulador de AFD]----------");
		System.out.println("-- Para iniciar indique o nome do    --");
		System.out.println("-- arquivo que contem a representa-  --");
		System.out.println("-- -ção da AFD no formato do jflap   --");
		System.out.println("-- ou aperte ENTER                  --");
		System.out.println("-- para usar a entrada.jff          -- ");
		System.out.println("---------------------------------------");
		System.out.println("> Digite apenas o nome do arquivo ex: entrada");
	}
	
	public static void apresenta2(int tamE, int tamT) {
		System.out.println("----------[Simulador de AFD]------------");
		System.out.println("-- AUTOMATO IDENTIFICADO COM SUCESSO  --");
		System.out.println("-- Numero de Estados: " + tamE + " --");
		System.out.println("-- Numero de Transicoes: " + tamT + " --");
		System.out.println("----------------------------------------");
	}
	
	public static void imprimeLista(ArrayList<Estado> a) {
		System.out.println("==ESTADOS ENCONTRADOS==");
		for(Estado e: a) {
			System.out.println("Estado n" + e.id);
			System.out.println("Apelido: " + e.apelido);
		}
	}
	
	public static void imprimeTrans(ArrayList<Transicao> transi) {
		System.out.println("==TRANSICOES ENCONTRADAS==");
		for(Transicao t: transi) {
			System.out.println("De estado id: " + t.fonte);
			System.out.println("Para estado id: " + t.destino);
			System.out.println("Simbolo = " + t.simbolo);
		}
	}
	
	public static Automato conversor(ArrayList<Estado> E, ArrayList<Transicao> T) {
		//converter um afn em afd
		Automato afd = new Automato();
		ArrayList<Estado> eLinha = new ArrayList<Estado>();
		ArrayList<Estado> X = new ArrayList<Estado>();
		String Y = "";
		ArrayList<Transicao> sigmaLinha = new ArrayList<Transicao>();
		Estado atual = new Estado();
		Estado inicial = new Estado();
		boolean temTransicao = false;
		boolean isIgual = false;
		int bigM = E.size() + 10; //Depois monta IDs novos.
		
		
		System.out.println("> Iniciando conversão do AFN.....");
		for(int o = 0; o < E.size(); o++) {
			//Busca estado inicial
			if(E.get(o).inicial) {
				eLinha.add(E.get(o)); //adiciona estado inicial
				inicial = E.get(o);
				atual = inicial;
				System.out.println(">> Estado inicial encontrado: ");
				System.out.println(">> Inicial = " + inicial.apelido);
				X.add(inicial); //O X contem todos estados que serao testados.
			}
		}
		if(inicial.id == -1) {
			return null;
		}
		else {
			for(int p = 0; p < eLinha.size(); p++) {
				System.out.println("[[Iteração de Conversão n" +p);
				temTransicao = false;
				for(int o = 0; o < T.size(); o++) {
					//Itera sobre as transicoes do estado atual
					//Se existe transicao que ele eh a fonte
					//Ele adiciona o destino no vetor X
					//O vetor X se tornara um novo estado
					System.out.println("[ Iteração de Alfabeto n" + o);
					System.out.println("[ Estado atual = " + atual.apelido + "][ID = " + atual.id);
					X.add(atual);
					String temp = ""+atual.id;
					System.out.println("[" + temp + "]");
					for(int k = 0; k < temp.length(); k++) {
						//Testa todos r dentro de X para um mesmo simbolo.
						System.out.println("[ Analisando ID de char = " + temp.charAt(k));
						String temp2 = ""+T.get(o).fonte;
						System.out.println("[ transicao de " + temp2 + "]");
						for(int l = 0; l < temp2.length(); l++) {
							if(temp2.charAt(l) == temp.charAt(k)) {
								//Adiciona estado destino ao conjunto teste.
								System.out.println("[ para " + T.get(o).destino + "]");
								System.out.println("> ESTADO destino encontrado, adicionando a X...");
								X.add(new Estado(""+T.get(o).destino,""+T.get(o).destino));
								Y = T.get(o).simbolo;
								temTransicao = true;
								System.out.println("[[Executou sobre transição: ");
								System.out.println("[ de " + T.get(o).fonte + " para " + T.get(o).destino);
							}
							else {
								System.out.println("[ nao diz respeito a fonte ]");
							}
						}
					}
					//Para cada iteração em X[a,a1,..an] adicionar resultado em e linha
					if(temTransicao) {
						String auxID = "";
						int comparator = -1;
						String auxAlias = "";
						for(Estado est: X) {
							if(est.id != comparator) {
								auxID = auxID + est.id;
								auxAlias = auxAlias + est.apelido;
							}
							comparator = est.id;
						}
						//Cria estado novo que é a juncao dos outros
						Estado novoE = new Estado(auxID,auxAlias);
						isIgual = false;
                        for(int j = 0; j < eLinha.size(); j++) {
							if(eLinha.get(j).id == novoE.id) {
								isIgual = true;
							}
						}
                        if(!isIgual) {
                        	//Adiciona transicao com simbolo do atual pra ele
    						sigmaLinha.add(new Transicao(atual.id,novoE.id,Y));
    						eLinha.add(novoE);
    						//Apos adicionar no eLinha, o atual se torna o novo estado
    						atual = novoE;
    						X.clear();
    						Y = "";
    						//o = T.size();
                        }
					}
				}
				System.out.println("[Algoritmo iterou por todo E']");
				afd = new Automato(eLinha,sigmaLinha);
				return afd;
			}
		}
		
		
		return afd;
	}
	
	public static int simulador(ArrayList<Estado> E, ArrayList<Transicao> T, String fita) {
		// Retorna o ID do estado que aceiou ou -1 se nao aceitou.
		Estado inicial = new Estado();
		Estado eAtual = new Estado();
		int atual = -1;
		int posicaoFita = 0;
		boolean temTransicao = false;
		//teste e debug:
		//int limiteIteracoes = E.size()+T.size();
		
		System.out.println("> Iniciando simulação do AFD.....");
		for(int o = 0; o < E.size(); o++) {
			//Busca estado inicial
			if(E.get(o).inicial) {
				inicial = E.get(o);
				atual = inicial.id;
				eAtual = inicial;
				System.out.println(">> Estado inicial encontrado: ");
				System.out.println(">> E = " + inicial.apelido);
			}
		}
		if(inicial.id == -1) {
			return -1;
		}
		else {
			while(posicaoFita < fita.length()) {
				//Executa enquanto a fita nao tiver acabado
				System.out.println(">>>> Processando leitura de fita: ");
				System.out.println("> Estado atual: " + eAtual.apelido + "|ID = " + eAtual.id);
				System.out.println("> Simbolo atual: " + fita.charAt(posicaoFita));
				temTransicao = false;
				for(int o = 0; o < T.size(); o++) {
					if(T.get(o).fonte == atual) {
						if(fita.charAt(posicaoFita) == T.get(o).simbolo.charAt(0)) {
							//Se a transicao gasta o simbolo da fita
							posicaoFita++;
							atual = T.get(o).destino;
							eAtual = E.get(atual);
							temTransicao = true;
							//Move o atual para o id do estado destino
							System.out.println(">> Transicao ativada");
							System.out.println(">> No destino: " + eAtual.apelido);
							//Sai do for apos transicao pois precisa testar toda a lista para o proximo estado
							o = T.size();
						}
					}
				}
				if(!temTransicao) {
					System.out.println(">>>> Não foi encontrada transicao para o simbolo = " + fita.charAt(posicaoFita));
					System.out.println(">>>> No estado = " + eAtual.apelido + "|de ID = " + eAtual.id);
					break;
				}
				//teste
				//limiteIteracoes--;
				//if(limiteIteracoes < 0) break;
			}
			if(eAtual.aceite) {
				System.out.println(">>>>>>>>>>>>> FIM DA SIMULACAO");
				if(posicaoFita == fita.length()) {
					System.out.println("> Resultado: Aceitou");
					System.out.println("> Parou no estado: ID= " + eAtual.id + " de nome: " + eAtual.apelido);
					return eAtual.id;
				}
				else {
					System.out.println("> Resultado: Não aceitou: Fita ainda contem elementos");
					System.out.print("> Fita : ");
					for(int l = posicaoFita; l < fita.length(); l++) {
						System.out.print(fita.charAt(l));
					}
					System.out.println(" ");
					return -1;
				}
				
			}
			else {
				System.out.println(">>>>>>>>>>>>> FIM DA SIMULACAO");
				System.out.println("> Resultado: Não Aceite");
				System.out.println("> Parou no estado: ID= " + eAtual.id + " de nome: " + eAtual.apelido);
				return -1;
			}
			
		}
	}

	public static void main(String[] args) {
		Scanner en = new Scanner(System.in);
		boolean semaforo = true;
		String fileName = "entrada.jff";
		ArrayList<Estado> conjuntoEstados = new ArrayList<Estado>(); //guardar estados do automato
		ArrayList<Transicao> conjuntoTransicoes = new ArrayList<Transicao>(); //guardar transicoes do automato
		
		
		//INICIO - MENU
		while(semaforo) {
			apresenta();
			fileName = en.nextLine();
			if(fileName.length() == 0) {
				System.out.println("Entrada.jff selecionada: " + fileName);
				System.out.println("Retornando...");
				fileName = "entrada";
			}
			
			fileName = fileName + ".jff";
			System.out.println(">" + fileName);
			//String userDirectory = System.getProperty("user.dir");
			 String userDirectory = FileSystems.getDefault()
		                .getPath("")
		                .toAbsolutePath()
		                .toString();
			fileName = userDirectory + "\\" + fileName;
			System.out.println(">" + fileName);
			semaforo = false;
			
		}
		//en.close();
		
		//INICIO - LEITURA DO ARQUIVO XML E CRIACAO DO AUTOMATO
		File xml;
		String leitura;
		try {
			xml = new File(fileName);
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(xml);
			Node estado; //usado para receber estado do DOM XML
			Node temp; //usado para receber os atributos do Elemento XML
			Estado receptor = new Estado("-1","-1"); //inicializa com -1 como ID.
			
			//Variaveis utilizadas para guardar transicoes
			Node aresta;
			Transicao receptora = new Transicao();
			
			doc.getDocumentElement();
			System.out.println("Arquivo identificado, raiz: " + doc.getDocumentElement().getNodeName());
			
			//Obter lista de <state> do dom
			NodeList estados = doc.getElementsByTagName("state");
			
			
			for(int i = 0; i < estados.getLength(); i++) {
				//Para cada estado um no e iterado
				estado = estados.item(i);
				NamedNodeMap map = estado.getAttributes();
				for(int j = 0; j < map.getLength(); j++) {
					//Itera por cada atributo do elemento
					temp = map.item(j); //pega o par ID NOME
					
					if(temp.getNodeName().equals("id")) {
						//Se for ID entao eh novo estado portanto instancia denovo
						receptor = new Estado();
						receptor.id = Integer.parseInt(temp.getNodeValue());
					}
					else {
						receptor.apelido = temp.getNodeValue();
					}
				}
				//Verificar se ele é de aceite e inicial. Itera pelos elementos xml filhos do atual.
				NodeList auxiliar = estado.getChildNodes();
				for(int k = 0; k < auxiliar.getLength(); k++) {
					if(auxiliar.item(k).getNodeName().equals("initial")) {
						receptor.inicial = true;
					}
					if(auxiliar.item(k).getNodeName().equals("final")) {
						receptor.aceite = true;
					}
				}
				if(receptor != null) conjuntoEstados.add(receptor);
			}
			//Imprime estados
			imprimeLista(conjuntoEstados);
			System.out.println("....");
			
			NodeList trans = doc.getElementsByTagName("transition");
			System.out.println("Identificadas [" + trans.getLength() + "] transicoes");
			for(int i = 0; i<trans.getLength(); i++) {
				//Itera pelos nos <transition> do xml
				aresta = trans.item(i);
				if(aresta.getNodeType() == Node.ELEMENT_NODE) {
					Element elemento = (Element)aresta;
					int de = Integer.parseInt(elemento.getElementsByTagName("from").item(0).getTextContent());
					int ate = Integer.parseInt(elemento.getElementsByTagName("to").item(0).getTextContent());
					String le = elemento.getElementsByTagName("read").item(0).getTextContent();
					
					receptora = new Transicao(de,ate,le);
					System.out.println("T> de " + receptora.fonte + "|para " +receptora.destino +"|le: "+ receptora.simbolo);
					conjuntoTransicoes.add(receptora);
				}
			}
			//Imprime Transicoes
			imprimeTrans(conjuntoTransicoes);
			
			// Obtendo a frase para a simulaçao
			System.out.println("====================================");
			String fita;
			
			apresenta2(conjuntoEstados.size(),conjuntoTransicoes.size());
			System.out.println("> Digite a frase a ser testada: ");
			fita = en.nextLine();
			
			//en.close();
			System.out.println("Fita de entrada identificada = " + fita);
			System.out.println("================= SIMULAÇAO 1 ======================");
			int result = -1;
			//result = simulador(conjuntoEstados,conjuntoTransicoes,fita);
			Automato a = conversor(conjuntoEstados,conjuntoTransicoes);
			result = simulador(a.E,a.T,fita);
			if(result >= 0) {
				System.out.println("===================================================");
				System.out.println("= Simulação encerrada com sucesso");
				System.out.println("= Terminou no estado de ID = " + result);
				System.out.println("===================================================");
			}
			else {
				System.out.println("===================================================");
				System.out.println("= Simulação encerrada sem sucesso");
				System.out.println("= Terminou no estado de ID = " + result);
				System.out.println("===================================================");
			}
			System.out.println(">Simulador encerrado, para simular outra entrada abra o .bat novamente.");
			System.out.println(">Pressione qualquer tecla para fechar");
			en.nextLine();
			en.close();
			
			
			
		}catch(Exception ioerro) {
			ioerro.printStackTrace();
		}
		
		
		
	}

}
