package View;

import Database.DatabaseEngine;
import Database.DatabaseRemover;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public abstract class BaseManagementPanel<T> extends JPanel {
    protected final DefaultTableModel tableModel;
    protected final JTable table;
    protected final JComboBox<String> sortBox;
    protected final JTextField searchField;
    protected final String databaseFile;

    public BaseManagementPanel(String title, String databaseFile, String[] columns, String[] sortOptions) {
        this.databaseFile = databaseFile;
        setLayout(new BorderLayout());

        tableModel = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        JButton addBtn = new JButton("+ Create New");
        JButton editBtn = new JButton("Edit");
        JButton deleteBtn = new JButton("Delete");

        sortBox = new JComboBox<>(sortOptions);
        searchField = new JTextField(15);

        controlPanel.add(addBtn); controlPanel.add(editBtn); controlPanel.add(deleteBtn);
        controlPanel.add(new JLabel("Sort By:")); controlPanel.add(sortBox);
        controlPanel.add(new JLabel("Search:")); controlPanel.add(searchField);

        add(controlPanel, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        addBtn.addActionListener(e -> openAddDialog());
        editBtn.addActionListener(e -> verifyAndOpenEditDialog());
        deleteBtn.addActionListener(e -> executeDeleteRoutine());
        sortBox.addActionListener(e -> loadData());
        searchField.addKeyListener(new KeyAdapter() {
            @Override public void keyReleased(KeyEvent e) { loadData(); }
        });

        SwingUtilities.invokeLater(this::loadData);
    }

    protected abstract ArrayList<String[]> fetchRawRows(String sortBy);
    protected abstract T convertRowToModel(String[] row);
    protected abstract String[] convertModelToRow(T model);
    protected abstract boolean matchesSearchCriteria(T model, String query);
    protected abstract void openAddDialog();
    protected abstract void openEditDialog(T modelData);

    public void loadData() {
        tableModel.setRowCount(0);
        ArrayList<String[]> rows = fetchRawRows(sortBox.getSelectedItem().toString());
        String query = searchField.getText().trim().toLowerCase();

        for (String[] row : rows) {
            T model = convertRowToModel(row);
            if (model == null) continue;

            if (query.isEmpty() || matchesSearchCriteria(model, query)) {
                tableModel.addRow(convertModelToRow(model));
            }
        }
    }

    private void verifyAndOpenEditDialog() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a row first.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String targetId = tableModel.getValueAt(selectedRow, 0).toString();
        ArrayList<String[]> matches = DatabaseEngine.querySearch(databaseFile, 0, targetId);
        if (!matches.isEmpty()) {
            openEditDialog(convertRowToModel(matches.getFirst()));
        }
    }

    private void executeDeleteRoutine() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a row first.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String targetId = tableModel.getValueAt(selectedRow, 0).toString();
        int confirmation = JOptionPane.showConfirmDialog(this, "Delete ID: " + targetId + "?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirmation == JOptionPane.YES_OPTION) {
            DatabaseRemover.deleteRecordById(databaseFile, targetId);
            loadData();
        }
    }
}
