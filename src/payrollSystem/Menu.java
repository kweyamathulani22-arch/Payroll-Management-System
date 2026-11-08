/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package payrollSystem;



import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.sql.SQLException;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 *
 * @author DELL
 */
public class Menu extends javax.swing.JFrame {

    /**
     * Creates new form Menu
     */
    public Menu() {
        initComponents();
        // a user must be logged in to see this screen
        if (!Database.requireLogin(this)) {
            return;
        }
        // The designer gave this window an absolute layout (null), so the panels stayed small
        // in the top left corner when the window was big. I change it to a BorderLayout:
        // the red menu on the left and the white area on the right, both stretch with the window.
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(jPanel1, BorderLayout.WEST);          // red menu buttons

        JPanel rightSide = new JPanel(new BorderLayout());
        rightSide.setBackground(Color.WHITE);
        jPanel3.setPreferredSize(new Dimension(100, 50));           // thin bar at the top with the user name
        rightSide.add(jPanel3, BorderLayout.NORTH);
        JLabel welcome = new JLabel(welcomeText(), SwingConstants.CENTER);   // welcome message in the middle
        welcome.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        rightSide.add(welcome, BorderLayout.CENTER);
        getContentPane().add(rightSide, BorderLayout.CENTER);

        // show the name of the person who is logged in (it said "Administrator" before)
        jLabel1.setText(Database.currentUserName);

        // Same as the other screens: the menu is a form of a fixed size, in the middle of the window
        // (grey around it), and the window opens maximised.
        getContentPane().setPreferredSize(new Dimension(880, 560));
        Util.fitScreen(this, true);
    }

   
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        employee3 = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        payrollProcessing = new javax.swing.JButton();
        dashboard1 = new javax.swing.JButton();
        payslip = new javax.swing.JButton();
        employee2 = new javax.swing.JButton();
        reports = new javax.swing.JButton();
        settings = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();

        employee3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        employee3.setText("Employees");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(null);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel2.setBackground(new java.awt.Color(139, 0, 0));

        payrollProcessing.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        payrollProcessing.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/payrollcal.jpg.jpeg"))); // NOI18N
        payrollProcessing.setText("Payroll ");
        payrollProcessing.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                payrollProcessingActionPerformed(evt);
            }
        });

        dashboard1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        dashboard1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/dashboard_icon.jpg.jpeg"))); // NOI18N
        dashboard1.setText("Dashboard");
        dashboard1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dashboard1ActionPerformed(evt);
            }
        });

        payslip.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        payslip.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/payslip.png"))); // NOI18N
        payslip.setText("Payslip");
        payslip.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                payslipActionPerformed(evt);
            }
        });

        employee2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        employee2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/employeeIcon.jpg.jpeg"))); // NOI18N
        employee2.setText("Employees");
        employee2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                employee2ActionPerformed(evt);
            }
        });

        reports.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        reports.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/reportIcon.jpg.jpeg"))); // NOI18N
        reports.setText("Reports");
        reports.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reportsActionPerformed(evt);
            }
        });

        settings.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        settings.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/settingIcon.jpg.jpeg"))); // NOI18N
        settings.setText("Settings");
        settings.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                settingsActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(settings, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(reports, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(payrollProcessing, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(employee2, javax.swing.GroupLayout.DEFAULT_SIZE, 165, Short.MAX_VALUE))
                    .addComponent(payslip, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dashboard1, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(9, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(dashboard1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(employee2, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(payrollProcessing)
                .addGap(9, 9, 9)
                .addComponent(payslip)
                .addGap(8, 8, 8)
                .addComponent(reports, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(settings)
                .addContainerGap(30, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel2, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel1);
        jPanel1.setBounds(0, 0, 180, 300);

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/New_profile_icon.png"))); // NOI18N
        jLabel1.setText("Administrator");

        jPanel4.setBackground(new java.awt.Color(0, 0, 0));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 148, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 13, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addContainerGap(275, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel3);
        jPanel3.setBounds(180, 0, 260, 300);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void settingsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_settingsActionPerformed
        // open the settings screen and close the menu
        new Setting().setVisible(true);
        dispose();
    }//GEN-LAST:event_settingsActionPerformed

    private void reportsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reportsActionPerformed
        // open the reports screen and close the menu
        new Reports().setVisible(true);
        dispose();
    }//GEN-LAST:event_reportsActionPerformed

    private void employee2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_employee2ActionPerformed
        // open the employees screen and close the menu
        new employees().setVisible(true);
        dispose();
    }//GEN-LAST:event_employee2ActionPerformed

    private void payslipActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_payslipActionPerformed
        // open the payslips screen and close the menu
        new payslip().setVisible(true);
        dispose();
    }//GEN-LAST:event_payslipActionPerformed

    private void dashboard1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dashboard1ActionPerformed
        // open the dashboard and close the menu
        new dashboard().setVisible(true);
        dispose();
    }//GEN-LAST:event_dashboard1ActionPerformed

    private void payrollProcessingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_payrollProcessingActionPerformed
        // open payroll processing and close the menu
        new PayrollProcessing().setVisible(true);
        dispose();
    }//GEN-LAST:event_payrollProcessingActionPerformed

    // ----------------- my own code (the database part) -----------------
    // builds the welcome text (name of the user and the company name from Settings)
    private String welcomeText() {
        String company = "";
        try {
            company = Database.loadSettings().companyName;
        } catch (SQLException ex) {
            // if the database fails we just show no company name
        }
        String second = company.isEmpty()
                ? "Open Settings to type your company name and address (they are printed on the payslips)."
                : Util.html(company);
        return "<html><div style='text-align:center'><span style='font-size:26pt'>Welcome, "
                + Util.html(Database.currentUserName) + "</span><br><br><span style='color:gray'>"
                + second + "</span></div></html>";
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Menu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Menu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Menu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Menu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Menu().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton dashboard1;
    private javax.swing.JButton employee2;
    private javax.swing.JButton employee3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JButton payrollProcessing;
    private javax.swing.JButton payslip;
    private javax.swing.JButton reports;
    private javax.swing.JButton settings;
    // End of variables declaration//GEN-END:variables
}
