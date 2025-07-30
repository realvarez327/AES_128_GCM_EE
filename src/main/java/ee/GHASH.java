package ee;

import java.util.BitSet;

public class GHASH {
    static BitSet zeroBlock = new BitSet(128);
    static BitSet hash(BitSet X, BitSet H){
        int m = X.length()/128;
        BitSet Y = zeroBlock;
        for (int i = 1; i <= m; i++) {
            Y.xor(X.get((i-1)*128, i*128));
            //todo make multiplication accept bitset
            Y = Utils.multiplicationB(Y, H);
        }
        return Y;

    }
}
