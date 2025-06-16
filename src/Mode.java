
public enum Mode {
    CODE("����ģʽ"),
    ARTICLE("����ģʽ"),
    WORD("����ģʽ");
    
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