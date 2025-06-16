import javax.swing.*;

public class TypingApp {
    public static void main(String[] args) {
			try {
				org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper.launchBeautyEyeLNF();
			} catch (Exception e) {
				e.printStackTrace();
			}
        

        SwingUtilities.invokeLater(() -> {
            System.out.println("正在创建主框架...");
            MainFrame mainFrame = new MainFrame();
            System.out.println("主框架创建完成，设置可见...");
            mainFrame.setVisible(true);
            System.out.println("主框架应已显示");
        });
    }
}







