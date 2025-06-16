import java.io.Serializable;

public class TypingRecord implements Serializable {
    private String date;
    private String textName;
    private double accuracy;
    private int speed;

    public TypingRecord(String date, String textName, double accuracy, int speed) {
        this.date = date;
        this.textName = textName;
        this.accuracy = accuracy;
        this.speed = speed;
    }

    public String getDate() {
        return date;
    }

    public String getTextName() {
        return textName;
    }

    public double getAccuracy() {
        return accuracy;
    }

    public int getSpeed() {
        return speed;
    }

    @Override
    public String toString() {
        return String.format("%s - 《%s》 - 准确率: %.1f%% - 速度: %d 字符/分钟", date, textName, accuracy, speed);
    }
}
