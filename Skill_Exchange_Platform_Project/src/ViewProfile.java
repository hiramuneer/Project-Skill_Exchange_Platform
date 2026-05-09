import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class ViewProfile extends JFrame{
    int userId;
    JTable table;
    DefaultTableModel model;

    public ViewProfile(int userId){
        this.userId = userId;

        setTitle("My Profile");
        setSize(550, 550);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel infoPanel = new JPanel(new GridLayout(4, 2, 8, 8));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(16, 16, 8, 16));

        Font bold  = new Font("Segoe UI", Font.BOLD,  14);
        Font plain = new Font("Segoe UI", Font.PLAIN, 14);

        JLabel lblName     = new JLabel("Name:");
        JLabel lblEmail    = new JLabel("Email:");
        JLabel lblDept     = new JLabel("Department:");
        JLabel lblSemester = new JLabel("Semester:");

        lblName.setFont(bold);
        lblEmail.setFont(bold);
        lblDept.setFont(bold);
        lblSemester.setFont(bold);

        JLabel valName     = new JLabel();
        JLabel valEmail    = new JLabel();
        JLabel valDept     = new JLabel();
        JLabel valSemester = new JLabel();

        valName.setFont(plain);
        valEmail.setFont(plain);
        valDept.setFont(plain);
        valSemester.setFont(plain);

        infoPanel.add(lblName);     infoPanel.add(valName);
        infoPanel.add(lblEmail);    infoPanel.add(valEmail);
        infoPanel.add(lblDept);     infoPanel.add(valDept);
        infoPanel.add(lblSemester); infoPanel.add(valSemester);

        add(infoPanel, BorderLayout.NORTH);

        try{
            Connection con = DBConnection.getConnection();

            PreparedStatement pst = con.prepareStatement(
                    "SELECT full_name, email, department, semester " +
                            "FROM users WHERE user_id = ?");
            pst.setInt(1, userId);

            ResultSet rs = pst.executeQuery();

            if (rs.next()){
                valName.setText(rs.getString("full_name"));
                valEmail.setText(rs.getString("email"));
                valDept.setText(rs.getString("department"));
                valSemester.setText(String.valueOf(rs.getInt("semester")));
            }

            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        JLabel skillsTitle = new JLabel("  My Skills");
        skillsTitle.setFont(bold);
        skillsTitle.setBorder(BorderFactory.createEmptyBorder(4, 12, 4, 0));

        model = new DefaultTableModel();
        table = new JTable(model);
        table.setRowHeight(26);
        table.setFont(plain);
        table.getTableHeader().setFont(bold);

        model.addColumn("Skill Name");
        model.addColumn("Type");

        loadMySkills();

        JPanel centrePanel = new JPanel(new BorderLayout());
        centrePanel.add(skillsTitle,            BorderLayout.NORTH);
        centrePanel.add(new JScrollPane(table), BorderLayout.CENTER);

        add(centrePanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));

        JButton deleteBtn = new JButton("Delete Selected Skill");
        deleteBtn.setFont(bold);
        deleteBtn.addActionListener(e -> deleteSkill());

        JButton backBtn = new JButton("Back to Dashboard");
        backBtn.setFont(plain);
        backBtn.addActionListener(e -> {
            dispose();
            new Dashboard(userId);
        });

        bottomPanel.add(deleteBtn);
        bottomPanel.add(backBtn);
        add(bottomPanel, BorderLayout.SOUTH);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    void loadMySkills(){
        model.setRowCount(0);
        try{
            Connection con = DBConnection.getConnection();

            PreparedStatement pst1 = con.prepareStatement(
                    "SELECT s.skill_name, 'Teach' AS type " +
                            "FROM skills s " +
                            "JOIN teach_skills ts ON s.skill_id = ts.skill_id " +
                            "WHERE ts.user_id = ?");
            pst1.setInt(1, userId);
            ResultSet rs1 = pst1.executeQuery();

            while (rs1.next()) {
                model.addRow(new Object[]{
                        rs1.getString("skill_name"),
                        rs1.getString("type")
                });
            }

            PreparedStatement pst2 = con.prepareStatement(
                    "SELECT s.skill_name, 'Learn' AS type " +
                            "FROM skills s " +
                            "JOIN learn_skills ls ON s.skill_id = ls.skill_id " +
                            "WHERE ls.user_id = ?");
            pst2.setInt(1, userId);
            ResultSet rs2 = pst2.executeQuery();

            while (rs2.next()) {
                model.addRow(new Object[]{
                        rs2.getString("skill_name"),
                        rs2.getString("type")
                });
            }

            con.close();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Could not load skills: " + e.getMessage());
        }
    }

    void deleteSkill() {
        int selected = table.getSelectedRow();

        if (selected == -1) {
            JOptionPane.showMessageDialog(this, "Please select a skill to delete.");
            return;
        }

        String skillName = (String) model.getValueAt(selected, 0);
        String type      = (String) model.getValueAt(selected, 1);

        try {
            Connection con = DBConnection.getConnection();

            PreparedStatement pst1 = con.prepareStatement(
                    "SELECT skill_id FROM skills WHERE skill_name = ?");
            pst1.setString(1, skillName);
            ResultSet rs = pst1.executeQuery();

            int skillId = 0;
            if (rs.next()) {
                skillId = rs.getInt("skill_id");
            }

            String sql = type.equals("Teach")
                    ? "DELETE FROM teach_skills WHERE user_id = ? AND skill_id = ?"
                    : "DELETE FROM learn_skills WHERE user_id = ? AND skill_id = ?";

            PreparedStatement pst2 = con.prepareStatement(sql);
            pst2.setInt(1, userId);
            pst2.setInt(2, skillId);
            pst2.executeUpdate();

            JOptionPane.showMessageDialog(this, skillName + " deleted.");

            con.close();
            loadMySkills();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Could not delete: " + e.getMessage());
        }
    }
}
