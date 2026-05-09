import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class MatchSystem extends JFrame{
    JTable table;
    DefaultTableModel model;
    int userId;

    public MatchSystem(int userId){
        this.userId = userId;

        setTitle("Matches");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        model = new DefaultTableModel();

        table = new JTable(model);
        table.setRowHeight(28);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));

        model.addColumn("Teacher Name");
        model.addColumn("Skill");
        model.addColumn("Contact Email");

        add(new JScrollPane(table), BorderLayout.CENTER);

        JButton back = new JButton("Back");
        back.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        add(back, BorderLayout.SOUTH);

        back.addActionListener(e -> {
            dispose();
            new Dashboard(userId);
        });

        loadMatches();
        setVisible(true);
    }

    void loadMatches() {
        model.setRowCount(0);
        try {
            Connection con = DBConnection.getConnection();

            String sql =
                    "SELECT u.full_name, u.email, s.skill_name " +
                            "FROM users u " +
                            "JOIN teach_skills ts ON u.user_id = ts.user_id " +
                            "JOIN skills s ON ts.skill_id = s.skill_id " +
                            "JOIN learn_skills ls ON ts.skill_id = ls.skill_id " +
                            "WHERE ls.user_id = ?";

            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, userId);

            ResultSet rs = pst.executeQuery();
            model.setRowCount(0);

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getString("full_name"),
                        rs.getString("skill_name"),
                        rs.getString("email")
                });
            }
            con.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    public static void main(String[] args){
        new Login();
    }
}
