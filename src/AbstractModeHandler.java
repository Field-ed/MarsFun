public abstract class AbstractModeHandler implements ModeHandler {
    
    protected PracticeStatusListener statusListener;
    protected long startTime;
    protected boolean practiceStarted;
    protected String content;
    
    // 统计相关字段
    protected int totalChars = 0;
    protected int correctChars = 0;
    
    @Override
    public void setPracticeStatusListener(PracticeStatusListener listener) {
        this.statusListener = listener;
    }
    
    @Override
    public void loadContent(String content) {
        this.content = content;
        resetPractice();
    }
    
    @Override
    public void resetPractice() {
        practiceStarted = false;
        startTime = 0;
        totalChars = 0;
        correctChars = 0;
        doResetPractice(); // 子类实现具体重置逻辑
    }
    
    /**
     * 子类实现具体的重置逻辑
     */
    protected abstract void doResetPractice();
    
    /**
     * 开始练习
     */
    protected void startPractice() {
        if (!practiceStarted) {
            practiceStarted = true;
            startTime = System.currentTimeMillis();
            if (statusListener != null) {
                statusListener.onPracticeStarted();
            }
        }
    }
    
    /**
     * 完成练习
     */
    protected void completePractice() {
        if (statusListener != null) {
            statusListener.onPracticeCompleted(getStatistics());
        }
    }
    
    /**
     * 更新统计信息
     */
    protected void updateStatistics() {
        if (statusListener != null) {
            statusListener.onStatisticsUpdated(getStatistics());
            statusListener.onProgressUpdated(getProgressInfo());
        }
    }
    
    @Override
    public TypingStatistics getStatistics() {
        long elapsedTime = practiceStarted ? System.currentTimeMillis() - startTime : 0;
        double minutes = elapsedTime / 60000.0;
        int cpm = minutes > 0 ? (int) (totalChars / minutes) : 0;
        double accuracy = totalChars > 0 ? (double) correctChars / totalChars * 100 : 0;
        
        return new TypingStatistics(totalChars, correctChars, accuracy, cpm, elapsedTime);
    }
    
    @Override
    public PracticeStatusListener getPracticeStatusListener() {
        return statusListener;
    }
}
