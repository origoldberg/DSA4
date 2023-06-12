import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;

public class ProbingHashTable<K, V> implements HashTable<K, V> {
    final static int DEFAULT_INIT_CAPACITY = 4;
    final static double DEFAULT_MAX_LOAD_FACTOR = 0.75;
    final private HashFactory<K> hashFactory;
    final private double maxLoadFactor;
    private int capacity;
    private HashFunctor<K> hashFunc;
    private Item<K,V>[] table;
    private int k;
    private int size;
    private Item<K,V> D;


    /*
     * You should add additional private members as needed.
     */

    public ProbingHashTable(HashFactory<K> hashFactory) {
        this(hashFactory, DEFAULT_INIT_CAPACITY, DEFAULT_MAX_LOAD_FACTOR);
    }

    @SuppressWarnings("unchecked")
	public ProbingHashTable(HashFactory<K> hashFactory, int k, double maxLoadFactor) {
        this.hashFactory = hashFactory;
        this.maxLoadFactor = maxLoadFactor;
        this.capacity = 1 << k;
        this.hashFunc = hashFactory.pickHash(k);
        this.table = new Item[capacity];
        this.k = k;
        this.size = 0;
        this.D = new Item<K,V>(null,null);
    }

    public V search(K key) {
        int index = hashFunc.hash(key);
        while(true) {
        	if(table[index] == null)
        		return null;
        	if(table[index].k.equals(key))
        		return table[index].v;
        	index = HashingUtils.mod(index+1, capacity);
        }
    }

    public void insert(K key, V value) {
    	if(size+1 >= maxLoadFactor*capacity)
    		rehash();
        int index = hashFunc.hash(key);
        while(true) {
        	if(table[index] == null) {
        		table[index] = new Item<K, V>(key, value);
        		break;
        	}
        	else
        		index = HashingUtils.mod(index+1, capacity);
        }
    }
    
    public void rehash() {
    	ProbingHashTable<K,V> newHash = new ProbingHashTable<K, V>(hashFactory, k+1, maxLoadFactor);
        for (int i = 0; i < table.length; i++) {
        	if(table[i] != null && table[i] != D)
        		newHash.insert(table[i].k, table[i].v);
		}
        capacity *= 2;
        k++;
        table = newHash.table;
        hashFunc = newHash.hashFunc;

    }

    public boolean delete(K key) {
        int index = hashFunc.hash(key);
        while(true) {
        	if(table[index] == null)
        		return false;
        	if(table[index].k.equals(key)) {
        		table[index] = D;
        		size--;
        	}
        	index = HashingUtils.mod(index+1, capacity);
        }
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
