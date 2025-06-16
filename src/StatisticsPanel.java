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

        JLabel titleLabel = new JLabel("������ϰͳ��", JLabel.CENTER);
        titleLabel.setFont(new Font("΢���ź�", Font.BOLD, 18));
        add(titleLabel, BorderLayout.NORTH);

        statsArea = new JTextArea();
        statsArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(statsArea);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton refreshButton = new JButton("ˢ��ͳ��");
        refreshButton.addActionListener(e -> refreshStats());
        buttonPanel.add(refreshButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // ��ʼ����ͳ����Ϣ
        refreshStats();
    }

    private void refreshStats() {
        List<TypingRecord> records = new TextManager().loadTypingRecords();
        StringBuilder sb = new StringBuilder();
        sb.append("==== ������ϰͳ�� ====\n\n");

        sb.append("����ϰ����: ").append(records.size()).append("\n");

        double totalAccuracy = 0;
        int totalSpeed = 0;
        for (TypingRecord r : records) {
            totalAccuracy += r.getAccuracy();
            totalSpeed += r.getSpeed();
        }

        if (!records.isEmpty()) {
            sb.append(String.format("ƽ��׼ȷ��: %.1f%%\n", totalAccuracy / records.size()));
            sb.append(String.format("ƽ���ٶ�: %d �ַ�/����\n\n", totalSpeed / records.size()));
        }

        sb.append("==== ���5����ϰ ====\n\n");
        for (int i = 0; i < Math.min(5, records.size()); i++) {
            sb.append((i + 1)).append(". ").append(records.get(i).toString()).append("\n");
        }

        statsArea.setText(sb.toString());
    }

}