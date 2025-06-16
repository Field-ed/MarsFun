import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

class CustomTextPanel extends JPanel {
    private TextManager textManager;
    private JTextField nameField;
    private JTextArea contentArea;
    private JList<String> textList;
    private DefaultListModel<String> listModel;

    public CustomTextPanel(TextManager textManager) {
        this.textManager = textManager;
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 左侧字帖列表
        listModel = new DefaultListModel<>();
        textList = new JList<>(listModel);
        textList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        textList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                loadSelectedText();
            }
        });

        JScrollPane listScroll = new JScrollPane(textList);
        listScroll.setPreferredSize(new Dimension(200, 0));
        listScroll.setBorder(BorderFactory.createTitledBorder("字帖列表"));

        // 左侧按钮面板
        JPanel leftButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JButton refreshButton = new JButton("刷新");
        refreshButton.addActionListener(e -> refreshTextList());

        JButton deleteButton = new JButton("删除");
        deleteButton.addActionListener(e -> deleteSelectedText());

        leftButtonPanel.add(refreshButton);
        leftButtonPanel.add(deleteButton);

        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.add(listScroll, BorderLayout.CENTER);
        leftPanel.add(leftButtonPanel, BorderLayout.SOUTH);

        add(leftPanel, BorderLayout.WEST);

        // 右侧编辑区
        JPanel rightPanel = new JPanel(new BorderLayout(0, 10));
        rightPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));

        JPanel namePanel = new JPanel(new BorderLayout());
        namePanel.add(new JLabel("字帖名称:"), BorderLayout.WEST);
        nameField = new JTextField();
        namePanel.add(nameField, BorderLayout.CENTER);

        contentArea = new JTextArea();
        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);
        JScrollPane contentScroll = new JScrollPane(contentArea);
        contentScroll.setBorder(BorderFactory.createTitledBorder("字帖内容"));

        JPanel rightButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton newButton = new JButton("新建");
        newButton.addActionListener(e -> newText());

        JButton saveButton = new JButton("保存");
        saveButton.addActionListener(e -> saveText());

        rightButtonPanel.add(newButton);
        rightButtonPanel.add(saveButton);

        rightPanel.add(namePanel, BorderLayout.NORTH);
        rightPanel.add(contentScroll, BorderLayout.CENTER);
        rightPanel.add(rightButtonPanel, BorderLayout.SOUTH);

        add(rightPanel, BorderLayout.CENTER);

        // 初始加载字帖列表
        refreshTextList();
    }

    private void refreshTextList() {
        listModel.clear();
        List<String> textNames = textManager.getTextNames();
        for (String name : textNames) {
            listModel.addElement(name);
        }
    }

    private void loadSelectedText() {
        String selectedText = textList.getSelectedValue();
        if (selectedText != null) {
            nameField.setText(selectedText);
            contentArea.setText(textManager.getTextContent(selectedText));
        }
    }

    private void newText() {
        nameField.setText("");
        contentArea.setText("");
    }

    private void saveText() {
        String name = nameField.getText().trim();
        String content = contentArea.getText();

        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "请输入字帖名称", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (content.isEmpty()) {
            JOptionPane.showMessageDialog(this, "请输入字帖内容", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }

        textManager.saveText(name, content);
        refreshTextList();
        JOptionPane.showMessageDialog(this, "字帖保存成功!", "成功", JOptionPane.INFORMATION_MESSAGE);
    }

    private void deleteSelectedText() {
        String selectedText = textList.getSelectedValue();
        if (selectedText != null) {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "确定要删除 \"" + selectedText + "\" 吗?",
                    "确认删除", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                textManager.deleteText(selectedText);
                refreshTextList();
                newText();
            }
        }
    }
}
