import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class LeagueGUI {
    // تعريف المحرك الرئيسي للبرنامج
    private LeagueSystem league = new LeagueSystem();
    private JFrame frame;

    public LeagueGUI() {
        // 1. تحميل البيانات عند بدء التشغيل
        league.loadData();

        // 2. إعداد النافذة الرئيسية
        frame = new JFrame("Egyptian League Management System");
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout(15, 15));
        frame.setLocationRelativeTo(null); 

        // 3. العنوان (Header)
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(41, 128, 185)); 
        JLabel titleLabel = new JLabel("Egyptian League System");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        headerPanel.add(titleLabel);
        frame.add(headerPanel, BorderLayout.NORTH);

        // 4. لوحة الأزرار (Main Menu) - شبكة من عمودين
        JPanel menuPanel = new JPanel(new GridLayout(0, 2, 15, 15));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        // تعريف الأزرار
        JButton btnViewTeams = createStyledButton("Display All Teams");
        JButton btnAddTeam = createStyledButton("Add New Team");
        
        JButton btnViewTeamPlayers = createStyledButton("View Team Players");
        JButton btnAddPlayer = createStyledButton("Add Player to Team");
        
        JButton btnViewMatches = createStyledButton("Display All Matches");
        JButton btnAddMatch = createStyledButton("Add New Match");
        
        JButton btnTopScorers = createStyledButton("Top 3 Scorers");
        JButton btnTopGKs = createStyledButton("Top 3 Goalkeepers");
        
        JButton btnSaveExit = createStyledButton("Save & Exit Program");
        btnSaveExit.setBackground(new Color(192, 57, 43)); 
        btnSaveExit.setForeground(Color.WHITE);

        // إضافة الأزرار للوحة
        menuPanel.add(btnViewTeams);
        menuPanel.add(btnAddTeam);
        menuPanel.add(btnViewTeamPlayers);
        menuPanel.add(btnAddPlayer);
        menuPanel.add(btnViewMatches);
        menuPanel.add(btnAddMatch);
        menuPanel.add(btnTopScorers);
        menuPanel.add(btnTopGKs);
        menuPanel.add(btnSaveExit);

        frame.add(menuPanel, BorderLayout.CENTER);

        // --- برمجة الأزرار (Action Listeners) ---

        // 1. عرض كل الفرق
        btnViewTeams.addActionListener(e -> showScrollableMessage(league.getAllTeamsString(), "All Teams"));

        // 2. إضافة فريق جديد
        btnAddTeam.addActionListener(e -> {
            JTextField nameField = new JTextField();
            JTextField idField = new JTextField();
            JTextField captainField = new JTextField();

            Object[] message = {
                "Team Name:", nameField,
                "Team ID:", idField,
                "Captain Name:", captainField
            };

            int option = JOptionPane.showConfirmDialog(frame, message, "Add New Team", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                String name = nameField.getText().trim();
                String id = idField.getText().trim();
                String captain = captainField.getText().trim();

                if (name.isEmpty() || id.isEmpty()) return;

                if (league.findTeamByID(id) != null) {
                    JOptionPane.showMessageDialog(frame, "Error: Team ID already exists!", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    league.addTeam(new Team(name, id, captain));
                    JOptionPane.showMessageDialog(frame, "Team '" + name + "' added successfully!");
                }
            }
        });

        // 3. عرض لاعبي فريق معين
        btnViewTeamPlayers.addActionListener(e -> {
            String teamId = JOptionPane.showInputDialog(frame, "Enter Team ID:", "View Players", JOptionPane.QUESTION_MESSAGE);
            if (teamId == null || teamId.trim().isEmpty()) return;
            
            Team t = league.findTeamByID(teamId);
            if (t == null) {
                JOptionPane.showMessageDialog(frame, "Team not found!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (t.getPlayers().isEmpty()) {
                JOptionPane.showMessageDialog(frame, "No players found in this team.", "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            StringBuilder sb = new StringBuilder("=== Players of " + t.getName() + " ===\n\n");
            for (Player p : t.getPlayers()) {
                sb.append("Name: ").append(p.getName())
                  .append(" | ID: ").append(p.getPlayerID())
                  .append("\nPosition: ").append(p.getPosition())
                  .append(" | Number: ").append(p.getNumber())
                  .append(" | Age: ").append(p.getAge())
                  .append("\nGoals: ").append(p.getScore());
                  
                if (p instanceof Goalkeeper) {
                    sb.append(" | Conceded: ").append(((Goalkeeper) p).getGoalsConceded());
                }
                sb.append("\n-----------------------------\n");
            }
            showScrollableMessage(sb.toString(), t.getName() + " Players");
        });

        // 4. إضافة لاعب لفريق
        btnAddPlayer.addActionListener(e -> {
            String teamId = JOptionPane.showInputDialog(frame, "Enter Team ID to add player to:", "Add Player", JOptionPane.QUESTION_MESSAGE);
            if (teamId == null || teamId.trim().isEmpty()) return;
            
            Team t = league.findTeamByID(teamId);
            if (t == null) {
                JOptionPane.showMessageDialog(frame, "Team not found!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            JTextField nameField = new JTextField();
            JTextField idField = new JTextField();
            JTextField numField = new JTextField();
            JTextField ageField = new JTextField();
            String[] positions = {"Forward", "Midfielder", "Defender", "Goalkeeper"};
            JComboBox<String> posBox = new JComboBox<>(positions);

            Object[] message = {
                "Player Name:", nameField,
                "Player ID:", idField,
                "Shirt Number:", numField,
                "Age:", ageField,
                "Position:", posBox
            };

            int option = JOptionPane.showConfirmDialog(frame, message, "Add Player to " + t.getName(), JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                try {
                    String name = nameField.getText().trim();
                    String pid = idField.getText().trim();
                    int num = Integer.parseInt(numField.getText().trim());
                    int age = Integer.parseInt(ageField.getText().trim());
                    String pos = (String) posBox.getSelectedItem();

                    Player p;
                    switch (pos) {
                        case "Forward": p = new Forward(name, pid, num, t.getName(), age); break;
                        case "Midfielder": p = new Midfielder(name, pid, num, t.getName(), age); break;
                        case "Defender": p = new Defender(name, pid, num, t.getName(), age); break;
                        case "Goalkeeper": p = new Goalkeeper(name, pid, num, t.getName(), age); break;
                        default: p = new Forward(name, pid, num, t.getName(), age);
                    }

                    if (t.addPlayer(p)) {
                        league.addPlayerToSystem(p);
                        JOptionPane.showMessageDialog(frame, "Player added successfully!");
                    } else {
                        JOptionPane.showMessageDialog(frame, "Player ID already exists in this team.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Invalid number format for Age or Shirt Number.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // 5. عرض المباريات
        btnViewMatches.addActionListener(e -> showScrollableMessage(league.getAllMatchesString(), "League Matches"));

        // 6. إضافة مباراة
        btnAddMatch.addActionListener(e -> {
            JTextField idField = new JTextField();
            JTextField dateField = new JTextField("yyyy-MM-dd");
            JTextField t1Field = new JTextField();
            JTextField t2Field = new JTextField();
            JTextField refField = new JTextField();
            JTextField stadField = new JTextField();

            Object[] message = {
                "Match ID:", idField,
                "Date (yyyy-MM-dd):", dateField,
                "Team 1 Name:", t1Field,
                "Team 2 Name:", t2Field,
                "Referee:", refField,
                "Stadium:", stadField
            };

            int option = JOptionPane.showConfirmDialog(frame, message, "Add New Match", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                String mid = idField.getText().trim();
                String date = dateField.getText().trim();
                String t1 = t1Field.getText().trim();
                String t2 = t2Field.getText().trim();
                String ref = refField.getText().trim();
                String stad = stadField.getText().trim();

                if (league.findMatchByID(mid) != null) {
                    JOptionPane.showMessageDialog(frame, "Match ID already exists.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (league.findTeamByName(t1) == null || league.findTeamByName(t2) == null) {
                    JOptionPane.showMessageDialog(frame, "One or both teams not found in the system.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (!league.isStadiumFree(stad, date, "")) {
                    JOptionPane.showMessageDialog(frame, "Stadium is already booked on this date.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                try {
                    league.addMatch(new Match(mid, date, t1, t2, ref, stad));
                    JOptionPane.showMessageDialog(frame, "Match added successfully!");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Invalid date format. Please use yyyy-MM-dd", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // 7. الهدافين
        btnTopScorers.addActionListener(e -> JOptionPane.showMessageDialog(frame, league.getTopScorersString(), "Top 3 Scorers", JOptionPane.INFORMATION_MESSAGE));

        // 8. الحراس
        btnTopGKs.addActionListener(e -> JOptionPane.showMessageDialog(frame, league.getTopGoalkeepersString(), "Top 3 Goalkeepers", JOptionPane.INFORMATION_MESSAGE));

        // 9. الحفظ والخروج
        btnSaveExit.addActionListener(e -> performSaveAndExit());

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                performSaveAndExit();
            }
        });

        frame.setVisible(true);
    }

    private JButton createStyledButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        btn.setFocusPainted(false);
        btn.setBackground(new Color(236, 240, 241));
        return btn;
    }

    private void showScrollableMessage(String message, String title) {
        JTextArea textArea = new JTextArea(message);
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(500, 400));
        JOptionPane.showMessageDialog(frame, scrollPane, title, JOptionPane.PLAIN_MESSAGE);
    }

    private void performSaveAndExit() {
        league.saveData();
        JOptionPane.showMessageDialog(frame, "Data saved successfully to CSV files.\nGoodbye!", "Saving", JOptionPane.INFORMATION_MESSAGE);
        System.exit(0);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LeagueGUI());
    }
}