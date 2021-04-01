package info.podkowinski.sandra.financescanner.category;

public class KeywordDTO {
    Category category;
    boolean safeKeyword;
    String keyword;

    public Category getCategory() {
        return category;
    }

    public boolean getSafeKeyword() {
        return safeKeyword;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
