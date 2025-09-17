package ee;

public class GHASH {

    static BetterBitSet hash(BetterBitSet X, BetterBitSet H){
        System.out.println(X.length());
        assert X.length()%128==0;
        assert H.length()==128;
        System.out.println(H.length());
        int m = X.length()/128;
        BetterBitSet Y = new BetterBitSet(128);
        for (int i = 0; i < m; i++) {
            Y.xor(X.get(128*i, (128*(i+1))));//todo check parameters passed in, X isnt a multiple of 128
            //for first block just X[0..128] * H
            //H isnt 128
            Y = Utils.multiplicationBlock(H, Y);
        }
        return Y;
    }
}
