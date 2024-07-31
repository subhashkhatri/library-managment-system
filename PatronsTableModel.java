package org.example;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class PatronsTableModel extends AbstractTableModel {
    private final String[] columnNames = {"ID", "Name", "Email"};
    private List<Patron> patrons;

    public PatronsTableModel(List<Patron> patrons) {
        this.patrons = patrons;
    }

    @Override
    public int getRowCount() {
        return patrons.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Patron patron = patrons.get(rowIndex);
        switch (columnIndex) {
            case 0: return patron.getId();
            case 1: return patron.getName();
            case 2: return patron.getEmail();
            default: return null;
        }
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }
}
