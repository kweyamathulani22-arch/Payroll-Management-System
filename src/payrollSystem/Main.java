package payrollSystem;

// The program starts here. It switches on the Nimbus look and shows the login screen.
// (While testing, you can also run a single screen with Shift+F6; if nobody is
// logged in, the screen sends you to the login window first.)
public class Main {

    public static void main(String[] args) {
        try {
            // look for the Nimbus look and feel and use it
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) {
            // if it fails we just keep the normal look
        }
        // Swing windows must be created on the event thread
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new login().setVisible(true);
            }
        });
    }
}
