import java.util.Iterator;
import java.util.Random;

public class StringHash implements HashFactory<String> {
	Random rand;
	HashingUtils hu;
    public StringHash() {
        rand = new Random();
        hu = new HashingUtils();
    }

    @Override
    public HashFunctor<String> pickHash(int k) {
    	int q;
    	while(true){
            q = rand.nextInt(Integer.MAX_VALUE/2) + Integer.MAX_VALUE/2 + 1;
    		if (hu.runMillerRabinTest(q, 64))
    			break;
    	}
        int c = rand.nextInt(q - 2) + 2;
        ModularHash mh = new ModularHash();
        return new Functor(mh.pickHash(k) , c, q);
    }

    public class Functor implements HashFunctor<String> {
        final private HashFunctor<Integer> carterWegmanHash;
        final private int c;
        final private int q;

        public Functor(HashFunctor<Integer> carterWegmanHash, int c, int q) {
			super();
			this.carterWegmanHash = carterWegmanHash;
			this.c = c;
			this.q = q;
		}

		@Override
        public int hash(String key) {
			int sum = 0;
			int cc = 1;
            for (int i = key.length() - 1; i >= 0; i--) {
				sum += key.charAt(i)*(HashingUtils.mod(cc, q));
				cc = HashingUtils.mod(cc * c, q);
			}
            return carterWegmanHash.hash(sum);
        }

        public int c() {
            return c;
        }

        public int q() {
            return q;
        }

        public HashFunctor carterWegmanHash() {
            return carterWegmanHash;
        }
    }
}
