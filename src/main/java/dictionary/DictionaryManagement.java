package main.java.dictionary;

import java.io.*;
import java.util.ArrayList;



public class DictionaryManagement {
    ArrayList link[];
    public String explain(String target) {
        int i = hash(target);
        for (int k = 0; k < link[i].size(); k++) {

                if (((Word) link[i].get(k)).target.equals(target)) {
                    return (((Word) link[i].get(k)).explain);
                }

        }
        return null;
    }
    static int hash(String i) {
        int index = 0;
        for (int j = 0; j < i.length(); j++) {
            if (!(97 <= i.charAt(j) && i.charAt(j) <= 'z')) {
                index++;
            } else {
                break;
            }
        }
        return i.charAt(index) - 97;
    }
    public void read() throws IOException {
        link = new ArrayList[26];
        for (int n = 0; n < 26; n++) {
            link[n] = new ArrayList();
        }
        FileReader file = new FileReader("EnViOut.txt");
        BufferedReader buff = new BufferedReader(file);
        String a = null;
        String target = null;
        String explain = null;

        int i = 0;
        do {
            a = buff.readLine();
            if (a == null || a.equals("")) {
                link[hash(target)].add(new Word(target, explain));
                i += 1;
            }

            if (a != null && a.equals("") == false) {
                i = 0;
                if (a.charAt(0) == '@') {
                    if (target != null) {
                        link[hash(target)].add(new Word(target, explain));
                    }
                    int index = a.indexOf('/');
                    target = a.substring(1, index - 1);
                    explain = a.substring(index + 1, a.length());
                } else {
                    explain += "\n";
                    explain += a;
                }
            }

        } while (i != 2);

    }



}