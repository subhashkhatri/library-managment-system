package org.example;


import javax.swing.table.AbstractTableModel;
import java.util.List;

public class TransactionsTableModel extends AbstractTableModel {
    private final String[] columnNames = {"ID", "Book ID", "Patron ID", "Issue Date", "Return Date"};
    private List<Transaction> transactions;

    public TransactionsTableModel(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    @Override
    public int getRowCount() {
        return transactions.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Transaction transaction = transactions.get(rowIndex);
        switch (columnIndex) {
            case 0: return transaction.getId();
            case 1: return transaction.getBookId();
            case 2: return transaction.getPatronId();
            case 3: return transaction.getIssueDate();
            case 4: return transaction.getReturnDate();
            default: return null;
        }
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }
}

