package main.java.dictionary;
class Word {

    String target;
    String explain;

    public Word(String target, String explain) {
        this.target = target;
        this.explain = explain;
    }

    public String toString() {
        return target + " " + explain;
    }
}