package dictionary;

import java.io.*;
import java.util.ArrayList;
import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;


public class DictionaryManagement {
    private Dictionary dictionary;

    public DictionaryManagement() {
        dictionary = new Dictionary();
    }

    public Dictionary getDictionary() {
        return dictionary;
    }

    static int hash(String i) {
        return i.charAt(0) - 97;
    }

    public static void quickSort(ArrayList arr, int a, int b) {
        if (a < b) {
            int left = a;
            int right = b - 1;
            int pivot = b;
            while (left <= right) {
                while ((left <= right) && ((Word) arr.get(left)).getTarget().compareTo(((Word) arr.get(pivot)).getTarget()) < 0) {
                    left++;
                }
                while ((right >= left) && ((Word) arr.get(left)).getTarget().compareTo(((Word) arr.get(pivot)).getTarget()) > 0) {
                    right--;
                }
                if (left >= right) {
                    break;
                }
                String temp1 = ((Word) arr.get(left)).getTarget();
                String temp2 = ((Word) arr.get(left)).getExplain();
                ((Word) arr.get(left)).setTarget(((Word) arr.get(right)).getTarget());
                ((Word) arr.get(left)).setExplain(((Word) arr.get(right)).getExplain());
                ((Word) arr.get(right)).setTarget(temp1);
                ((Word) arr.get(right)).setExplain(temp2);
                left++;
                right--;
            }
            String temp1 = ((Word) arr.get(left)).getTarget();
            String temp2 = ((Word) arr.get(left)).getExplain();
            ((Word) arr.get(left)).setTarget(((Word) arr.get(pivot)).getTarget());
            ((Word) arr.get(left)).setExplain(((Word) arr.get(pivot)).getExplain());
            ((Word) arr.get(pivot)).setTarget(temp1);
            ((Word) arr.get(pivot)).setExplain(temp2);

            quickSort(arr, a, left - 1);
            quickSort(arr, left + 1, b);
        }
    }

    public void insertFromFile() throws IOException {
        BufferedReader buff = null;
        try {
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
                        if (index == -1) {
                            target = str.substring(1, str.length());
                            explain = "";
                        } else {
                            target = str.substring(1, index - 1);
                            explain = str.substring(index, str.length());
                        }
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

    public String dictionarySearch(String str) {
        for (int i = 0; i < dictionary.getLists().length; i++) {
            for (int j = 0; j < dictionary.getLists()[i].size(); j++) {
                if (((Word) dictionary.getLists()[i].get(j)).getTarget().equals(str)) {
                    return ((Word) dictionary.getLists()[i].get(j)).getTarget() + " " +((Word) dictionary.getLists()[i].get(j)).getExplain();
                }
            }
        }
        return null;
    }

    public ArrayList dictionarySuggest(String str) {
        ArrayList arrayList = new ArrayList();
        int index = hash(str);
        int num = str.length();
        for (int i = 0; i < dictionary.getLists()[index].size(); i++) {
            if (((Word) dictionary.getLists()[index].get(i)).getTarget().length() >= num)
                if (((Word) dictionary.getLists()[index].get(i)).getTarget().substring(0, num).equals(str)) {
                    arrayList.add(((Word) dictionary.getLists()[index].get(i)).getTarget());
                }
        }
        return arrayList;
    }

    public void dictionaryDelete(String str) {
        int index = hash(str);
        for (int i = 0; i < dictionary.getLists()[index].size(); i++) {
            if (((Word) dictionary.getLists()[index].get(i)).getTarget().equals(str)) {
                dictionary.getLists()[index].remove(i);
            }
        }
    }

    public void dictionaryAdd(Word word) {
        int index = hash(word.getTarget());
        dictionary.getLists()[index].add(word);
        quickSort(dictionary.getLists()[index], 0, dictionary.getLists()[index].size() - 1 );
    }

    public void  dictionaryChange(String target, String explain) {
        int index = hash(target);
        for (int i = 0; i < dictionary.getLists()[index].size(); i++) {
            if (((Word)dictionary.getLists()[index].get(i)).getTarget().equals(target)) {
                ((Word)dictionary.getLists()[index].get(i)).setExplain(explain);
            }
        }
    }
    public void dictionarySpeak(String words) {
        Voice voice;
        System.setProperty("freetts.voices", "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");
        voice = VoiceManager.getInstance().getVoice("kevin16");
        if (voice != null) {
            voice.allocate();// Allocating Voice
            try {
                voice.setRate(190);// Setting the rate of the voice
                voice.setPitch(150);// Setting the Pitch of the voice
                voice.setVolume(3);// Setting the volume of the voice
                voice.speak(words);

            } catch (Exception e1) {
                e1.printStackTrace();
            }

        } else {
            throw new IllegalStateException("Cannot find voice: kevin16");
        }
    }

    public void dictionarySave() throws IOException {
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter("dtb.txt");
            for (int i = 0; i < dictionary.getLists().length; i++) {
                for (int j = 0; j < dictionary.getLists()[i].size(); j++) {
                    if (i == dictionary.getLists().length - 1 && j == dictionary.getLists()[i].size() - 1)
                    {
                        String str = ((Word)dictionary.getLists()[i].get(j)) + " " +((Word)dictionary.getLists()[i].get(j)).getExplain();
                        fileWriter.write("@" + str);
                    }
                    else {
                        String str = ((Word)dictionary.getLists()[i].get(j)) + " " +((Word)dictionary.getLists()[i].get(j)).getExplain();
                        fileWriter.write("@" + str);
                        fileWriter.write("\n");
                    }
                }
            }
        } catch (IOException exc) {
            System.out.println(exc.getMessage());
        } finally {
            fileWriter.close();
        }
    }

}
