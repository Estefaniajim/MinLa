import java.util.Comparator;
 
public class LabelSorter implements Comparator<Node> 
{

	@Override
	public int compare(Node o1, Node o2) {
		// TODO Auto-generated method stub
		return o1.getLabel()-o2.getLabel();
	}
}
