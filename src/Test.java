import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		AbstractSkipList sl = new IndexableSkipList(0.5);
		
		List<Integer> lst = new ArrayList<Integer>();
		for (int i = 0; i <= 10; i++) {
			lst.add(2*i);
		}
		
		Collections.shuffle(lst);
		
		for (Iterator iterator = lst.iterator(); iterator.hasNext();) {
			Integer integer = (Integer) iterator.next();
			sl.insert(integer);
		}
		System.out.println(sl.toString());
	}

}
