import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class Search extends JFrame {
    JTable table;
    DefaultTableModel model;
    JTextField searchField;
    int userId;

    public Search(int userId) {
        this.userId = userId;

        setTitle("Search");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel top = new JPanel();

        searchField = new JTextField(15);
        JButton searchBtn = new JButton("Search");

        top.add(new JLabel("Skill: "));
        top.add(searchField);
        top.add(searchBtn);

        add(top, BorderLayout.NORTH);

        model = new DefaultTableModel();
        table = new JTable(model);

        model.addColumn("User Name");

        add(new JScrollPane(table), BorderLayout.CENTER);

        JButton back = new JButton("Back");
        add(back, BorderLayout.SOUTH);

        searchBtn.addActionListener(e -> search());

        back.addActionListener(e -> {
            dispose();
            new Dashboard(userId);
        });
        setVisible(true);
    }

    void search(){
        try {
            Connection con = DBConnection.getConnection();

            String sql = "SELECT u.full_name FROM users u " +
                    "JOIN teach_skills ts ON u.user_id = ts.user_id " +
                    "JOIN skills s ON ts.skill_id = s.skill_id " +
                    "WHERE s.skill_name LIKE ?";

            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, "%" + searchField.getText() + "%");

            ResultSet rs = pst.executeQuery();
            model.setRowCount(0);

            while (rs.next()){
                model.addRow(new Object[]{
                        rs.getString("full_name")
                });
            }
            con.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        new Login();
    }
}
