import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Graph {
  /* MinLa: Para sacar el costo entre nodos o el costo total del grafo 
  se usa la formula: : abs(sourceNode-destinationNode) */
	private static Scanner input;

	public List<Node> nodesList=new ArrayList<Node>(); //Lista de los nodos en el grafo
	public List<Integer> numbersAlreadyIn=new ArrayList<Integer>(); // Lista de los valores de los nodos que ya fueron visitados
	public int 	numberOfNodes, //numeros de los nodos del grafo
				sumOfNodes,  // Suma del costo total del grafo
				lastSwapped1, //posiciones de los ultimos nodos que fueron intercambiados
				lastSwapped2;

	/*Constructor del grafo
  Se inicializan los valores de los ultimos nodos swapeados en -1 (posiciones imposibles)*/
	public Graph() {
		this.lastSwapped1=-1; 
		this.lastSwapped2=-1;
	}
 /*Funcion para crear nodos*/
	public void createNodes() {
		for (int i=0;i<numberOfNodes;i++) { //Por cada numero de nodos que contiene el grafo
			Node aNode=new Node(i); //Se crea un nodo
			nodesList.add(i, aNode);; //se agrega a la lista de nodos
		}
	}
	/*Funcion para agregar coneciones entre los nodos
  Input: *int number 1 que es la posicion(empezando de 1 en vez del 0) del nodo source
         *int number2 que es (empezando de 1 en vez del 0) del nodo destination
  */
	public void addConnections(int number1, int number2) {
		Node a=nodesList.get(number1-1); //se consigue el nodo source
		Node b=nodesList.get(number2-1); // se consigue el nodo destino
		a.newNeighbour(b); // se agrega el nodo b en la lista de neigboars en a
		b.newNeighbour(a); /// y se hace lo mismo para el nodo b
		sumOfNodes+=Math.abs(number1-number2); //se actualiza el atributo de la suma total del grafo
	}
  
  /*Funcion para regresar el grafo en formato string*/
	public String toString() {
		StringBuilder builder=new StringBuilder();
		for (Node a: nodesList) {
			builder.append(a);
			builder.append("\n"); 
		}
		return builder.toString();
	}

	/*Funcion para cambair la posicion de 2 nodos entre ellos
  Input: Nodos que se quiera hacer el swap  */
	public void swapNodes(Node A, Node B) {
		if (!A.equals(B)) { //mientras los nodos sean diferentes
			for (int i=0;i<numberOfNodes;i++) { //se ente la lista de los nodos
				Node currentNode=nodesList.get(i); 
				if (currentNode.neighbourNodes.contains(A) && currentNode.neighbourNodes.contains(B)) {
            //mientras el nodo actual tenga como vecino al nodo a y b
          // help borre algo???
				}else if(currentNode.neighbourNodes.contains(A)) { //si el nodo a es vecino 
					currentNode.neighbourNodes.remove(A); // se elimina el nodo a de los vecinos 
					currentNode.newNeighbour(B); // y se agrega el nodo b de los vecinos 
				}else if(currentNode.neighbourNodes.contains(B)) { //si es el nodo b el vecino 
					currentNode.neighbourNodes.remove(B); // se elimina el nodo b de lso vecinos 
					currentNode.newNeighbour(A); // y se agrega el nodo a como vecinos
				}
			}
			List<Node> tmpNodes1=new ArrayList<Node>(); // se crea una lista para para guardar temporalmente los vecinos de los nodos a
			tmpNodes1.addAll(A.neighbourNodes); // se agrega los nodos vecinos del nodo a
			A.neighbourNodes.clear(); // y se elimina todos los nodos vecinos de a
			List<Node> tmpNodes2=new ArrayList<Node>(); // se crea una lista para para guardar temporalmente los vecinos de los nodos b
			tmpNodes2.addAll(B.neighbourNodes);// se agrega los nodos vecinos del nodo b
			B.neighbourNodes.clear(); // y se elimina todos los nodos vecinos de b
			
			B.neighbourNodes=tmpNodes1; // se actualiza la lista de vecinos del nodo b con los nodos vecinos que eran de a
			A.neighbourNodes=tmpNodes2; // y se hace lo mismo para la lista de a
			
		}
		
	}
	/*Funcion para calcular el costo del grafo total
  Output:int del costo total
  */
	public int getEnergySupport() {
		int sum=0; //variable para sumar el costo
		for (Node A:this.nodesList) { //iterar por nodos sources
			for (Node B: A.neighbourNodes) { //iterar por nodos destino
				if(A.label<B.label) { //Si el nodo sorce es menor entonces se realiza la formula 
					sum+=Math.abs(A.label-B.label); //fomula para sacar los valores individuales entre 2 nodos
				}
			}
		}
		return sum;// se regresa la suma
	}
/*Funcion para calcular el costo del nodo 
	Input: *Nodo A el nodo que se quiere sacar el costo
  			 *Mapa visited que son los nodos que ya fueron visitados
  Output:int suma que es el costo total
  */

	public double neighbours(int random) {//Función que consigue un vecino del grafo actual
		
		Node A=this.nodesList.get(random);//Se toma un nodo al azar
		int mean=0;												//Y se busca conseguir el valor de la media en su lista de vecinos
		
		if(!A.neighbourNodes.isEmpty()) {//Se hace un sorting de su lista de vecinos, según su etiqueta
			A.sorting();
			
			if (A.neighbourNodes.size()==1) {//Si la lista es de tamaño uno
				mean=A.neighbourNodes.get(0).label;//La media es el unico valor disponible
			}
			else if(A.neighbourNodes.size()%2==0) {//Si la lista es de tamaño par se consiguen los 2 nodos de la mitad y se saca un promedio
				mean= ((A.neighbourNodes.get((A.neighbourNodes.size()/2)-1).label) + (A.neighbourNodes.get(A.neighbourNodes.size()/2).label)   )/2;
			}
			else {//Si la lista es de tamaño impar se consigue el dato del medio
				mean=A.neighbourNodes.get(A.neighbourNodes.size()/2-1).label;
			}

			this.swapNodes(this.nodesList.get(random), this.nodesList.get(mean));//Se hace un swap entre el valor de la media y el primer nodo
			
			this.lastSwapped1=random;//Se guardan ambos nodos que fueron cambiados
			this.lastSwapped2=mean;
		}else {//Si la lista estaba vacía
			lastSwapped1=-1;//No se hizo un swap, por lo tanto se guarda un valor imposible
			lastSwapped2=-1;//Para saber que no hubo un swap
		}
		
		return this.getEnergySupport();//Regresa la energia del grafo vecino
	}
	
	public void revertNeighbours() {//Funcion que sirve para retornar el vecino a su estado original
		if (lastSwapped1!=-1 && lastSwapped2!=-1) {//Si hubo un swap anterior
			this.swapNodes(this.nodesList.get(this.lastSwapped1), this.nodesList.get(this.lastSwapped2));//Se manda a llamar a la funcion de swap
		}
		
	}
	
	
	public int simulatedAnnealing(){//Función principal del simulated annealing
		Random ran=new Random();//Se declara un random para la función de aceptación
		
		double t0=100;//Se declara una temperatura inicial
		double bestEnergy=this.getEnergySupport();//Se consigue la energia actual
		double alpha=.95;//Se declara un alpha para el cambio de temperatura
		double newEnergy;//Se declara la variable de la nueva energia
		double tf=0.5;//Se declara la temperatura final
		
		while(t0>tf) {//Mientras que la temperatura inicial sea mayor que la final
			int l=100;//Se declara el numero de loops en dicha temperatura
			while(l>0) {//Mientras que el numero de loops no se exceda
				l--;//Se reduce en uno la variable de loops
				
				newEnergy=this.neighbours(ran.nextInt(this.numberOfNodes)); //Se consigue la energia de un vecino del grafo
				if (this.acceptanceProbability(bestEnergy, newEnergy, t0)>ran.nextDouble()) {//Si la función de aceptacion es mayor que el random
					bestEnergy=newEnergy;//La nueva energia se convierte en la mejor y el vecino se queda así
				}else {//De otra manera
					this.revertNeighbours();//Se revierte el swap
				}
			}

			t0=t0*alpha;//Se reduce la temperatura usando el aplha
			
		}

		
		return (int) bestEnergy;//Se regresa la energia final
	}
	

	public double acceptanceProbability(double bestEnergy, double newEnergy, double temperature) {//La función de aceptación

		if (newEnergy<bestEnergy) {//Si la nueva energia es menor que la mejor energia actual
			return 1;//Regresa un 1 y el cambio se aprueba
		}
		else if(newEnergy>bestEnergy){//Si la nueva energia es mayor
			return Math.exp(-(newEnergy-bestEnergy)/temperature);//Se saca un exponencial del cambio de energia entre temperatura y se regresa para su evaluación
		}
		else {//Si la nueva energia y la vieja son iguales
			return .1;//Se regresa una probabilidad de .1
		}
	}





	public static void main(String[] args) {

		Graph g=new Graph();

		if(args.length == 0) {									//Lector de archivos por cmd
        System.out.println("File name not specified.");
        System.exit(1);
		}

		try {
			File file = new File(args[0]);
			input = new Scanner(file);
		} catch (IOException ioException) {
			System.err.println("Cannot open file.");
			System.exit(1);
		}

		String[] word= input.nextLine().split(" ");
		g.numberOfNodes=Integer.parseInt(word[2]);

		g.createNodes();

		while(input.hasNext()) {
			word= input.nextLine().split(" ");
			g.addConnections(Integer.parseInt(word[1]), Integer.parseInt(word[2]));
		}

		System.out.println(g);
		
		int oldEnergy=g.getEnergySupport();
		
		int newEnergy=g.simulatedAnnealing();
		
		System.out.println(g);
		System.out.println(newEnergy);
		System.out.println(oldEnergy);




	}

}




