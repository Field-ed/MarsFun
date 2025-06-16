
public enum Mode {
    CODE("代码模式"),
    ARTICLE("文章模式"),
    WORD("单词模式");
    
    private final String description;
    
    Mode(String description) {
        this.description = description;
    }
    
    @Override
    public String toString() {
        return description;
    }
    
    public String getDescription() {
        return description;
    }
}