package View;

import Database.DatabaseEngine;
import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;

public class SearchableComboBox extends JComboBox<String> {
    private final HashMap<String, String> itemMap = new HashMap<>(); // Holds "Name -> ID" mapping
    private final ArrayList<String> originalNames = new ArrayList<>();

    public SearchableComboBox(String databaseFile) {
        setEditable(true);

        // Fetch raw target reference lines
        ArrayList<String[]> rows = DatabaseEngine.readTable(databaseFile);
        for (String[] row : rows) {
            if (row.length > 1) {
                String id = row[0].trim();
                String name = row[1].trim();
                String displayString = name + " (ID: " + id + ")";

                itemMap.put(displayString, id);
                originalNames.add(displayString);
                addItem(displayString);
            }
        }

        // Apply real-time predictive filtering to input text fields
        JTextField editor = (JTextField) getEditor().getEditorComponent();
        editor.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_DOWN) {
                    return;
                }
                String currentText = editor.getText();
                DefaultComboBoxModel<String> filterModel = new DefaultComboBoxModel<>();

                for (String display : originalNames) {
                    if (display.toLowerCase().contains(currentText.toLowerCase())) {
                        filterModel.addElement(display);
                    }
                }
                setModel(filterModel);
                editor.setText(currentText);
                setPopupVisible(true);
            }
        });
    }

    /**
     * Resolves the selected item down to its clean numeric primary ID string.
     */
    public String getSelectedId() {
        Object item = getSelectedItem();
        if (item == null) return "";
        return itemMap.getOrDefault(item.toString(), "").trim();
    }

    /**
     * Sets the box selection state accurately based on a raw numeric entry ID.
     */
    public void setSelectedById(String id) {
        for (String display : originalNames) {
            if (display.endsWith("(ID: " + id + ")")) {
                setSelectedItem(display);
                return;
            }
        }
    }
}
