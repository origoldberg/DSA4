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

    @Override
    public int generateHeight() {
    	int res = 0;
    	while(rand.nextDouble() < probability)
    		res++;
    	return res;
    }

    public int rank(int val) {
        throw new UnsupportedOperationException("Replace this by your implementation");
    }

    public int select(int index) {
        throw new UnsupportedOperationException("Replace this by your implementation");
    }
}
