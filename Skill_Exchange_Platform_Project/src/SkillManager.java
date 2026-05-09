
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class SkillManager extends JFrame{
    int userId;
    JTable table;
    DefaultTableModel model;
    JComboBox<String> skillBox;
    JRadioButton teachBtn, learnBtn;

    public SkillManager(int userId){
        this.userId = userId;

        setTitle("Manage My Skills");
        setSize(600, 450);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));

        skillBox = new JComboBox<>();
        skillBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        loadSkillsIntoCombo();

        teachBtn = new JRadioButton("Teach");
        learnBtn = new JRadioButton("Learn");
        teachBtn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        learnBtn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        teachBtn.setSelected(true);

        ButtonGroup group = new ButtonGroup();
        group.add(teachBtn);
        group.add(learnBtn);

        JButton addBtn = new JButton("Add");
        addBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        addBtn.addActionListener(e -> addSkill());

        topPanel.add(new JLabel("Skill:"));
        topPanel.add(skillBox);
        topPanel.add(teachBtn);
        topPanel.add(learnBtn);
        topPanel.add(addBtn);

        add(topPanel, BorderLayout.NORTH);

        model = new DefaultTableModel();
        table = new JTable(model);
        table.setRowHeight(26);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));

        model.addColumn("Skill Name");
        model.addColumn("Type");

        loadMySkills();

        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));

        JButton deleteBtn = new JButton("Delete Selected");
        deleteBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        deleteBtn.addActionListener(e -> deleteSkill());

        JButton backBtn = new JButton("Back");
        backBtn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        backBtn.addActionListener(e ->{
            dispose();
            new Dashboard(userId);
        });

        bottomPanel.add(deleteBtn);
        bottomPanel.add(backBtn);
        add(bottomPanel, BorderLayout.SOUTH);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    void loadSkillsIntoCombo(){
        try{
            Connection con = DBConnection.getConnection();
            ResultSet rs = con.createStatement().executeQuery(
                    "SELECT skill_name FROM skills ORDER BY skill_name");
            while (rs.next()){
                skillBox.addItem(rs.getString("skill_name"));
            }
            con.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
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

            while (rs2.next()){
                model.addRow(new Object[]{
                        rs2.getString("skill_name"),
                        rs2.getString("type")
                });
            }
            con.close();
        }
        catch (Exception e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Could not load skills: " + e.getMessage());
        }
    }

    void addSkill(){
        String skill = (String) skillBox.getSelectedItem();

        if (skill == null){
            JOptionPane.showMessageDialog(this, "No skill selected.");
            return;
        }

        try{
            Connection con = DBConnection.getConnection();

            PreparedStatement pst1 = con.prepareStatement(
                    "SELECT skill_id FROM skills WHERE skill_name = ?");
            pst1.setString(1, skill);
            ResultSet rs = pst1.executeQuery();

            int skillId = 0;
            if (rs.next()){
                skillId = rs.getInt("skill_id");
            }
            if (teachBtn.isSelected()){

                PreparedStatement pst2 = con.prepareStatement(
                        "INSERT INTO teach_skills(user_id, skill_id) VALUES (?, ?)");
                pst2.setInt(1, userId);
                pst2.setInt(2, skillId);
                pst2.executeUpdate();
                JOptionPane.showMessageDialog(this, "Added to Teach.");

            } else {

                PreparedStatement pst2 = con.prepareStatement(
                        "INSERT INTO learn_skills(user_id, skill_id) VALUES (?, ?)");
                pst2.setInt(1, userId);
                pst2.setInt(2, skillId);
                pst2.executeUpdate();
                JOptionPane.showMessageDialog(this, "Added to Learn.");
            }

            con.close();
            loadMySkills();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Could not add skill: " + e.getMessage());
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
    public static void main(String[] args) {
        new Login();
    }
}
