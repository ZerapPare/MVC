package ui;

import model.DataStore;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {
    public LoginFrame(){
        setTitle("Login");
        setSize(360, 180);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTextField user = new JTextField("admin");
        JPasswordField pass = new JPasswordField("admin");
        JButton btn = new JButton("Login");

        JPanel p = new JPanel(new GridLayout(3,2,8,8));
        p.setBorder(BorderFactory.createEmptyBorder(12,12,12,12));
        p.add(new JLabel("Username")); p.add(user);
        p.add(new JLabel("Password")); p.add(pass);
        p.add(new JLabel()); p.add(btn);
        add(p);

        btn.addActionListener(e -> {
            if ("admin".equals(user.getText().trim()) && "admin".equals(new String(pass.getPassword()))) {
                dispose();
                new MainFrame(new DataStore()).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Invalid username or password.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
