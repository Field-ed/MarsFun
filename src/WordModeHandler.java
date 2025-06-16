import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import javax.swing.JPanel;

public class WordModeHandler extends AbstractModeHandler {
    
    private JLabel currentWordLabel;
    private JTextArea wordInputArea;
    private JLabel wordProgressLabel;
    private String[] words;
    private int currentWordIndex = 0;
    
    @Override
    public void initializeLayout(JPanel container) {
        container.setLayout(new BorderLayout(0, 20));
        
        // ����������ʾ
        wordProgressLabel = new JLabel("", JLabel.CENTER);
        wordProgressLabel.setFont(new Font("΢���ź�", Font.PLAIN, 14));
        wordProgressLabel.setForeground(Color.GRAY);
        container.add(wordProgressLabel, BorderLayout.NORTH);
        
        // �м䵱ǰ������ʾ����
        JPanel wordDisplayPanel = new JPanel(new BorderLayout());
        wordDisplayPanel.setBorder(BorderFactory.createTitledBorder("��ǰ����"));
        
        currentWordLabel = new JLabel("", JLabel.CENTER);
        currentWordLabel.setFont(new Font("΢���ź�", Font.BOLD, 36));
        currentWordLabel.setForeground(Color.BLACK);
        currentWordLabel.setPreferredSize(new java.awt.Dimension(0, 120));
        
        wordDisplayPanel.add(currentWordLabel, BorderLayout.CENTER);
        container.add(wordDisplayPanel, BorderLayout.CENTER);
        
        // �ײ���������
        JPanel wordInputPanel = new JPanel(new BorderLayout());
        wordInputPanel.setBorder(BorderFactory.createTitledBorder("���뵥��"));
        
        wordInputArea = new JTextArea();
        wordInputArea.setFont(new Font("΢���ź�", Font.PLAIN, 18));
        wordInputArea.setRows(3);
        wordInputArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String inputText = wordInputArea.getText().trim();
                if (!practiceStarted && !inputText.isEmpty()) {
                    startPractice();
                }
                TypingResult result = processInput(e, inputText);
                updateStatistics();
            }
        });
        
        JScrollPane wordInputScroll = new JScrollPane(wordInputArea);
        wordInputPanel.add(wordInputScroll, BorderLayout.CENTER);
        container.add(wordInputPanel, BorderLayout.SOUTH);
    }
    
    @Override
    public void loadContent(String content) {
        super.loadContent(content);
        setupWordMode(content);
    }
    
    private void setupWordMode(String content) {
        words = content.trim().split("\\s+");
        
        java.util.List<String> wordList = new java.util.ArrayList<>();
        for (String word : words) {
            if (!word.trim().isEmpty()) {
                wordList.add(word.trim());
            }
        }
        words = wordList.toArray(new String[0]);
        
        currentWordIndex = 0;
        showCurrentWord();
    }
    
    @Override
    public TypingResult processInput(KeyEvent keyEvent, String inputText) {
        if (words == null || currentWordIndex >= words.length) {
            return new TypingResult(false, true, "��ϰ���", getStatistics());
        }
        
        String currentWord = words[currentWordIndex];
        
        // ����ͳ����Ϣ
        calculateWordStatistics(inputText);
        
        // �ı䵥����ʾ��ɫ
        updateWordDisplay(inputText, currentWord);
        
        // ����Ƿ��¿ո����س���
        if (keyEvent.getKeyCode() == KeyEvent.VK_SPACE || keyEvent.getKeyCode() == KeyEvent.VK_ENTER) {
            String cleanInput = inputText.replaceAll("\\s+$", "");
            
            currentWordIndex++;
            if (currentWordIndex < words.length) {
                showCurrentWord();
            } else {
                completePractice();
                return new TypingResult(true, true, "���е������", getStatistics());
            }
            
            keyEvent.consume();
        }
        
        return new TypingResult(currentWord.startsWith(inputText), 
                               currentWordIndex >= words.length, "����ģʽ��ϰ��", getStatistics());
    }
    
    private void calculateWordStatistics(String inputText) {
        totalChars = 0;
        correctChars = 0;
        
        // ��������ɵ��ʵ�ͳ��
        for (int i = 0; i < currentWordIndex; i++) {
            totalChars += words[i].length();
            correctChars += words[i].length();
        }
        
        // ���㵱ǰ���ʵ�ͳ��
        if (currentWordIndex < words.length) {
            String currentWord = words[currentWordIndex];
            totalChars += inputText.length();
            int minLength = Math.min(currentWord.length(), inputText.length());
            for (int i = 0; i < minLength; i++) {
                if (currentWord.charAt(i) == inputText.charAt(i)) {
                    correctChars++;
                }
            }
        }
    }
    
    private void updateWordDisplay(String inputText, String currentWord) {
        if (inputText.isEmpty()) {
            currentWordLabel.setForeground(Color.BLACK);
        } else if (currentWord.startsWith(inputText)) {
            currentWordLabel.setForeground(Color.BLUE);
        } else {
            currentWordLabel.setForeground(Color.RED);
        }
    }
    
    private void showCurrentWord() {
        if (words != null && currentWordIndex < words.length) {
            currentWordLabel.setText(words[currentWordIndex]);
            updateWordProgress();
            
            if (wordInputArea != null) {
                wordInputArea.setText("");
                wordInputArea.requestFocus();
            }
        } else {
            currentWordLabel.setText("��ϰ���!");
            wordProgressLabel.setText("");
        }
    }
    
    private void updateWordProgress() {
        if (words != null && wordProgressLabel != null) {
            wordProgressLabel.setText(String.format("����: %d / %d", 
                currentWordIndex + 1, words.length));
        }
    }
    
    @Override
    protected void doResetPractice() {
        if (wordInputArea != null) {
            wordInputArea.setText("");
        }
        currentWordIndex = 0;
        if (words != null && words.length > 0) {
            showCurrentWord();
        }
    }
    
    @Override
    public boolean isPracticeComplete() {
        return words != null && currentWordIndex >= words.length;
    }
    
    @Override
    public String getModeName() {
        return "����ģʽ";
    }
    
    @Override
    public String getProgressInfo() {
        if (words == null) return "";
        return String.format("��ǰ����: %d/%d", 
            Math.min(currentWordIndex + 1, words.length), words.length);
    }
}