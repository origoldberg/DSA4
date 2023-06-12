import java.util.Random;

public class ModularHash implements HashFactory<Integer> {
	Random rand;
	HashingUtils hu;
	public ModularHash() {
        rand = new Random();
        hu = new HashingUtils();
    }

    @Override
    public HashFunctor<Integer> pickHash(int k) {
    	int a = rand.nextInt(Integer.MAX_VALUE - 1) + 1;
    	int b = rand.nextInt(Integer.MAX_VALUE - 1) + 1;
    	long p;
    	while(true){
    		p = hu.genLong((long)Integer.MAX_VALUE + 1, Long.MAX_VALUE);
    		if (hu.runMillerRabinTest(p, 64))
    			break;
    	}
    	int m = 1;
    	for (int i = 0; i < k; i++) {
			m = 2*m;
		}
    	return new Functor(a,b,p,m);
    }

    public class Functor implements HashFunctor<Integer> {
        final private int a;
        final private int b;
        final private long p;
        final private int m;
        public Functor(int a, int b, long p, int m) {
			super();
			this.a = a;
			this.b = b;
			this.p = p;
			this.m = m;
		}

		@Override
        public int hash(Integer key) {
			return (int)HashingUtils.mod(HashingUtils.mod(((long)a)*key + b, p), (long)m);
        }

        public int a() {
            return a;
        }

        public int b() {
            return b;
        }

        public long p() {
            return p;
        }

        public int m() {
            return m;
        }
    }
}
