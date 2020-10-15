package dictionary;

import java.util.ArrayList;

public class Dictionary {
    private ArrayList[] lists;

    public ArrayList[] getLists() {
        return lists;
    }

    public void setLists(Word word, int i) {
        lists[i].add(word);
    }

    public Dictionary() {
        lists = new ArrayList[26];
        for (int i = 0; i < 26; i++) {
            lists[i] = new ArrayList();
        }
    }
}
