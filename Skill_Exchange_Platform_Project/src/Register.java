import java.sql.Connection;
import java.sql.PreparedStatement;
import javax.swing.*;

public class Register extends JFrame{
    JTextField nameField;
    JTextField emailField;
    JPasswordField passField;
    JButton registerBtn;
    JButton backBtn;

    public Register(){
        setTitle("Register");
        setSize(500, 460);
        setLocationRelativeTo(null); // screen on center
        setLayout(null);

        //Title
        JLabel titleLabel = new JLabel("Create Account");
        titleLabel.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 26));
        titleLabel.setBounds(140, 40, 260, 40);
        add(titleLabel);

        //Name
        JLabel nameLabel = new JLabel("Full Name:");
        nameLabel.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 14));
        nameLabel.setBounds(80, 125, 120, 25);
        add(nameLabel);

        nameField = new JTextField();
        nameField.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 14));
        nameField.setBounds(80, 153, 340, 35);
        add(nameField);

        //Email
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 14));
        emailLabel.setBounds(80, 203, 120, 25);
        add(emailLabel);

        emailField = new JTextField();
        emailField.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 14));
        emailField.setBounds(80, 231, 340, 35);
        add(emailField);

        //Password
        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 14));
        passLabel.setBounds(80, 281, 120, 25);
        add(passLabel);

        passField = new JPasswordField();
        passField.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 14));
        passField.setBounds(80, 309, 340, 35);
        add(passField);

        //Register button
        registerBtn = new JButton("Register");
        registerBtn.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 15));
        registerBtn.setBounds(80, 368, 160, 40);
        add(registerBtn);

        //Back button
        backBtn = new JButton("Back to Login");
        backBtn.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 15));
        backBtn.setBounds(260, 368, 160, 40);
        add(backBtn);

        registerBtn.addActionListener(e -> registerUser());

        backBtn.addActionListener(e ->{
            dispose();
            new Login();
        });

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void registerUser(){
        try{
            Connection con = DBConnection.getConnection();

            String query = "INSERT INTO users(full_name, email, password) VALUES (?, ?, ?)";

            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, nameField.getText());
            pst.setString(2, emailField.getText());
            pst.setString(3, new String(passField.getPassword()));

            pst.executeUpdate();

            JOptionPane.showMessageDialog(this, "User Registered! Please log in.");

            dispose();
            new Login();
            con.close();
        }
        catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }
    public static void main(String[] args) {
        new Register();
    }
}
