import java.awt.event.KeyEvent;

import javax.swing.JPanel;

public interface ModeHandler {
    
    /**
     * 初始化模式的UI布局
     * @param container 容器面板
     */
    void initializeLayout(JPanel container);
    
    /**
     * 加载练习文本内容
     * @param content 文本内容
     */
    void loadContent(String content);
    
    /**
     * 处理键盘输入事件
     * @param keyEvent 键盘事件
     * @param inputText 当前输入的文本
     * @return 处理结果信息
     */
    TypingResult processInput(KeyEvent keyEvent, String inputText);
    
    /**
     * 重置练习状态
     */
    void resetPractice();
    
    /**
     * 检查练习是否完成
     * @return 是否完成
     */
    boolean isPracticeComplete();
    
    /**
     * 获取当前练习统计信息
     * @return 统计信息
     */
    TypingStatistics getStatistics();
    
    /**
     * 获取模式名称
     * @return 模式名称
     */
    String getModeName();
    
    /**
     * 获取当前进度信息
     * @return 进度信息字符串
     */
    String getProgressInfo();
    
    /**
     * 设置练习状态监听器
     * @param listener 状态监听器
     */
    void setPracticeStatusListener(PracticeStatusListener listener);

    PracticeStatusListener getPracticeStatusListener();
}
