import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*;

public class CodeModeHandler extends AbstractModeHandler {
    
    private JTextPane displayArea;
    private JTextArea inputArea;
    
    @Override
    public void initializeLayout(JPanel container) {
        // ʹ��GridLayout����ʾ�������������ȴ�
        container.setLayout(new GridLayout(2, 1, 0, 10));
        
        // ��ʾ����ʹ��JTextPaneʵ�ֲ�ɫ�ı���
        displayArea = new JTextPane();
        displayArea.setEditable(false);
        displayArea.setFont(new Font("΢���ź�", Font.PLAIN, 16));
        displayArea.setBackground(new Color(240, 240, 240));
        JScrollPane displayScroll = new JScrollPane(displayArea);
        displayScroll.setBorder(BorderFactory.createTitledBorder("��ϰ�ı�"));
        
        // ��������
        inputArea = new JTextArea();
        inputArea.setLineWrap(true);
        inputArea.setWrapStyleWord(true);
        inputArea.setFont(new Font("΢���ź�", Font.PLAIN, 16));
        inputArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                handleInput(e);
            }
        });
        
        JScrollPane inputScroll = new JScrollPane(inputArea);
        inputScroll.setBorder(BorderFactory.createTitledBorder("��������"));
        
        // ��ӵ�������GridLayout���Զ�ƽ������ռ�
        container.add(displayScroll);
        container.add(inputScroll);
        
        // ��ʼ��������
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
        super.loadContent(content); // ���ø��෽��
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
            return new TypingResult(false, false, "û�м�����ϰ����", getStatistics());
        }
        
        // ����ͳ������
        totalChars = inputText.length();
        correctChars = 0;
        
        // ������ʾ����
        updateDisplayArea(inputText);
        
        // ������ȷ�ַ���
        int minLength = Math.min(content.length(), inputText.length());
        for (int i = 0; i < minLength; i++) {
            if (content.charAt(i) == inputText.charAt(i)) {
                correctChars++;
            }
        }
        
        // ����Ƿ����
        boolean isComplete = inputText.length() >= content.length() && 
                           inputText.equals(content);
        
        if (isComplete) {
            completePractice();
        }
        
        return new TypingResult(true, isComplete, "����ģʽ��ϰ��", getStatistics());
    }
    
    private void updateDisplayArea(String inputText) {
        if (displayArea == null || content == null) return;
        
        StyledDocument doc = displayArea.getStyledDocument();
        
        // �����ı���ʽ
        Style defaultStyle = displayArea.addStyle("Default", null);
        StyleConstants.setForeground(defaultStyle, Color.BLACK);
        
        Style correctStyle = displayArea.addStyle("Correct", null);
        StyleConstants.setForeground(correctStyle, new Color(0, 150, 0)); // ����ɫ
        
        Style incorrectStyle = displayArea.addStyle("Incorrect", null);
        StyleConstants.setForeground(incorrectStyle, Color.RED);
        StyleConstants.setUnderline(incorrectStyle, true);
        
        try {
            // ����ĵ�
            doc.remove(0, doc.getLength());
            
            int minLength = Math.min(content.length(), inputText.length());
            
            // �����벿�� - ������ȷ����ʾ��ͬ��ɫ
            for (int i = 0; i < minLength; i++) {
                char expected = content.charAt(i);
                char actual = inputText.charAt(i);
                Style style = (expected == actual) ? correctStyle : incorrectStyle;
                doc.insertString(doc.getLength(), String.valueOf(expected), style);
            }
            
            // δ���벿�� - ��ʾΪĬ����ɫ
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
            inputArea.requestFocus(); // ���ú��Զ��۽�����������
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
        return "����ģʽ";
    }
    
    @Override
    public String getProgressInfo() {
        if (content == null) return "";
        String inputText = inputArea != null ? inputArea.getText() : "";
        int typed = Math.min(inputText.length(), content.length());
        return String.format("����: %d/%d (%.1f%%)", 
                           typed, 
                           content.length(), 
                           (double)typed/content.length()*100);
    }
    
    /**
     * ��ȡ������������ã������ⲿ���ý���Ȳ���
     */
    public JTextArea getInputArea() {
        return inputArea;
    }
    
    /**
     * ��ȡ��ʾ���������
     */
    public JTextPane getDisplayArea() {
        return displayArea;
    }
}