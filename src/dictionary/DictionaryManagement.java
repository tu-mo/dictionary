package dictionary;

import java.io.*;
import java.util.ArrayList;


public class DictionaryManagement {
    private Dictionary dictionary;

    public DictionaryManagement() {
        dictionary = new Dictionary();
    }

    public Dictionary getDictionary() {
        return dictionary;
    }

    public String dictionarySearch(String str) {
        for (int i = 0; i < dictionary.getLists().length; i++) {
            for (int j = 0; j < dictionary.getLists()[i].size(); j++) {
                if (((Word) dictionary.getLists()[i].get(j)).getTarget().equals(str)) {
                    return ((Word) dictionary.getLists()[i].get(j)).getExplain();
                }
            }
        }
        return null;
    }


    public void setDictionary(Dictionary dictionary) {
        this.dictionary = dictionary;
    }

    static int hash(String i) {
        return i.charAt(0) - 97;
    }

    public void insertFromFile() throws IOException {
        BufferedReader buff = null;
        try  {
            buff = new BufferedReader(new FileReader("dtb.txt"));
            String str = null;
            String target = null;
            String explain = null;
            do {
                str = buff.readLine();
                if (str == null) {
                    dictionary.setLists(new Word(target, explain), hash(target));
                }
                if (str != null) {
                    if (str.charAt(0) == '@') {
                        if (target != null) {
                            dictionary.setLists(new Word(target, explain), hash(target));
                        }
                        int index = str.indexOf('/');
                        target = str.substring(1, index - 2);
                        explain = str.substring(index, str.length());
                    } else {
                        explain += "\n";
                        explain += str;
                    }
                }
            } while (str != null);

        } catch (IOException exc) {
            System.out.println(exc.getMessage());
        } finally {
            buff.close();
        }
    }
}
