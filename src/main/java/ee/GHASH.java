package ee;

public class GHASH {
    private final static BetterBitSet zeroBlock = new BetterBitSet(128);
    static BetterBitSet hash(BetterBitSet X, BetterBitSet H){
        int m = X.size()/128;
        BetterBitSet Y = zeroBlock;
        for (int i = 0; i <= m; i++) {
            Y.xor(X.get(128*i, (128*(i+1)-1)));
            Y = Utils.multiplicationBlock(H, Y);
        }
        return Y;
    }
}
