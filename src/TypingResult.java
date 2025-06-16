public class TypingResult {
    private final boolean isCorrect;
    private final boolean isComplete;
    private final String feedback;
    private final TypingStatistics statistics;
    
    public TypingResult(boolean isCorrect, boolean isComplete, 
                       String feedback, TypingStatistics statistics) {
        this.isCorrect = isCorrect;
        this.isComplete = isComplete;
        this.feedback = feedback;
        this.statistics = statistics;
    }
    
    // Getters
    public boolean isCorrect() { return isCorrect; }
    public boolean isComplete() { return isComplete; }
    public String getFeedback() { return feedback; }
    public TypingStatistics getStatistics() { return statistics; }
}