import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.time.LocalDate;
import java.util.List;

public class PracticePanel extends JPanel {
    private ModeHandler currentHandler;
    private final TextManager textManager;
    private final JLabel statusLabel;
    private final JLabel timerLabel;
    private final JComboBox<String> textSelector;
    private final JComboBox<Mode> modeSelector;
    private final Timer timer;
    private int seconds = 0;
    private String currentTextName;
    private JPanel centerPanel;

    public PracticePanel(TextManager textManager) {
        this.textManager = textManager;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Initialize components
        statusLabel = new JLabel("准备开始...");
        timerLabel = new JLabel("时间: 00:00");
        textSelector = new JComboBox<>();
        modeSelector = new JComboBox<>(Mode.values());
        timer = new Timer(1000, e -> updateTimer());

        initializeUI();
    }

    private void initializeUI() {
        // Create control panel at the top
        add(createControlPanel(), BorderLayout.NORTH);
        
        // Create center practice area
        centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBorder(BorderFactory.createTitledBorder("练习区域"));
        add(centerPanel, BorderLayout.CENTER);
        
        // Create bottom panel with buttons
        add(createButtonPanel(), BorderLayout.SOUTH);

        // Set up event listeners
        setupEventListeners();
        refreshTextList();
    }

    private JPanel createControlPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        panel.add(new JLabel("练习模式:"));
        panel.add(modeSelector);
        panel.add(new JLabel("练习文本:"));
        panel.add(textSelector);
        panel.add(timerLabel);
        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 5));
        panel.add(statusLabel, BorderLayout.CENTER);
        
        JButton startButton = new JButton("开始");
        startButton.addActionListener(e -> startPractice());
        
        JButton resetButton = new JButton("重置");
        resetButton.addActionListener(e -> resetPractice());
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        buttonPanel.add(startButton);
        buttonPanel.add(resetButton);
        panel.add(buttonPanel, BorderLayout.EAST);
        
        return panel;
    }

    private void setupEventListeners() {
        modeSelector.addActionListener(e -> switchMode((Mode) modeSelector.getSelectedItem()));
    }

    private void refreshTextList() {
        textSelector.removeAllItems();
        List<String> textNames = textManager.getTextNames();
        textNames.forEach(textSelector::addItem);
        if (!textNames.isEmpty()) {
            textSelector.setSelectedIndex(0);
        }
    }

    private void switchMode(Mode mode) {
        cleanupCurrentHandler();
        
        currentHandler = ModeHandlerFactory.createHandler(mode);
        currentHandler.setPracticeStatusListener(createStatusListener());
        
        initializeHandlerUI();
    }

    private void cleanupCurrentHandler() {
        if (currentHandler != null) {
            centerPanel.removeAll();
        }
    }

    private PracticeStatusListener createStatusListener() {
        return new PracticeStatusListener() {
            @Override
            public void onPracticeStarted() {
                seconds = 0;
                timer.start();
                statusLabel.setText("练习中 - " + currentHandler.getModeName());
            }

            @Override
            public void onPracticeCompleted(TypingStatistics stats) {
                handlePracticeCompletion(stats);
            }

            @Override
            public void onStatisticsUpdated(TypingStatistics stats) {
                updateStatus(stats);
            }

            @Override
            public void onProgressUpdated(String progress) {
                statusLabel.setText(statusLabel.getText() + " | " + progress);
            }
        };
    }

    private void initializeHandlerUI() {
        centerPanel.removeAll();
        currentHandler.initializeLayout(centerPanel);
        setupInputListeners(centerPanel);
        centerPanel.revalidate();
        centerPanel.repaint();
    }

    private void setupInputListeners(Container container) {
        for (Component comp : container.getComponents()) {
            if (comp instanceof JTextComponent) {
                ((JTextComponent) comp).addKeyListener(new PracticeKeyListener());
            } else if (comp instanceof Container) {
                setupInputListeners((Container) comp);
            }
        }
    }

    private class PracticeKeyListener extends KeyAdapter {
        @Override
        public void keyReleased(KeyEvent e) {
            if (currentHandler == null) return;

            JTextComponent source = (JTextComponent) e.getSource();
            TypingResult result = currentHandler.processInput(e, source.getText());
            processTypingResult(result, source);

            if (currentHandler.isPracticeComplete()) {
                currentHandler.getPracticeStatusListener()
                    .onPracticeCompleted(currentHandler.getStatistics());
            }
        }
    }

    private void processTypingResult(TypingResult result, JTextComponent source) {
        // Visual feedback
        source.setBackground(result.isCorrect() ? Color.WHITE : new Color(255, 230, 230));
        
        // Update statistics
        if (result.getStatistics() != null) {
            currentHandler.getPracticeStatusListener()
                .onStatisticsUpdated(result.getStatistics());
        }
        
        // Update progress
        String progress = currentHandler.getProgressInfo();
        if (progress != null && !progress.isEmpty()) {
            currentHandler.getPracticeStatusListener()
                .onProgressUpdated(progress);
        }
    }

    private void startPractice() {
        if (currentHandler == null) return;
        
        currentTextName = (String) textSelector.getSelectedItem();
        if (currentTextName == null) return;

        String content = textManager.getTextContent(currentTextName);
        currentHandler.loadContent(content);
        currentHandler.resetPractice();
        resetUIState();
        currentHandler.getPracticeStatusListener().onPracticeStarted();
    }

    private void resetPractice() {
        if (currentHandler != null) {
            currentHandler.resetPractice();
            resetUIState();
            timer.stop();
            seconds = 0;
            timerLabel.setText("时间: 00:00");
            statusLabel.setText("准备开始...");
        }
    }

    private void resetUIState() {
        resetComponentColors(centerPanel);
    }

    private void resetComponentColors(Container container) {
        for (Component comp : container.getComponents()) {
            if (comp instanceof JTextComponent) {
                comp.setBackground(Color.WHITE);
            } else if (comp instanceof Container) {
                resetComponentColors((Container) comp);
            }
        }
    }

    private void updateTimer() {
        seconds++;
        timerLabel.setText(String.format("时间: %02d:%02d", seconds / 60, seconds % 60));
    }

    private void handlePracticeCompletion(TypingStatistics stats) {
        timer.stop();
        savePracticeRecord(stats);
        showCompletionDialog(stats);
    }

    private void savePracticeRecord(TypingStatistics stats) {
        TypingRecord record = new TypingRecord(
            LocalDate.now().toString(),
            currentTextName,
            stats.getAccuracy(),
            stats.getCharactersPerMinute()
        );
        textManager.saveTypingRecord(record);
    }

    private void showCompletionDialog(TypingStatistics stats) {
        String message = String.format(
            "%s 练习完成!\n" +
            "%s\n" +
            "用时: %d秒\n" +
            "速度: %d 字符/分钟\n" +
            "准确率: %.1f%%",
            currentHandler.getModeName(),
            currentTextName,
            seconds,
            stats.getCharactersPerMinute(),
            stats.getAccuracy());
        
        JOptionPane.showMessageDialog(
            this,
            message,
            "练习完成",
            JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void updateStatus(TypingStatistics stats) {
        String status = String.format(
            "模式: %s | 字符: %d/%d | 准确率: %.1f%% | 速度: %d cpm",
            currentHandler.getModeName(),
            stats.getCorrectCharacters(),
            stats.getTotalCharacters(),
            stats.getAccuracy(),
            stats.getCharactersPerMinute()
        );
        statusLabel.setText(status);
    }
}