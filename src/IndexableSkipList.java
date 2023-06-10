import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class IndexableSkipList extends AbstractSkipList {
    final protected double probability;
    Random rand;
    public IndexableSkipList(double probability) {
        super();
        this.probability = probability;
        rand = new Random();
//        rand.setSeed(15);
    }

    @Override
    public Node find(int val) {
    	Node p = head;
    	for (int i = head.height(); i >= 0; i--) {
			while(p.getNext(i) != null && p.getNext(i).key() <= val)
				p = p.getNext(i);
		}
    	return p;
    }
    

    private List<Node> getAncestors(Node node) {
    	List<Node> ancestors = new ArrayList<>();
    	
    	Node a = node;
    	for (int level = 0; level <= this.head.height; level++) {
    		ancestors.add(a);
    		while (a != null && a.height() <= level) 
    			a = a.getPrev(level);
    	}
    	
    	return ancestors;
    }

    private void updateSizes(List<Node> ancestors) {
		for (int level = 0; level < ancestors.size(); level++) {
			Node ancestor = ancestors.get(level);
		  	ancestor.size.set(level, getSize(ancestor, level));
		}
    }
    
    @Override
    public Node insert(int key) {
    	Node newNode = super.insert(key);
        
    	List<Node> oldAncestors1 = getAncestors(newNode);
        updateSizes(oldAncestors1);
        
        List<Node> oldAncestors2 = getAncestors(newNode.getPrev(0));
        updateSizes(oldAncestors2);

		return newNode;
    }
    
    @Override
    public boolean delete(Node node) {
        boolean ret = super.delete(node);

    	List<Node> oldAncestors1 = getAncestors(node.getNext(0));
        updateSizes(oldAncestors1);

        List<Node> oldAncestors2 = getAncestors(node.getPrev(0));
        updateSizes(oldAncestors2);
        
        return ret;    	
    }


    @Override
    public int generateHeight() {
    	int res = 0;
    	while(rand.nextDouble() < probability)
    		res++;
    	return res;
    }

    public int rank(int val) {
    	int r = 0;
    	Node p = head;
    	for (int i = head.height(); i >= 0; i--) {
			while(p.getNext(i) != null && p.getNext(i).key() <= val) {
				r += p.size.get(i);
				p = p.getNext(i);
			}
		}
    	return r;
    }

    public int select(int index) {
    	int r = 0;
    	Node p = head;
    	for (int i = head.height(); i >= 0; i--) {
			while (r + p.size.get(i) < index) {
				r += p.size.get(i);
				p = p.getNext(i);
			}
		}
    	
    	return p.key;
    }
    

    private void levelToString2(StringBuilder s, int level) {
        s.append("H[");
        s.append(head.size.get(level));
        s.append("]   ");
        		
        Node curr = head.getNext(level);

        while (curr != tail) {
            s.append(curr.key);
            s.append("[");
            s.append(curr.size.get(level));
            s.append("]   ");
            
            curr = curr.getNext(level);
        }

        s.append("T\n");
    }

    public String toString() {
        StringBuilder str = new StringBuilder();

        for (int level = head.height(); level >= 0; --level) {
            levelToString2(str, level);
        }

        return str.toString();
    }

}
