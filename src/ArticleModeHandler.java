import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import javax.swing.JPanel;

public class ArticleModeHandler extends AbstractModeHandler {
    
    private JTextPane articleDisplayArea;
    private JTextArea articleInputArea;
    private String[] originalLines;
    private String[] inputLines;
    private int currentLineIndex = 0;
    
    @Override
    public void initializeLayout(JPanel container) {
        container.setLayout(new BorderLayout());
        
        // 创建显示区域
        articleDisplayArea = new JTextPane() {
            @Override
            public boolean getScrollableTracksViewportWidth() {
                return true;
            }
        };
        articleDisplayArea.setEditable(false);
        articleDisplayArea.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        articleDisplayArea.setBackground(Color.WHITE);
        articleDisplayArea.addMouseListener(new MouseAdapter() {
        
        	@Override
            public void mouseClicked(MouseEvent e) {
                // 点击显示区域时，将焦点转移到输入区域
                articleInputArea.requestFocusInWindow();
            }
        });
        // 创建输入区域
        articleInputArea = new JTextArea();
        articleInputArea.setOpaque(false);
        articleInputArea.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        articleInputArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String inputText = articleInputArea.getText();
                if (!practiceStarted && !inputText.isEmpty()) {
                    startPractice();
                }
                TypingResult result = processInput(e, inputText);
                updateStatistics();
            }
        });
        
        JScrollPane displayScroll = new JScrollPane(articleDisplayArea);
        displayScroll.setBorder(BorderFactory.createTitledBorder("原文与输入对照"));
        
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.setBorder(BorderFactory.createTitledBorder("输入区域"));
        inputPanel.add(new JScrollPane(articleInputArea), BorderLayout.CENTER);
        inputPanel.setPreferredSize(new java.awt.Dimension(0, 0));
        
        container.add(displayScroll, BorderLayout.CENTER);
        container.add(inputPanel, BorderLayout.SOUTH);
        articleInputArea.addFocusListener(new FocusAdapter() {

        });
     // 在 initializeLayout 方法最后添加
        container.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                articleInputArea.requestFocusInWindow();
            }
        });
    }
    
    @Override
    public void loadContent(String content) {
        super.loadContent(content);
        setupArticleMode(content);
        // 确保焦点在输入区域
        SwingUtilities.invokeLater(() -> {
            articleInputArea.requestFocusInWindow();
        });
    }
    
    private void setupArticleMode(String content) {
        originalLines = content.split("\n");
        currentLineIndex = 0;
        inputLines = new String[originalLines.length];
        
        for (int i = 0; i < inputLines.length; i++) {
            inputLines[i] = "";
        }
        
        updateArticleDisplay();
    }
    
    @Override
    public TypingResult processInput(KeyEvent keyEvent, String inputText) {
        if (originalLines == null) {
            return new TypingResult(false, false, "没有加载练习内容", getStatistics());
        }
        
        String[] currentInputLines = inputText.split("\n", -1);
        
        // 更新输入行数组
        for (int i = 0; i < inputLines.length && i < currentInputLines.length; i++) {
            inputLines[i] = currentInputLines[i];
        }
        
        // 计算统计信息
        calculateStatistics();
        
        // 检查当前行是否完成
        checkLineCompletion();
        
        // 更新显示
        updateArticleDisplay();
        
        boolean isComplete = isPracticeComplete();
        if (isComplete) {
            completePractice();
        }
        
        return new TypingResult(true, isComplete, "文章模式练习中", getStatistics());
    }
    
    private void calculateStatistics() {
        totalChars = 0;
        correctChars = 0;
        
        for (int i = 0; i <= currentLineIndex && i < originalLines.length; i++) {
            String originalLine = originalLines[i];
            String inputLine = inputLines[i];
            
            totalChars += inputLine.length();
            
            int minLength = Math.min(originalLine.length(), inputLine.length());
            for (int j = 0; j < minLength; j++) {
                if (originalLine.charAt(j) == inputLine.charAt(j)) {
                    correctChars++;
                }
            }
        }
    }
    
    private void checkLineCompletion() {
        if (currentLineIndex < originalLines.length && currentLineIndex < inputLines.length) {
            String currentOriginal = originalLines[currentLineIndex];
            String currentInput = inputLines[currentLineIndex];
            
            if (currentInput.length() >= currentOriginal.length() && 
                currentInput.substring(0, currentOriginal.length()).equals(currentOriginal)) {
                
                if (currentLineIndex < originalLines.length - 1) {
                    currentLineIndex++;
                    if (!articleInputArea.getText().endsWith("\n")) {
                        articleInputArea.append("\n");
                    }
                }
            }
        }
    }
    
    private void updateArticleDisplay() {
        if (originalLines == null || articleDisplayArea == null) return;
        
        StyledDocument doc = articleDisplayArea.getStyledDocument();
        
        // 定义样式
        Style originalStyle = articleDisplayArea.addStyle("Original", null);
        StyleConstants.setForeground(originalStyle, Color.BLACK);
        StyleConstants.setBackground(originalStyle, new Color(240, 240, 240));
        
        Style inputCorrectStyle = articleDisplayArea.addStyle("InputCorrect", null);
        StyleConstants.setForeground(inputCorrectStyle, Color.BLUE);
        
        Style inputIncorrectStyle = articleDisplayArea.addStyle("InputIncorrect", null);
        StyleConstants.setForeground(inputIncorrectStyle, Color.RED);
        
        Style currentLineStyle = articleDisplayArea.addStyle("CurrentLine", null);
        StyleConstants.setBackground(currentLineStyle, new Color(255, 255, 200));
        StyleConstants.setBold(currentLineStyle, true);
        
        try {
            doc.remove(0, doc.getLength());
            
            for (int i = 0; i < originalLines.length; i++) {
                String originalLine = originalLines[i];
                String inputLine = inputLines[i];
                
                Style lineStyle = (i == currentLineIndex) ? currentLineStyle : originalStyle;
                doc.insertString(doc.getLength(), "原文: " + originalLine + "\n", lineStyle);
                
                if (!inputLine.isEmpty() || i <= currentLineIndex) {
                    doc.insertString(doc.getLength(), "输入: ", inputCorrectStyle);
                    
                    int minLength = Math.min(originalLine.length(), inputLine.length());
                    for (int j = 0; j < minLength; j++) {
                        char inputChar = inputLine.charAt(j);
                        char originalChar = originalLine.charAt(j);
                        Style charStyle = (inputChar == originalChar) ? inputCorrectStyle : inputIncorrectStyle;
                        doc.insertString(doc.getLength(), String.valueOf(inputChar), charStyle);
                    }
                    
                    if (inputLine.length() > originalLine.length()) {
                        String extraChars = inputLine.substring(originalLine.length());
                        doc.insertString(doc.getLength(), extraChars, inputIncorrectStyle);
                    }
                    
                    doc.insertString(doc.getLength(), "\n", inputCorrectStyle);
                }
                
                if (i < originalLines.length - 1) {
                    doc.insertString(doc.getLength(), "——————————————————\n", originalStyle);
                }
            }
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
        
        articleDisplayArea.setCaretPosition(doc.getLength());
    }
    
    @Override
    protected void doResetPractice() {
        if (articleInputArea != null) {
            articleInputArea.setText("");
        }
        currentLineIndex = 0;
        if (inputLines != null) {
            for (int i = 0; i < inputLines.length; i++) {
                inputLines[i] = "";
            }
        }
        updateArticleDisplay();
    }
    
    @Override
    public boolean isPracticeComplete() {
        if (originalLines == null || inputLines == null) return false;
        
        if (currentLineIndex >= originalLines.length - 1) {
            if (currentLineIndex < originalLines.length && currentLineIndex < inputLines.length) {
                String lastOriginal = originalLines[currentLineIndex];
                String lastInput = inputLines[currentLineIndex];
                return lastInput.length() >= lastOriginal.length() && 
                       lastInput.substring(0, lastOriginal.length()).equals(lastOriginal);
            }
        }
        return false;
    }
    
    @Override
    public String getModeName() {
        return "文章模式";
    }
    
    @Override
    public String getProgressInfo() {
        if (originalLines == null) return "";
        return String.format("当前行: %d/%d", currentLineIndex + 1, originalLines.length);
    }

}