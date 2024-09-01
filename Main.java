import com.formdev.flatlaf.FlatDarkLaf;
import javax.swing.*;
import java.io.*;
import java.awt.event.ActionEvent;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Main {


    public static void createAndShowGUI() {
        FlatDarkLaf.setup();  //Must be called first of all Swing code as this sets the look and feel to FlatDark.
        //Creating Frame, and naming it
        final JFrame frame = new JFrame("Notepad --");

        //Variables
        JMenuBar menuBar;
        JMenu menu, submenu;
        JMenuItem menuItemNew, menuItemSave, menuItemClear;

        //Create the File menu bar
        menuBar = new JMenuBar();
        menu = new JMenu("File");
        menuBar.add(menu);

        //New file/ Save as / Recent
        menuItemNew = new JMenuItem("New File", new ImageIcon("new.png"));
        menu.add(menuItemNew);
        menuItemSave = new JMenuItem("Save File", new ImageIcon("save-file.png"));
        menu.add(menuItemSave);
        menu.addSeparator();
        submenu = new JMenu("Recent");
        menu.add(submenu);

        //Create the Edit in menu bar
        menu = new JMenu("Edit");
        menuBar.add(menu);
        menuItemClear = new JMenuItem("Clear");
        menu.add(menuItemClear);

        //Create the Format in menu bar
        menu = new JMenu("Format");
        menuBar.add(menu);
        //Create the View in menu bar
        menu = new JMenu("View");
        menuBar.add(menu);
        //Create the Help in menu bar
        menu = new JMenu("Help");
        menuBar.add(menu);

        final JPanel panel = new JPanel(); //FlowLayout.
        //Menu bar
        JTextArea text_area = new JTextArea("New Text File");
        text_area.setColumns(80);
        text_area.setRows(30);
        panel.add(text_area);

        //Save file mechanism
        menuItemSave.addActionListener((ActionEvent e) ->{
            JFileChooser fileChooser = new JFileChooser();
            int option = fileChooser.showSaveDialog(frame);
            if (option == JFileChooser.APPROVE_OPTION){
                File file = fileChooser.getSelectedFile();
                String name = file.getAbsolutePath();
                JMenuItem menuItemRecent = new JMenuItem(name);
                submenu.add(menuItemRecent);
                menuItemRecent.addActionListener((ActionEvent a) -> {
                        try (Scanner sc = new Scanner(new File(name), StandardCharsets.UTF_8)) {
                            while (sc.hasNextLine()) {
                                String line = sc.next();
                                text_area.append(line);

                            }
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                });
//                System.out.println(name);
                try(BufferedWriter writer = new BufferedWriter(new FileWriter(file))){
                    writer.write(text_area.getText());
                    JOptionPane.showMessageDialog(frame, "File Saved Successfully!");
                } catch(IOException ex){
                    JOptionPane.showMessageDialog(frame, "Error saving file: " + ex.getMessage());
                }
            }
        });
        //New File Mechanism
        menuItemNew.addActionListener((ActionEvent e) ->{
            int option = JOptionPane.showConfirmDialog(frame, "Unsaved file gonna be lost!");
            if ((option == JOptionPane.NO_OPTION) | (option == JOptionPane.CANCEL_OPTION) | (option == JOptionPane.CLOSED_OPTION)){
                JOptionPane.showMessageDialog(frame, "Then go save it!");
            }
            else {
                text_area.setText(" ");
            }

        });
        //Clear mechanism
        menuItemClear.addActionListener((ActionEvent c) -> {
            int option = JOptionPane.showConfirmDialog(frame, "Are you sure? (All text is going to be deleted!)");
            if ((option == JOptionPane.NO_OPTION) | (option == JOptionPane.CANCEL_OPTION) | (option == JOptionPane.CLOSED_OPTION)){
                return;
            }
            else {
                text_area.setText(" ");
            }
        });

        //Icon
        ImageIcon icon = new ImageIcon("notepad_icon.png");
        frame.setJMenuBar(menuBar);
        frame.setIconImage(icon.getImage());

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(panel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void main(final String[] args) {
        SwingUtilities.invokeLater(Main::createAndShowGUI);
    }
}
