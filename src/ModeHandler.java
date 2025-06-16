import java.awt.event.KeyEvent;

import javax.swing.JPanel;

public interface ModeHandler {
    
    /**
     * ��ʼ��ģʽ��UI����
     * @param container �������
     */
    void initializeLayout(JPanel container);
    
    /**
     * ������ϰ�ı�����
     * @param content �ı�����
     */
    void loadContent(String content);
    
    /**
     * ������������¼�
     * @param keyEvent �����¼�
     * @param inputText ��ǰ������ı�
     * @return ��������Ϣ
     */
    TypingResult processInput(KeyEvent keyEvent, String inputText);
    
    /**
     * ������ϰ״̬
     */
    void resetPractice();
    
    /**
     * �����ϰ�Ƿ����
     * @return �Ƿ����
     */
    boolean isPracticeComplete();
    
    /**
     * ��ȡ��ǰ��ϰͳ����Ϣ
     * @return ͳ����Ϣ
     */
    TypingStatistics getStatistics();
    
    /**
     * ��ȡģʽ����
     * @return ģʽ����
     */
    String getModeName();
    
    /**
     * ��ȡ��ǰ������Ϣ
     * @return ������Ϣ�ַ���
     */
    String getProgressInfo();
    
    /**
     * ������ϰ״̬������
     * @param listener ״̬������
     */
    void setPracticeStatusListener(PracticeStatusListener listener);

    PracticeStatusListener getPracticeStatusListener();
}
