import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

class TextManager {
    private static final String DATA_DIR = "data";
    private static final String TEXT_DIR = DATA_DIR + File.separator + "texts";

    private static final String RECORD_FILE = DATA_DIR + File.separator + "typing_records.dat";

    public void saveTypingRecord(TypingRecord record) {
        List<TypingRecord> records = loadTypingRecords();
        records.add(0, record);  // ���µ�������ǰ
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(RECORD_FILE))) {
            out.writeObject(records);
        } catch (IOException e) {
            System.err.println("�����¼ʧ��: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public List<TypingRecord> loadTypingRecords() {
        File file = new File(RECORD_FILE);
        if (!file.exists()) return new ArrayList<>();
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            return (List<TypingRecord>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("���ؼ�¼ʧ��: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public TextManager() {
        initDataDirectories();
        if (getTextNames().isEmpty()) {
            createSampleTexts();
        }
    }

    private void initDataDirectories() {
        File dataDir = new File(DATA_DIR);
        if (!dataDir.exists()) {
            dataDir.mkdir();
        }

        File textDir = new File(TEXT_DIR);
        if (!textDir.exists()) {
            textDir.mkdir();
        }
    }

    private void createSampleTexts() {
        saveText("Java��̻���", "Java��һ�ֹ㷺ʹ�õļ����������ԣ�ӵ�п�ƽ̨��������󡢷��ͱ�̵����ԣ��㷺Ӧ������ҵ��WebӦ�ÿ������ƶ�Ӧ�ÿ�����");
        saveText("��������ҹ", "������ˮ����ƽ���������¹������������沨ǧ����δ�������������������ת�Ʒ��飬���ջ��ֽ�������");
        saveText("Ӣ����ϰ", "The quick brown fox jumps over the lazy dog. This pangram contains all the letters of the English alphabet.");
    }

    public List<String> getTextNames() {
        List<String> result = new ArrayList<>();
        File dir = new File(TEXT_DIR);
        if (dir.exists() && dir.isDirectory()) {
            File[] files = dir.listFiles((d, name) -> name.endsWith(".txt"));
            if (files != null) {
                for (File file : files) {
                    result.add(file.getName().replace(".txt", ""));
                }
            }
        }
        return result;
    }

    public String getTextContent(String name) {
        String fileName = TEXT_DIR + File.separator + name + ".txt";
        StringBuilder content = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (IOException e) {
            System.err.println("��ȡ�ļ�ʧ��: " + e.getMessage());
        }

        return content.toString();
    }

    public void saveText(String name, String content) {
        String fileName = TEXT_DIR + File.separator + name + ".txt";

        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write(content);
        } catch (IOException e) {
            System.err.println("�����ļ�ʧ��: " + e.getMessage());
        }
    }

    public void deleteText(String name) {
        String fileName = TEXT_DIR + File.separator + name + ".txt";
        File file = new File(fileName);
        if (file.exists()) {
            boolean deleted = file.delete();
            if (!deleted) {
                System.err.println("ɾ���ļ�ʧ��: " + fileName);
            }
        }
    }
}