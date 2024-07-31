package org.example;


import javax.swing.table.AbstractTableModel;
import java.util.List;

public class BooksTableModel extends AbstractTableModel {
    private final String[] columnNames = {"ID", "Title", "Author", "ISBN", "Issued"};
    private List<Book> books;

    public BooksTableModel(List<Book> books) {
        this.books = books;
    }

    @Override
    public int getRowCount() {
        return books.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Book book = books.get(rowIndex);
        switch (columnIndex) {
            case 0: return book.getId();
            case 1: return book.getTitle();
            case 2: return book.getAuthor();
            case 3: return book.getIsbn();
            case 4: return book.isIssued();
            default: return null;
        }
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }
}
