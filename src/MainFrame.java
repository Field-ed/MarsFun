import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

class MainFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private PracticePanel practicePanel;
    private CustomTextPanel customTextPanel;
    private StatisticsPanel statisticsPanel;
    private TextManager textManager;

    public MainFrame() {
        try{
            textManager = new TextManager();
            initComponents();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initComponents() {
        setTitle("MarsFun");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // 创建菜单栏
        JMenuBar menuBar = createMenuBar();
        setJMenuBar(menuBar);

        // 创建各个面板
        JPanel welcomePanel = createWelcomePanel();
        practicePanel = new PracticePanel(textManager);
        customTextPanel = new CustomTextPanel(textManager);
        statisticsPanel = new StatisticsPanel();

        // 添加到主面板
        mainPanel.add(welcomePanel, "welcome");
        mainPanel.add(practicePanel, "practice");
        mainPanel.add(customTextPanel, "custom");
        mainPanel.add(statisticsPanel, "statistics");

        cardLayout.show(mainPanel, "welcome");
        add(mainPanel);
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("菜单");

        JMenuItem homeItem = new JMenuItem("首页");
        homeItem.addActionListener(e -> cardLayout.show(mainPanel, "welcome"));

        JMenuItem practiceItem = new JMenuItem("打字练习");
        practiceItem.addActionListener(e -> cardLayout.show(mainPanel, "practice"));

        JMenuItem customItem = new JMenuItem("自定义文本");
        customItem.addActionListener(e -> cardLayout.show(mainPanel, "custom"));

        JMenuItem statsItem = new JMenuItem("统计信息");
        statsItem.addActionListener(e -> cardLayout.show(mainPanel, "statistics"));

        JMenuItem exitItem = new JMenuItem("退出");
        exitItem.addActionListener(e -> System.exit(0));

        menu.add(homeItem);
        menu.add(practiceItem);
        menu.add(customItem);
        menu.add(statsItem);
        menu.addSeparator();
        menu.add(exitItem);

        menuBar.add(menu);
        return menuBar;
    }

    private JPanel createWelcomePanel() {
        JPanel panel = new JPanel(new BorderLayout());

        JLabel titleLabel = new JLabel("MarsFun", JLabel.CENTER);
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 35));
        panel.add(titleLabel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(50, 100, 50, 100));

        JButton practiceButton = new JButton("开始练习");
        practiceButton.setFont(new Font("微软雅黑", Font.PLAIN, 18));
        practiceButton.addActionListener(e -> cardLayout.show(mainPanel, "practice"));

        JButton customButton = new JButton("自定义文本");
        customButton.setFont(new Font("微软雅黑", Font.PLAIN, 18));
        customButton.addActionListener(e -> cardLayout.show(mainPanel, "custom"));

        JButton statsButton = new JButton("查看统计");
        statsButton.setFont(new Font("微软雅黑", Font.PLAIN, 18));
        statsButton.addActionListener(e -> cardLayout.show(mainPanel, "statistics"));

        centerPanel.add(practiceButton);
        centerPanel.add(customButton);
        centerPanel.add(statsButton);

        panel.add(centerPanel, BorderLayout.CENTER);
        return panel;
    }
}