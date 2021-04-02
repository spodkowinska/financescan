package info.podkowinski.sandra.financescanner.category;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KeywordDTO {
    Category category;
    boolean safeWord;
    String keyword;

//    public Category getCategory() {
//        return category;
//    }
//
//    public boolean getSafeWord() {
//        return safeWord;
//    }
//
//    public String getKeyword() {
//        return keyword;
//    }
//
//    public void setKeyword(String keyword) {
//        this.keyword = keyword;
//    }
}
