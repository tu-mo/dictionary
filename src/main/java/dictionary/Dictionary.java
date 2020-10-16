package dictionary;

import java.util.ArrayList;

public class Dictionary {
    /**
     * mảng arraylist chứa các arraylist.
     * mỗi arraylist chứa các từ theo chữ cái đầu.
     */
    private ArrayList[] lists;

    /**
     * trả về araylist chứa word.
     *
     * @return list.
     */
    public ArrayList[] getLists() {
        return lists;
    }

    /**
     * constructor chứa từ và giá trị băm của từ.
     *
     * @param word lưu từ.
     * @param i    lưu giá trị băm của từ.
     */
    public void setLists(Word word, int i) {
        lists[i].add(word);
    }

    /**
     * constructor khởi tạo mảng arraylist chứa các từ.
     */
    public Dictionary() {
        lists = new ArrayList[26];
        for (int i = 0; i < 26; i++) {
            lists[i] = new ArrayList();
        }
    }
}
