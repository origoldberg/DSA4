import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class SkipListExperimentUtils {
	public static double measureLevels(double p, int x) {
		IndexableSkipList sl = new IndexableSkipList(p);
		double sum = 0;
		for (int i = 0; i < x; i++) {
			sum += sl.generateHeight();
		}
		return sum/x + 1;
	}

	/*
	 * The experiment should be performed according to these steps:
	 * 1. Create the empty Data-Structure.
	 * 2. Generate a randomly ordered list (or array) of items to insert.
	 *
	 * 3. Save the start time of the experiment (notice that you should not
	 *    include the previous steps in the time measurement of this experiment).
	 * 4. Perform the insertions according to the list/array from item 2.
	 * 5. Save the end time of the experiment.
	 *
	 * 6. Return the DS and the difference between the times from 3 and 5.
	 */
	public static Pair<AbstractSkipList, Double> measureInsertions(double p, int size) {
		IndexableSkipList sl = new IndexableSkipList(p);
		List<Integer> vals = new ArrayList<Integer>();
		for (int i = 0; i < size + 1; i++) {
			vals.add(2*i);
		}
		Collections.shuffle(vals);
		double startTime, endTime;
		double sumTime = 0;
		for (Iterator<Integer> iterator = vals.iterator(); iterator.hasNext();) {
			Integer integer = (Integer) iterator.next();
			startTime = System.nanoTime();
			sl.insert(integer);
			endTime = System.nanoTime();
			sumTime += endTime - startTime;
		}
		return new Pair<AbstractSkipList, Double>(sl, sumTime/vals.size());
	}

	public static double measureSearch(AbstractSkipList skipList, int size) {
		List<Integer> vals = new ArrayList<Integer>();
		for (int i = 0; i < 2*size + 1; i++) {
			vals.add(i);
		}	
		Collections.shuffle(vals);
		double startTime, endTime;
		double sumTime = 0;
		for (Iterator<Integer> iterator = vals.iterator(); iterator.hasNext();) {
			Integer integer = (Integer) iterator.next();
			startTime = System.nanoTime();
			skipList.search(integer);
			endTime = System.nanoTime();
			sumTime += endTime - startTime;
		}
		return sumTime/vals.size();

	}
	public static double measureDeletions(AbstractSkipList skipList, int size) {
		List<Integer> vals = new ArrayList<Integer>();
		for (int i = 0; i < size + 1; i++) {
			vals.add(2*i);
		}
		Collections.shuffle(vals);
		double startTime, endTime;
		double sumTime = 0;	
		for (Iterator<Integer> iterator = vals.iterator(); iterator.hasNext();) {
			Integer integer = (Integer) iterator.next();
			startTime = System.nanoTime();
			skipList.delete(skipList.find(integer));
			endTime = System.nanoTime();
			sumTime += endTime - startTime;
		}
		return sumTime/vals.size();
	}

	public static void main(String[] args) {
		System.out.println("experimentA: \n " );
		experimentA();
		System.out.println("experimentB: \n " );
		experimentB();
	}
	
	 public static void experimentA() {
		 double [] probs = new double[]{0.33, 0.5, 0.75, 0.9};
		 int [] x = new int[] {10,100,1000,10000};
		 double expected;
		 double sumDelta;
		 double avgLevel;
		 for (int k = 0; k < probs.length; k++) {
			 System.out.println("**********************p=" + probs[k] + "**********************");
			 expected = 1/probs[k];
			 System.out.println("E=" + expected);
			 for (int i = 0; i < x.length; i++) {
				 System.out.println("********x=" + x[i] + ":******** \n");
				 sumDelta = 0;
				 for (int j = 0; j < 5; j++) {
					 avgLevel = measureLevels(probs[k], x[i]);
					 sumDelta += Math.abs(avgLevel - expected);
					 System.out.println("l" + (j+1) + "=" + avgLevel);
				 }
				 System.out.println("Average delta=" + 0.2*sumDelta);
			 }
		 }
	 }
	 
	 public static void experimentB() {
		 double[] probs = new double[] {0.33, 0.5, 0.75, 0.9};
		 int[] sizes = new int[] {1000, 2500, 5000, 10000, 15000, 20000, 50000};
		 Pair<AbstractSkipList, Double> pair;
		 double sumTimeInsert = 0;
		 double sumTimeSearch = 0;
		 double sumTimeDelete = 0;
		 for(double p: probs) {
			 System.out.println("**********************p=" + p + "**********************");			 
			 for(int x: sizes) { 
				 System.out.println("********x=" + x + ":******** \n");
				 for (int i = 0; i < 30; i++) {
					 pair= measureInsertions(p, x);
					 sumTimeInsert += pair.second();
					 sumTimeSearch += measureSearch(pair.first(), x);
					 sumTimeDelete += measureDeletions(pair.first(), x);

				}
				 System.out.println("Insertions:" + sumTimeInsert/30);
				 System.out.println("Search:" + sumTimeSearch/30);
				 System.out.println("Deletion:" + sumTimeDelete/30);
				 sumTimeInsert = 0;
				 sumTimeSearch = 0;
				 sumTimeDelete = 0;
			 }
		 }
	 }
	 
}
