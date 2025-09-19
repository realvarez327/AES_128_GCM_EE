package ee;

import static ee.Utils.Nb;

public class Word {
    // a word is a "column" in the state array.
    int[] word;

    Word(){
        this.word = new int[]{0,0,0,0};
    }

    Word(int item0, int item1, int item2, int item3){
        this.word = new int[]{item0,item1,item2,item3};
    }

    public int getItemAtIndex(int index){
        if(index<4 && index>-1){
            return this.word[index];
        }else {
            throw new IndexOutOfBoundsException("You tried to use an item out of length 4. Index given : "+index);

        }
    }

    public void setItemAtIndex(int value, int index){
        if (index<4 && index>-1){
            this.word[index] = value;
        }else{
            throw new IndexOutOfBoundsException("You tried to use an item out of length 4. Index given : "+index);
        }
    }

    public Word copy(){
        return new Word(this.word[0], this.word[1], this.word[2], this.word[3]);
    }

    public void xor(int[] toXorWith){
        this.word[0] = this.word[0]^toXorWith[0];
        this.word[1] = this.word[1]^toXorWith[1];
        this.word[2] = this.word[2]^toXorWith[2];
        this.word[3] = this.word[3]^toXorWith[3];

    }

    public void xor(Word toXorWith){
        this.word[0] = this.word[0]^toXorWith.word[0];
        this.word[1] = this.word[1]^toXorWith.word[1];
        this.word[2] = this.word[2]^toXorWith.word[2];
        this.word[3] = this.word[3]^toXorWith.word[3];

    }

    public static Word xorToReturn(Word toXorWithOne, Word toXorWithTwo){
        Word toReturn = new Word(
        toXorWithOne.word[0]^toXorWithTwo.word[0],
        toXorWithOne.word[1]^toXorWithTwo.word[1],
        toXorWithOne.word[2]^toXorWithTwo.word[2],
        toXorWithOne.word[3]^toXorWithTwo.word[3]
        ) ;
        return toReturn;
    }

    public Word rotWordWordClass() {
        Word toReturn = new Word();
        int holder = this.word[0];
        for (int i = 1; i < Nb; i++) {
            toReturn.word[i-1] = this.word[i];
        }
        toReturn.word[3] = holder;
        return toReturn;
    }
}
