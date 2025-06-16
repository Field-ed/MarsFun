import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

class StatisticsPanel extends JPanel {
    private JTextArea statsArea;

    public StatisticsPanel() {
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel("打字练习统计", JLabel.CENTER);
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 18));
        add(titleLabel, BorderLayout.NORTH);

        statsArea = new JTextArea();
        statsArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(statsArea);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton refreshButton = new JButton("刷新统计");
        refreshButton.addActionListener(e -> refreshStats());
        buttonPanel.add(refreshButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // 初始加载统计信息
        refreshStats();
    }

    private void refreshStats() {
        List<TypingRecord> records = new TextManager().loadTypingRecords();
        StringBuilder sb = new StringBuilder();
        sb.append("==== 打字练习统计 ====\n\n");

        sb.append("总练习次数: ").append(records.size()).append("\n");

        double totalAccuracy = 0;
        int totalSpeed = 0;
        for (TypingRecord r : records) {
            totalAccuracy += r.getAccuracy();
            totalSpeed += r.getSpeed();
        }

        if (!records.isEmpty()) {
            sb.append(String.format("平均准确率: %.1f%%\n", totalAccuracy / records.size()));
            sb.append(String.format("平均速度: %d 字符/分钟\n\n", totalSpeed / records.size()));
        }

        sb.append("==== 最近5次练习 ====\n\n");
        for (int i = 0; i < Math.min(5, records.size()); i++) {
            sb.append((i + 1)).append(". ").append(records.get(i).toString()).append("\n");
        }

        statsArea.setText(sb.toString());
    }

}