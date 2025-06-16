public abstract class AbstractModeHandler implements ModeHandler {
    
    protected PracticeStatusListener statusListener;
    protected long startTime;
    protected boolean practiceStarted;
    protected String content;
    
    // ͳ������ֶ�
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
        doResetPractice(); // ����ʵ�־��������߼�
    }
    
    /**
     * ����ʵ�־���������߼�
     */
    protected abstract void doResetPractice();
    
    /**
     * ��ʼ��ϰ
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
     * �����ϰ
     */
    protected void completePractice() {
        if (statusListener != null) {
            statusListener.onPracticeCompleted(getStatistics());
        }
    }
    
    /**
     * ����ͳ����Ϣ
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
