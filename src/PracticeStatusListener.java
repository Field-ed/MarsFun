public interface PracticeStatusListener {
    
    /**
     * 练习开始时回调
     */
    void onPracticeStarted();
    
    /**
     * 练习完成时回调
     * @param statistics 最终统计结果
     */
    void onPracticeCompleted(TypingStatistics statistics);
    
    /**
     * 统计信息更新时回调
     * @param statistics 当前统计信息
     */
    void onStatisticsUpdated(TypingStatistics statistics);
    
    /**
     * 进度更新时回调
     * @param progress 进度信息
     */
    void onProgressUpdated(String progress);
    
}
