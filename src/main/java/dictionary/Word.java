package dictionary;
public class Word {

    private String target;
    private String explain;

    public String getTarget() {
        return target;
    }

    public String getExplain() {
        return explain;
    }

    public void setExplain(String explain) {
        this.explain = explain;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public Word(String target, String explain) {
        this.target = target;
        this.explain = explain;
    }

    public String toString() {
        return target + " " + explain;
    }
}