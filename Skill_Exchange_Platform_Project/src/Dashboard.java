import javax.swing.*;
import java.awt.*;

public class Dashboard extends JFrame{
    int userId;

    public Dashboard(int userId){
        this.userId = userId;

        setTitle("Skill Exchange Platform");
        setSize(900, 640);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Main Panel
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setLayout(null);

        // Heading
        JLabel heading = new JLabel("SKILL EXCHANGE PLATFORM");
        heading.setFont(new Font("Segoe UI", Font.BOLD, 34));
        heading.setForeground(new Color(0, 51, 102));
        heading.setBounds(190, 50, 550, 50);

        mainPanel.add(heading);

        // Buttons
        JButton btnProfile = createButton(
                "View Profile",
                new Color(210, 230, 255));

        JButton btnSkills = createButton(
                "Manage Skills",
                new Color(180, 180, 180));

        JButton btnSearch = createButton(
                "Search",
                new Color(190, 220, 235));

        JButton btnMatch = createButton(
                "Match Result",
                new Color(240, 220, 210));

        JButton btnLogout = createButton(
                "Log Out",
                new Color(120, 210, 130));

        //Button Positions
        btnProfile.setBounds(250, 140, 400, 55);
        btnSkills.setBounds(250, 220, 400, 55);
        btnSearch.setBounds(250, 300, 400, 55);
        btnMatch.setBounds(250, 380, 400, 55);
        btnLogout.setBounds(250, 460, 400, 55);

        //Add Buttons
        mainPanel.add(btnProfile);
        mainPanel.add(btnSkills);
        mainPanel.add(btnSearch);
        mainPanel.add(btnMatch);
        mainPanel.add(btnLogout);

        //Button Actions
        btnProfile.addActionListener(e ->{
            dispose();
            new ViewProfile(userId);
        });

        btnSkills.addActionListener(e ->{
            dispose();
            new SkillManager(userId);
        });

        btnSearch.addActionListener(e ->{
            dispose();
            new Search(userId);
        });

        btnMatch.addActionListener(e ->{
            dispose();
            new MatchSystem(userId);
        });

        btnLogout.addActionListener(e ->{
            dispose();
            new Login();
        });

        add(mainPanel);
        setVisible(true);
    }

    // Button Design
    JButton createButton(String text, Color color) {
        JButton btn = new JButton(text);

        btn.setBackground(color);
        btn.setForeground(Color.BLACK);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 24));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder());
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    public static void main(String[] args){
        try {
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
        }
        catch (Exception e){
            e.printStackTrace();
        }
        new Dashboard(1);
    }
}
