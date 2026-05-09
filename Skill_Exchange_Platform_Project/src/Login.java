import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Login extends JFrame{
    JTextField emailField;
    JPasswordField passField;
    JButton loginBtn;
    JButton registerBtn;

    public Login(){

        setTitle("Login");
        setSize(500, 400);
        setLocationRelativeTo(null); // centres screen
        setLayout(null);

        // Title at top
        JLabel titleLabel = new JLabel("Welcome Back");
        titleLabel.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 26));
        titleLabel.setBounds(140, 40, 260, 40);
        add(titleLabel);

        JLabel subLabel = new JLabel("Please login to continue");
        subLabel.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 14));
        subLabel.setForeground(java.awt.Color.GRAY);
        subLabel.setBounds(155, 82, 220, 20);
        add(subLabel);

        //Email
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 14));
        emailLabel.setBounds(80, 130, 100, 25);
        add(emailLabel);

        emailField = new JTextField();
        emailField.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 14));
        emailField.setBounds(80, 158, 340, 35);
        add(emailField);

        //Password
        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 14));
        passLabel.setBounds(80, 208, 100, 25);
        add(passLabel);

        passField = new JPasswordField();
        passField.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 14));
        passField.setBounds(80, 236, 340, 35);
        add(passField);

        //Login button
        loginBtn = new JButton("Login");
        loginBtn.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 15));
        loginBtn.setBounds(80, 295, 160, 40);
        add(loginBtn);

        //Register button
        registerBtn = new JButton("Register");
        registerBtn.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 15));
        registerBtn.setBounds(260, 295, 160, 40);
        add(registerBtn);

        loginBtn.addActionListener(e -> loginUser());

        registerBtn.addActionListener(e ->{
            dispose();
            new Register();
        });

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void loginUser(){
        try{
            Connection con = DBConnection.getConnection();

            String query = "SELECT * FROM users WHERE email = ? AND password = ?";

            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, emailField.getText());
            pst.setString(2, new String(passField.getPassword()));

            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                int userId = rs.getInt("user_id");
                JOptionPane.showMessageDialog(this,
                        "Login Successful: " + rs.getString("full_name"));

                this.dispose();
                new Dashboard(userId);
            } else {
                JOptionPane.showMessageDialog(this, "Invalid Credentials");
            }
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }
    public static void main(String[] args) {
        new Login();
    }
}
