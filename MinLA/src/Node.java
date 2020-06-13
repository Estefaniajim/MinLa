import java.util.ArrayList;
import java.util.List;

public class Node {
	public Integer label;
	public List<Node> neighbourNodes;
	
	public Node(int lable, Node neighbour) {
		this.label=lable;
		this.neighbourNodes.add(neighbour);
	}
	
	public Node(int lable) {
		this.label=lable;
		this.neighbourNodes=new ArrayList<Node>();
	}
	
	
	public void newNeighbour(Node a) {
		this.neighbourNodes.add(a);
	}
	
	public int getLabel() {
		return this.label;
	}
	
	public void sorting() {
		this.neighbourNodes.sort(new LabelSorter());
	}
	
	public String toString() {
		String finalString=this.label+"-Neighbours: ";
		for (Node a:neighbourNodes) {
			finalString+=" "+a.label;
		}
		return finalString;
	}
	
}
