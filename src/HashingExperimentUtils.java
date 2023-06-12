import java.util.ArrayList;
import java.util.Collections; // can be useful
import java.util.Iterator;
import java.util.List;

public class HashingExperimentUtils {
    final private static int k = 16;
    
    public static Pair<Double, Double> measureOperationsChained(double maxLoadFactor) {
        HashTable<Integer, Integer> ht = new ChainedHashTable<>(new ModularHash(), k, maxLoadFactor);
        return measureOperations(ht, maxLoadFactor);
    }
    
    public static Pair<Double, Double> measureOperationsProbing(double maxLoadFactor) {
    	HashTable<Integer, Integer> ht = new ProbingHashTable<>(new ModularHash(), k, maxLoadFactor);
    	return measureOperations(ht, maxLoadFactor);    
    }
    
    public static Pair<Double, Double> measureOperations(HashTable< Integer, Integer> ht,  double maxLoadFactor) {
		List<Integer> vals = new ArrayList<Integer>();
		for (int i = 0; i < maxLoadFactor*ht.capacity() - 1; i++) {
			vals.add(2*i);
		}
		Collections.shuffle(vals);
		double sumTimeInsert = 0;
		double sumTimeSearch = 0;
		double startTime, endTime;
		for (Iterator<Integer> iterator = vals.iterator(); iterator.hasNext();) {
			Integer integer = (Integer) iterator.next();
			startTime = System.nanoTime();
			ht.insert(integer,integer);
			endTime = System.nanoTime();
			sumTimeInsert += endTime - startTime;
		}
		Collections.shuffle(vals);
		List<Integer> searchVals = new ArrayList<Integer>();
		for (int i = 0; i < vals.size()/2; i++) {
			searchVals.add(vals.get(i));
			searchVals.add(2*i + 1);
		}
		Collections.shuffle(searchVals);
		for (Iterator<Integer> iterator = searchVals.iterator(); iterator.hasNext();) {
			Integer integer = (Integer) iterator.next();
			startTime = System.nanoTime();
			ht.search(integer);
			endTime = System.nanoTime();
			sumTimeSearch += endTime - startTime;
		}		
		return new Pair<Double, Double>(sumTimeInsert/vals.size(), sumTimeSearch/vals.size());
    }


    public static Pair<Double, Double> measureLongOperations() {
        HashTable<Long, Long> ht = new ChainedHashTable<Long, Long>(new MultiplicativeShiftingHash(), k, 1);
        HashingUtils utils = new HashingUtils();
        Long[] vals = utils.genUniqueLong(ht.capacity()*2);
        return measureOperations(vals, ht);
    }

    public static Pair<Double, Double> measureStringOperations() {
        HashTable<String, String> ht = new ChainedHashTable<>(new StringHash(), k, 1);
        HashingUtils utils = new HashingUtils();
        return measureOperations(utils.genUniqueStringsArray(ht.capacity()*2, 10, 20), ht);    
    }
    
    public static <K> Pair<Double, Double> measureOperations(K[]vals, HashTable< K, K> ht) {
		double sumTimeInsert = 0;
		double sumTimeSearch = 0;
		double startTime, endTime;
		for (int i = 0; i < vals.length/2; i++) {
			startTime = System.nanoTime();
			ht.insert(vals[i],vals[i]);
			endTime = System.nanoTime();
			sumTimeInsert += endTime - startTime;
		}
		List<Object> searchVals = new ArrayList<>();
		for (int i = 0; i < vals.length/4; i++) {
			searchVals.add(vals[i]);
			searchVals.add(vals[i + vals.length/2]);
		}
		Collections.shuffle(searchVals);
		for (@SuppressWarnings("unchecked")
		Iterator<K> iterator = (Iterator<K>) searchVals.iterator(); iterator.hasNext();) {
			K k = iterator.next();
			startTime = System.nanoTime();
			ht.search(k);
			endTime = System.nanoTime();
			sumTimeSearch += endTime - startTime;
		}		
		return new Pair<Double, Double>(sumTimeInsert/(vals.length/2), sumTimeSearch/(vals.length/2));   
    }
    
    
    

    public static void main(String[] args) {
    }
      
}
