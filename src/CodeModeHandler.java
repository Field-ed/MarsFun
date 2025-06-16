import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*;

public class CodeModeHandler extends AbstractModeHandler {
    
    private JTextPane displayArea;
    private JTextArea inputArea;
    
    @Override
    public void initializeLayout(JPanel container) {
        // 使用GridLayout让显示区域和输入区域等大
        container.setLayout(new GridLayout(2, 1, 0, 10));
        
        // 显示区域（使用JTextPane实现彩色文本）
        displayArea = new JTextPane();
        displayArea.setEditable(false);
        displayArea.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        displayArea.setBackground(new Color(240, 240, 240));
        JScrollPane displayScroll = new JScrollPane(displayArea);
        displayScroll.setBorder(BorderFactory.createTitledBorder("练习文本"));
        
        // 输入区域
        inputArea = new JTextArea();
        inputArea.setLineWrap(true);
        inputArea.setWrapStyleWord(true);
        inputArea.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        inputArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                handleInput(e);
            }
        });
        
        JScrollPane inputScroll = new JScrollPane(inputArea);
        inputScroll.setBorder(BorderFactory.createTitledBorder("您的输入"));
        
        // 添加到容器，GridLayout会自动平均分配空间
        container.add(displayScroll);
        container.add(inputScroll);
        
        // 初始加载内容
        if (content != null) {
            displayArea.setText(content);
        }
    }
    
    private void handleInput(KeyEvent e) {
        String inputText = inputArea.getText();
        if (!practiceStarted && !inputText.isEmpty()) {
            startPractice();
        }
        processInput(e, inputText);
        updateStatistics();
    }
    
    @Override
    public void loadContent(String content) {
        super.loadContent(content); // 调用父类方法
        if (displayArea != null) {
            displayArea.setText(content);
        }
        if (inputArea != null) {
            inputArea.setText("");
        }
    }
    
    @Override
    public TypingResult processInput(KeyEvent keyEvent, String inputText) {
        if (content == null || content.isEmpty()) {
            return new TypingResult(false, false, "没有加载练习内容", getStatistics());
        }
        
        // 更新统计数据
        totalChars = inputText.length();
        correctChars = 0;
        
        // 更新显示区域
        updateDisplayArea(inputText);
        
        // 计算正确字符数
        int minLength = Math.min(content.length(), inputText.length());
        for (int i = 0; i < minLength; i++) {
            if (content.charAt(i) == inputText.charAt(i)) {
                correctChars++;
            }
        }
        
        // 检查是否完成
        boolean isComplete = inputText.length() >= content.length() && 
                           inputText.equals(content);
        
        if (isComplete) {
            completePractice();
        }
        
        return new TypingResult(true, isComplete, "代码模式练习中", getStatistics());
    }
    
    private void updateDisplayArea(String inputText) {
        if (displayArea == null || content == null) return;
        
        StyledDocument doc = displayArea.getStyledDocument();
        
        // 定义文本样式
        Style defaultStyle = displayArea.addStyle("Default", null);
        StyleConstants.setForeground(defaultStyle, Color.BLACK);
        
        Style correctStyle = displayArea.addStyle("Correct", null);
        StyleConstants.setForeground(correctStyle, new Color(0, 150, 0)); // 深绿色
        
        Style incorrectStyle = displayArea.addStyle("Incorrect", null);
        StyleConstants.setForeground(incorrectStyle, Color.RED);
        StyleConstants.setUnderline(incorrectStyle, true);
        
        try {
            // 清空文档
            doc.remove(0, doc.getLength());
            
            int minLength = Math.min(content.length(), inputText.length());
            
            // 已输入部分 - 根据正确性显示不同颜色
            for (int i = 0; i < minLength; i++) {
                char expected = content.charAt(i);
                char actual = inputText.charAt(i);
                Style style = (expected == actual) ? correctStyle : incorrectStyle;
                doc.insertString(doc.getLength(), String.valueOf(expected), style);
            }
            
            // 未输入部分 - 显示为默认颜色
            for (int i = minLength; i < content.length(); i++) {
                doc.insertString(doc.getLength(), String.valueOf(content.charAt(i)), defaultStyle);
            }
            
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    protected void doResetPractice() {
        if (inputArea != null) {
            inputArea.setText("");
            inputArea.requestFocus(); // 重置后自动聚焦到输入区域
        }
        if (displayArea != null && content != null) {
            displayArea.setText(content);
        }
    }
    
    @Override
    public boolean isPracticeComplete() {
        if (content == null || inputArea == null) return false;
        String inputText = inputArea.getText();
        return inputText.equals(content);
    }
    
    @Override
    public String getModeName() {
        return "代码模式";
    }
    
    @Override
    public String getProgressInfo() {
        if (content == null) return "";
        String inputText = inputArea != null ? inputArea.getText() : "";
        int typed = Math.min(inputText.length(), content.length());
        return String.format("进度: %d/%d (%.1f%%)", 
                           typed, 
                           content.length(), 
                           (double)typed/content.length()*100);
    }
    
    /**
     * 获取输入区域的引用，用于外部设置焦点等操作
     */
    public JTextArea getInputArea() {
        return inputArea;
    }
    
    /**
     * 获取显示区域的引用
     */
    public JTextPane getDisplayArea() {
        return displayArea;
    }
}