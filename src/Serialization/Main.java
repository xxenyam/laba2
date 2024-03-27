package Serialization;

import java.io.*;
import java.util.ArrayList;

class Human implements Serializable {
    private static final long serialVersionUID = 1L;
    private String firstName;
    private String lastName;

    public Human(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return "Name: " + firstName + " " + lastName;
    }
}

class Author extends Human {
    public Author(String firstName, String lastName) {
        super(firstName, lastName);
    }
}

class Book implements Serializable {
    private static final long serialVersionUID = 1L;
    private String title;
    private ArrayList<Author> authors;
    private int publicationYear;
    private int editionNumber;

    public Book(String title, ArrayList<Author> authors, int publicationYear, int editionNumber) {
        this.title = title;
        this.authors = authors;
        this.publicationYear = publicationYear;
        this.editionNumber = editionNumber;
    }

    @Override
    public String toString() {
        StringBuilder authorNames = new StringBuilder();
        for (Author author : authors) {
            authorNames.append(author.toString()).append(", ");
        }
        return "Title: " + title + ", Authors: " + authorNames + ", Publication Year: " + publicationYear + ", Edition Number: " + editionNumber;
    }
}

class BookStore implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private ArrayList<Book> books;

    public BookStore(String name) {
        this.name = name;
        this.books = new ArrayList<>();
    }

    public void addBook(Book book) {
        books.add(book);
    }

    @Override
    public String toString() {
        StringBuilder bookList = new StringBuilder();
        for (Book book : books) {
            bookList.append(book.toString()).append("\n");
        }
        return "Serialization.BookStore: " + name + "\nBooks:\n" + bookList;
    }
}

class BookReader extends Human implements Serializable {
    private static final long serialVersionUID = 1L;
    private int readerID;
    private ArrayList<Book> borrowedBooks;

    public BookReader(String firstName, String lastName, int readerID) {
        super(firstName, lastName);
        this.readerID = readerID;
        this.borrowedBooks = new ArrayList<>();
    }

    public void borrowBook(Book book) {
        borrowedBooks.add(book);
    }

    @Override
    public String toString() {
        StringBuilder borrowedBooksList = new StringBuilder();
        for (Book book : borrowedBooks) {
            borrowedBooksList.append(book.toString()).append("\n");
        }
        return super.toString() + "\nReader ID: " + readerID + "\nBorrowed Books:\n" + borrowedBooksList;
    }
}

class Library implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private ArrayList<BookStore> bookStores;
    private ArrayList<BookReader> readers;

    public Library(String name) {
        this.name = name;
        this.bookStores = new ArrayList<>();
        this.readers = new ArrayList<>();
    }

    public void addBookStore(BookStore bookStore) {
        bookStores.add(bookStore);
    }

    public void addReader(BookReader reader) {
        readers.add(reader);
    }

    @Override
    public String toString() {
        StringBuilder bookStoresList = new StringBuilder();
        for (BookStore bookStore : bookStores) {
            bookStoresList.append(bookStore.toString()).append("\n");
        }

        StringBuilder readersList = new StringBuilder();
        for (BookReader reader : readers) {
            readersList.append(reader.toString()).append("\n");
        }

        return "Serialization.Library: " + name + "\nBookStores:\n" + bookStoresList + "\nReaders:\n" + readersList;
    }
}

public class Main {
    public static void main(String[] args) {

        Author tolkien = new Author("J.R.R.", "Tolkien");
        Author rowling = new Author("J.K.", "Rowling");
        Author martin = new Author("George R.R.", "Martin");
        Author austen = new Author("Jane", "Austen");
        Author dostoevsky = new Author("Fyodor", "Dostoevsky");


        Book book1 = new Book("The Lord of the Rings", new ArrayList<Author>() {{ add(tolkien); }}, 1954, 1);
        Book book2 = new Book("Harry Potter and the Philosopher's Stone", new ArrayList<Author>() {{ add(rowling); }}, 1997, 1);
        Book book3 = new Book("A Game of Thrones", new ArrayList<Author>() {{ add(martin); }}, 1996, 1);
        Book book4 = new Book("Pride and Prejudice", new ArrayList<Author>() {{ add(austen); }}, 1813, 1);
        Book book5 = new Book("Crime and Punishment", new ArrayList<Author>() {{ add(dostoevsky); }}, 1866, 1);

        BookStore bookStore = new BookStore("Fantasy Bookstore");
        bookStore.addBook(book1);
        bookStore.addBook(book2);
        bookStore.addBook(book3);

        BookStore classicStore = new BookStore("Classic Bookstore");
        classicStore.addBook(book4);
        classicStore.addBook(book5);


        BookReader reader1 = new BookReader("Alice", "Johnson", 1);
        reader1.borrowBook(book1);

        BookReader reader2 = new BookReader("Bob", "Smith", 2);
        reader2.borrowBook(book2);

        Library library = new Library("My Serialization.Library");
        library.addBookStore(bookStore);
        library.addBookStore(classicStore);
        library.addReader(reader1);
        library.addReader(reader2);


        try {
            FileOutputStream fileOut = new FileOutputStream("library.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(library);
            out.close();
            fileOut.close();
            System.out.println("Serialized data is saved in library.ser");
        } catch (IOException i) {
            i.printStackTrace();
        }

        Library deserializedLibrary = null;
        try {
            FileInputStream fileIn = new FileInputStream("library.ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            deserializedLibrary = (Library) in.readObject();
            in.close();
            fileIn.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return;
        }

        // Display Deserialized Serialization.Library
        System.out.println("Deserialized Serialization.Library:");
        System.out.println(deserializedLibrary.toString());
    }
}




