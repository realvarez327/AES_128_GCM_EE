package ee;

public class Word {
    // a word is a "column" in the state array.
    int[] word;

    Word(){
        this.word = new int[4];
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
}
