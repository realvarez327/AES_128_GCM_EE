package ee;

import java.util.BitSet;

public class GHASH {
    private final static BetterBitSet zeroBlock = new BetterBitSet();
    static BetterBitSet hash(BitSet X, BitSet H){
        int m = X.size()/128;
        BetterBitSet Y = zeroBlock;
        for (int i = 0; i <= m; i++) {
            Y.xor(X.get(128*i, (128*(i+1)-1)));
            Y = (BetterBitSet) Utils.multiplicationBlock(H, Y);
        }
        return Y;
    }
}
