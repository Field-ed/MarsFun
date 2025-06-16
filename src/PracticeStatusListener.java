public interface PracticeStatusListener {
    
    /**
     * ��ϰ��ʼʱ�ص�
     */
    void onPracticeStarted();
    
    /**
     * ��ϰ���ʱ�ص�
     * @param statistics ����ͳ�ƽ��
     */
    void onPracticeCompleted(TypingStatistics statistics);
    
    /**
     * ͳ����Ϣ����ʱ�ص�
     * @param statistics ��ǰͳ����Ϣ
     */
    void onStatisticsUpdated(TypingStatistics statistics);
    
    /**
     * ���ȸ���ʱ�ص�
     * @param progress ������Ϣ
     */
    void onProgressUpdated(String progress);
    
}
