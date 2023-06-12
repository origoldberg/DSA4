import java.util.Random;

public class MultiplicativeShiftingHash implements HashFactory<Long> {
    Random rand;
    HashingUtils hu;
	public MultiplicativeShiftingHash() {
        rand = new Random();
        hu = new HashingUtils();
    }

    @Override
    public HashFunctor<Long> pickHash(int k) {
        long a = hu.genLong(2, Long.MAX_VALUE);
        return new Functor(a, k);
    }

    public class Functor implements HashFunctor<Long> {
        final public static long WORD_SIZE = 64;
        final private long a;
        final private long k;

        public Functor(long a, long k) {
			super();
			this.a = a;
			this.k = k;
		}

		@Override
        public int hash(Long key) {
            return (int)(a*key)>>>(64-k);
        }

        public long a() {
            return a;
        }

        public long k() {
            return k;
        }
    }
}
