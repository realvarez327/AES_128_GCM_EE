package ee;

import java.util.BitSet;

public class GHASH {
    private final static BitSet zeroBlock = new BitSet(128);
    static BitSet hash(BitSet X, BitSet H){
        int m = X.size()/128;
        BitSet Y = zeroBlock;
        for (int i = 0; i <= m; i++) {
            Y.xor(X.get(128*i, (128*(i+1)-1)));
            Y = Utils.multiplicationBlock(H, Y);
        }
        return Y;
    }
}
