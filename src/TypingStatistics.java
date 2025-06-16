public class TypingStatistics {
    private final int totalCharacters;
    private final int correctCharacters;
    private final double accuracy;
    private final int charactersPerMinute;
    private final long elapsedTimeMs;
    
    public TypingStatistics(int totalCharacters, int correctCharacters, 
                           double accuracy, int charactersPerMinute, long elapsedTimeMs) {
        this.totalCharacters = totalCharacters;
        this.correctCharacters = correctCharacters;
        this.accuracy = accuracy;
        this.charactersPerMinute = charactersPerMinute;
        this.elapsedTimeMs = elapsedTimeMs;
    }
    
    // Getters
    public int getTotalCharacters() { return totalCharacters; }
    public int getCorrectCharacters() { return correctCharacters; }
    public double getAccuracy() { return accuracy; }
    public int getCharactersPerMinute() { return charactersPerMinute; }
    public long getElapsedTimeMs() { return elapsedTimeMs; }
    
    @Override
    public String toString() {
        return String.format("字符数: %d, 正确: %d, 准确率: %.1f%%, 速度: %d 字符/分钟",
                           totalCharacters, correctCharacters, accuracy, charactersPerMinute);
    }
 // 添加 builder 模式支持
//    public static class Builder {
//        private int totalChars;
//        private int correctChars;
//        private long elapsedTimeMs;
//        
//        public Builder totalChars(int val) { totalChars = val; return this; }
//        public Builder correctChars(int val) { correctChars = val; return this; }
//        public Builder elapsedTimeMs(long val) { elapsedTimeMs = val; return this; }
//        
//        public TypingStatistics build() {
//            double accuracy = totalChars > 0 ? (correctChars * 100.0) / totalChars : 0;
//            double minutes = elapsedTimeMs / 60000.0;
//            int cpm = minutes > 0 ? (int)(totalChars / minutes) : 0;
//            
//            return new TypingStatistics(totalChars, correctChars, accuracy, cpm, elapsedTimeMs);
//        }
//    }
}
