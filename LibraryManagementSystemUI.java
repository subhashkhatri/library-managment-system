package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LibraryManagementSystemUI extends JFrame {
    private DatabaseHandler databaseHandler;

    public LibraryManagementSystemUI() {
        databaseHandler = new DatabaseHandler();
        initialize();
    }

    private void initialize() {
        setTitle("Library Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLayout(new BorderLayout());

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Books", createBooksPanel());
        tabbedPane.addTab("Patrons", createPatronsPanel());
        tabbedPane.addTab("Transactions", createTransactionsPanel());

        add(tabbedPane, BorderLayout.CENTER);
        add(createFooter(), BorderLayout.SOUTH);

        setVisible(true);
    }

    private JPanel createBooksPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel formPanel = new JPanel(new GridLayout(4, 2));

        JTextField titleField = new JTextField();
        JTextField authorField = new JTextField();
        JTextField isbnField = new JTextField();

        formPanel.add(new JLabel("Title:"));
        formPanel.add(titleField);
        formPanel.add(new JLabel("Author:"));
        formPanel.add(authorField);
        formPanel.add(new JLabel("ISBN:"));
        formPanel.add(isbnField);

        JButton addButton = new JButton("Add Book");
        formPanel.add(new JLabel());
        formPanel.add(addButton);

        panel.add(formPanel, BorderLayout.NORTH);
        JTable booksTable = new JTable(new BooksTableModel(databaseHandler.getAllBooks()));
        panel.add(new JScrollPane(booksTable), BorderLayout.CENTER);

        addButton.addActionListener(e -> {
            Book book = new Book(titleField.getText(), authorField.getText(), isbnField.getText());
            boolean success = databaseHandler.addBook(book);
            showMessageDialog(success ? "Book added successfully!" : "Failed to add book. ISBN might already exist.");
            refreshBooksTable(booksTable);
        });

        return panel;
    }

    private JPanel createPatronsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel formPanel = new JPanel(new GridLayout(3, 2));

        JTextField nameField = new JTextField();
        JTextField emailField = new JTextField();

        formPanel.add(new JLabel("Name:"));
        formPanel.add(nameField);
        formPanel.add(new JLabel("Email:"));
        formPanel.add(emailField);

        JButton addButton = new JButton("Add Patron");
        formPanel.add(new JLabel());
        formPanel.add(addButton);

        panel.add(formPanel, BorderLayout.NORTH);
        JTable patronsTable = new JTable(new PatronsTableModel(databaseHandler.getAllPatrons()));
        panel.add(new JScrollPane(patronsTable), BorderLayout.CENTER);

        addButton.addActionListener(e -> {
            Patron patron = new Patron(nameField.getText(), emailField.getText());
            databaseHandler.addPatron(patron);
            refreshPatronsTable(patronsTable);
        });

        return panel;
    }

    private JPanel createTransactionsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel formPanel = new JPanel(new GridLayout(4, 2));

        JTextField bookIdField = new JTextField();
        JTextField patronIdField = new JTextField();

        formPanel.add(new JLabel("Book ID:"));
        formPanel.add(bookIdField);
        formPanel.add(new JLabel("Patron ID:"));
        formPanel.add(patronIdField);

        JButton issueButton = new JButton("Issue Book");
        JButton returnButton = new JButton("Return Book");
        formPanel.add(new JLabel());
        formPanel.add(issueButton);
        formPanel.add(new JLabel());
        formPanel.add(returnButton);

        panel.add(formPanel, BorderLayout.NORTH);
        JTable transactionsTable = new JTable(new TransactionsTableModel(databaseHandler.getAllTransactions()));
        panel.add(new JScrollPane(transactionsTable), BorderLayout.CENTER);

        issueButton.addActionListener(e -> {
            int bookId = Integer.parseInt(bookIdField.getText());
            int patronId = Integer.parseInt(patronIdField.getText());
            databaseHandler.issueBook(bookId, patronId);
            refreshTransactionsTable(transactionsTable);
        });

        returnButton.addActionListener(e -> {
            int bookId = Integer.parseInt(bookIdField.getText());
            databaseHandler.returnBook(bookId);
            refreshTransactionsTable(transactionsTable);
        });

        return panel;
    }

    private JPanel createFooter() {
        JPanel footer = new JPanel();
        footer.add(new JLabel("Made by Chandan Kumar"));
        return footer;
    }

    private void refreshBooksTable(JTable booksTable) {
        booksTable.setModel(new BooksTableModel(databaseHandler.getAllBooks()));
    }

    private void refreshPatronsTable(JTable patronsTable) {
        patronsTable.setModel(new PatronsTableModel(databaseHandler.getAllPatrons()));
    }

    private void refreshTransactionsTable(JTable transactionsTable) {
        transactionsTable.setModel(new TransactionsTableModel(databaseHandler.getAllTransactions()));
    }

    private void showMessageDialog(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LibraryManagementSystemUI::new);
    }
}
