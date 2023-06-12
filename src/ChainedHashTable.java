import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

public class ChainedHashTable<K, V> implements HashTable<K, V> {
    final static int DEFAULT_INIT_CAPACITY = 4;
    final static double DEFAULT_MAX_LOAD_FACTOR = 2;
    final private HashFactory<K> hashFactory;
    final private double maxLoadFactor;
    private int capacity;
    private HashFunctor<K> hashFunc;
    private LinkedList<Item<K,V>>[] table;
    private int size;
    private int k;


    /*
     * You should add additional private members as needed.
     */

    public ChainedHashTable(HashFactory<K> hashFactory) {
        this(hashFactory, DEFAULT_INIT_CAPACITY, DEFAULT_MAX_LOAD_FACTOR);
    }

    @SuppressWarnings("unchecked")
	public ChainedHashTable(HashFactory<K> hashFactory, int k, double maxLoadFactor) {
        this.hashFactory = hashFactory;
        this.maxLoadFactor = maxLoadFactor;
        this.capacity = 1 << k; 
        this.hashFunc = hashFactory.pickHash(k);
        this.table = new LinkedList[capacity];
        this.size = 0;
        this.k = k;
    }

    public V search(K key) {
        int index = hashFunc.hash(key);
        LinkedList<Item<K,V>> lst = table[index];
        if(lst == null)
        	return null;
        for (Iterator<Item<K, V>> iterator = lst.iterator(); iterator.hasNext();) {
			Item<K, V> item = (Item<K, V>) iterator.next();
			if(item.k.equals(key))
				return item.v;
		}
        return null;
    }

    public void insert(K key, V value) {
    	if(size+1 >= maxLoadFactor*capacity)
    		rehash();
    	int index = hashFunc.hash(key);
        LinkedList<Item<K,V>> lst = table[index];
        if(lst == null)
        	lst = new LinkedList<Item<K,V>>();
        lst.add(new Item<K, V>(key,value));
        size++;
    }
    
    public void rehash(){
    	ChainedHashTable<K, V> newHash = new ChainedHashTable<K,V>(hashFactory, k+1, maxLoadFactor);
        for (int i = 0; i < table.length; i++) {
			LinkedList<Item<K,V>> lst = table[i];
			if(lst != null) {
				for (Item<K, V> item : lst) {
					newHash.insert(item.k, item.v);
				}				
			}
		}
        capacity *= 2;
        k++;
        table = newHash.table;
        hashFunc = newHash.hashFunc;
    }

    public boolean delete(K key) {
        int index = hashFunc.hash(key);
        LinkedList<Item<K,V>> lst = table[index];
        if(lst == null)
        	return false;
        for (Iterator<Item<K, V>> iterator = lst.iterator(); iterator.hasNext();) {
			Item<K, V> item = (Item<K, V>) iterator.next();
			if(item.k.equals(key)) {
				size--;
				return lst.remove(item);
			}
		}
        return false;
    }

    public HashFunctor<K> getHashFunc() {
        return hashFunc;
    }

    public int capacity() { return capacity; }
    
    public static class Item<K,V>{
    	K k;
    	V v;
    	
		public Item(K k, V v) {
			super();
			this.k = k;
			this.v = v;
		}
    	
    }
}
