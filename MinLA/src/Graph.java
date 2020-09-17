import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Graph {
  /* MinLa: To get the cost between nodes or the total cost of the graph
   the formula is used:: abs (sourceNode-destinationNode) */
	private static Scanner input;

	public List<Node> nodesList=new ArrayList<Node>(); //List of nodes in the graph
	public List<Integer> numbersAlreadyIn=new ArrayList<Integer>(); // List of node values that have already been visited
	public int 	numberOfNodes, //graph node numbers
				sumOfNodes,  // Sum of the total cost of the graph
				lastSwapped1, //positions of the last nodes that were exchanged
				lastSwapped2;

	/*Graph constructor
   	The values of the last swapped nodes are initialized at -1 (impossible positions)*/
	public Graph() {
		this.lastSwapped1=-1; 
		this.lastSwapped2=-1;
	}
	
	 /*Function to create nodes*/
	public void createNodes() {
		for (int i=0;i<numberOfNodes;i++) { //For each number of nodes that the graph contains
			Node aNode=new Node(i); //A node is created
			nodesList.add(i, aNode);; //is added to the list of nodes
		}
	}
	
	/*Function to add connections between nodes
   	Input: * int number 1 which is the position (starting from 1 instead of 0) of the source node
        * int number2 which is (starting from 1 instead of 0) of the destination node*/
	public void addConnections(int number1, int number2) {
		Node a=nodesList.get(number1-1); //source node is achieved
		Node b=nodesList.get(number2-1); // the destination node is achieved
		a.newNeighbour(b); // node b is added to the list of neighbors in a
		b.newNeighbour(a); ///and the same is done for node b
		sumOfNodes+=Math.abs(number1-number2); //the attribute of the total sum of the graph is updated

	}
  
  	/*Function to return the graph in string format*/
	public String toString() {
		StringBuilder builder=new StringBuilder();
		for (Node a: nodesList) {
			builder.append(a);
			builder.append("\n"); 
		}
		return builder.toString();
	}
	
	/*Function to change the position of 2 nodes between them 
	Input: Nodes that you want to swap  */
	public void swapNodes(Node A, Node B) {
		if (!A.equals(B)) { //as long as the nodes are different
			for (int i=0;i<numberOfNodes;i++) { //enter the list of nodes
				Node currentNode=nodesList.get(i); 
				if(currentNode.neighbourNodes.contains(A)) { //if node a is neighbor
					currentNode.neighbourNodes.remove(A); // node a is removed from neighbors
					currentNode.newNeighbour(B); // and the node b of the neighbors is added
				}else if(currentNode.neighbourNodes.contains(B)) { //if node b is the neighbor
					currentNode.neighbourNodes.remove(B); // node b is removed from the neighbors
					currentNode.newNeighbour(A); // and add the node a as neighbors
				}
			}
			List<Node> tmpNodes1=new ArrayList<Node>(); // a list is created to temporarily save the neighbors of the nodes a
			tmpNodes1.addAll(A.neighbourNodes); // the node's neighboring nodes are added a
			A.neighbourNodes.clear(); //and delete all the neighboring nodes of a
			List<Node> tmpNodes2=new ArrayList<Node>(); // a list is created to temporarily save the neighbors of the nodes b
			tmpNodes2.addAll(B.neighbourNodes);// the neighboring nodes of node b are added
			B.neighbourNodes.clear(); // and remove all neighboring nodes from b
			
			B.neighbourNodes=tmpNodes1; // update the neighbor list of node b with the neighboring nodes that were from a
			A.neighbourNodes=tmpNodes2; //and the same is done for the list of to
		}
	}
	/*Function to calculate the cost of the total graph
   	Output: int of total cost*/
	public int getEnergySupport() {
		int sum=0; //variable to add the cost
		for (Node A:this.nodesList) { //iterate over sources nodes
			for (Node B: A.neighbourNodes) { //iterate over destination nodes
				if(A.label<B.label) { //If the source node is smaller then the formula is performed
					sum+=Math.abs(A.label-B.label); //formula to output individual values between 2 nodes
				}
			}
		}
		return sum;// the sum is returned
	}
	/*Function to calculate the cost of the node
	Input: * Node A the node that you want to get the cost
   	* Map visited which are the nodes that have already been visited
   	Output: int sum which is the total cost*/
	public double neighbours(int random) {//Function that gets a neighbor of the current graph
		
		Node A=this.nodesList.get(random);//A node is taken at random
		int mean=0;	//And you are looking to get the value of the mean in your list of neighbors
		
		if(!A.neighbourNodes.isEmpty()) {//A sorting is done from your list of neighbors, according to their label
			A.sorting();
			if (A.neighbourNodes.size()==1) {//If the list is size one
				mean=A.neighbourNodes.get(0).label;//The mean is the only available value
			}
			else if(A.neighbourNodes.size()%2==0) {//If the list is even in size, the 2 half nodes are obtained and an average is obtained
				mean= ((A.neighbourNodes.get((A.neighbourNodes.size()/2)-1).label) + (A.neighbourNodes.get(A.neighbourNodes.size()/2).label)   )/2;
			}
			else {//If the list is odd-sized, the middle data is obtained
				mean=A.neighbourNodes.get(A.neighbourNodes.size()/2-1).label;
			}
			this.swapNodes(this.nodesList.get(random), this.nodesList.get(mean));//A swap is made between the value of the mean and the first node
			
			this.lastSwapped1=random;//Both nodes that were changed are saved
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

		if(args.length == 0) {									
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
