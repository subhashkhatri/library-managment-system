package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler {
    private Connection connection;

    public DatabaseHandler() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:src/main/resources/library.db");
            createTables();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    private void createTables() throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.execute("CREATE TABLE IF NOT EXISTS books (id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT NOT NULL, author TEXT NOT NULL, isbn TEXT UNIQUE NOT NULL, issued BOOLEAN DEFAULT 0)");
        stmt.execute("CREATE TABLE IF NOT EXISTS patrons (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, email TEXT UNIQUE NOT NULL)");
        stmt.execute("CREATE TABLE IF NOT EXISTS transactions (id INTEGER PRIMARY KEY AUTOINCREMENT, book_id INTEGER, patron_id INTEGER, issue_date DATE, return_date DATE, FOREIGN KEY(book_id) REFERENCES books(id), FOREIGN KEY(patron_id) REFERENCES patrons(id))");
        stmt.close();
    }

    public boolean addBook(Book book) {
        try {
            if (isIsbnExists(book.getIsbn())) {
                System.out.println("ISBN already exists in the database.");
                return false;
            }
            PreparedStatement pstmt = connection.prepareStatement("INSERT INTO books (title, author, isbn) VALUES (?, ?, ?)");
            pstmt.setString(1, book.getTitle());
            pstmt.setString(2, book.getAuthor());
            pstmt.setString(3, book.getIsbn());
            pstmt.executeUpdate();
            pstmt.close();
            System.out.println("Book added successfully: " + book.getTitle());
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }  private boolean isIsbnExists(String isbn) throws SQLException {
        PreparedStatement pstmt = connection.prepareStatement("SELECT COUNT(*) FROM books WHERE isbn = ?");
        pstmt.setString(1, isbn);
        ResultSet rs = pstmt.executeQuery();
        boolean exists = rs.next() && rs.getInt(1) > 0;
        rs.close();
        pstmt.close();
        return exists;
    }

    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM books");
            while (rs.next()) {
                books.add(new Book(rs.getInt("id"), rs.getString("title"), rs.getString("author"), rs.getString("isbn"), rs.getBoolean("issued")));
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

    public void addPatron(Patron patron) {
        try {
            PreparedStatement pstmt = connection.prepareStatement("INSERT INTO patrons (name, email) VALUES (?, ?)");
            pstmt.setString(1, patron.getName());
            pstmt.setString(2, patron.getEmail());
            pstmt.executeUpdate();
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Patron> getAllPatrons() {
        List<Patron> patrons = new ArrayList<>();
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM patrons");
            while (rs.next()) {
                patrons.add(new Patron(rs.getInt("id"), rs.getString("name"), rs.getString("email")));
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return patrons;
    }

    public void issueBook(int bookId, int patronId) {
        try {
            PreparedStatement pstmt = connection.prepareStatement("INSERT INTO transactions (book_id, patron_id, issue_date) VALUES (?, ?, ?)");
            pstmt.setInt(1, bookId);
            pstmt.setInt(2, patronId);
            pstmt.setDate(3, new Date(System.currentTimeMillis()));
            pstmt.executeUpdate();
            pstmt.close();

            pstmt = connection.prepareStatement("UPDATE books SET issued = 1 WHERE id = ?");
            pstmt.setInt(1, bookId);
            pstmt.executeUpdate();
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void returnBook(int bookId) {
        try {
            PreparedStatement pstmt = connection.prepareStatement("UPDATE transactions SET return_date = ? WHERE book_id = ? AND return_date IS NULL");
            pstmt.setDate(1, new Date(System.currentTimeMillis()));
            pstmt.setInt(2, bookId);
            pstmt.executeUpdate();
            pstmt.close();

            pstmt = connection.prepareStatement("UPDATE books SET issued = 0 WHERE id = ?");
            pstmt.setInt(1, bookId);
            pstmt.executeUpdate();
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Transaction> getAllTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM transactions");
            while (rs.next()) {
                transactions.add(new Transaction(rs.getInt("id"), rs.getInt("book_id"), rs.getInt("patron_id"), rs.getDate("issue_date"), rs.getDate("return_date")));
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transactions;
    }
}
