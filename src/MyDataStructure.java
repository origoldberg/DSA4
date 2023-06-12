import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;


public class MyDataStructure {
    /*
     * You may add any members that you wish to add.
     * Remember that all the data-structures you use must be YOUR implementations,
     * except for the List and its implementation for the operation Range(low, high).
     */
	
	AbstractSkipList list;
	HashTable<Integer, AbstractSkipList.Node> table;
	
    /***
     * This function is the Init function described in Part 4.
     *
     * @param N The maximal number of items expected in the DS.
     */
    public MyDataStructure(int N) {
    	list = new IndexableSkipList(0.5);
    	table = new ChainedHashTable<>(new ModularHash(), (int)Math.log(N)+1, 1);
    }

    /*
     * In the following functions,
     * you should REMOVE the place-holder return statements.
     */
    public boolean insert(int value) {
        if(!contains(value)) {
        	table.insert(value, new AbstractSkipList.Node(value));
        	list.insert(value);
        	return true;
        }
        return false;
    }

    public boolean delete(int value) {
        if(contains(value)) {
        	table.delete(value);
        	list.delete(list.search(value));
        	return true;
        }
        return false;
    }

    public boolean contains(int value) {
        if(table.search(value) == null)
        	return false;
        return true;
        
    }

    public int rank(int value) {
        return ((IndexableSkipList)list).rank(value);
    }

    public int select(int index) {
        return ((IndexableSkipList)list).select(index);
    }

	public List<Integer> range(int low, int high) {
    	AbstractSkipList.Node item = table.search(low);
        if(item == null)
        	return null;
        List<Integer> res = new LinkedList<Integer>();
        while(item.key() <= high) {
        	res.add(item.key());
        	item = item.getNext(0);
        }
        return res;
    }
}
