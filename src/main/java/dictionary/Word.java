package dictionary;

public class Word {
    /**
     * target lưu từ tiếng anh.
     */
    private String target;
    /**
     * explain lưu phiên âm và nghĩa.
     */
    private String explain;

    /**
     * trả về từ tiếng anh của word.
     *
     * @return target.
     */
    public String getTarget() {
        return target;
    }

    /**
     * trả về phiên âm và nghĩa của từ.
     *
     * @return explain.
     */
    public String getExplain() {
        return explain;
    }

    /**
     * cài đặt phiên âm và nghĩa của từ.
     *
     * @param explain lưu phiên âm và nghĩa của từ
     */
    public void setExplain(String explain) {
        this.explain = explain;
    }

    /**
     * cài đặt từ tiếng anh.
     *
     * @param target lưu từ tiếng anh.
     */
    public void setTarget(String target) {
        this.target = target;
    }

    /**
     * constructor 2 tham số.
     *
     * @param target  lưu từ tiếng anh.
     * @param explain lưu phiên âm và nghĩa.
     */
    public Word(String target, String explain) {
        this.target = target;
        this.explain = explain;
    }

    /**
     * trả về chuỗi theo dạng từ + phiên âm + nghĩa.
     *
     * @return string.
     */
    @Override
    public String toString() {
        return target + " " + explain;
    }
}