package Serialization2;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

class Human implements Serializable {
    public String firstName;
    public String lastName;

    public Human(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return "Name: " + firstName + " " + lastName;
    }
}

class Author extends Human implements Serializable {
    public Author(String firstName, String lastName) {
        super(firstName, lastName);
    }

    @Override
    public String toString() {
        return super.toString();
    }

    // Публичные методы для сериализации и десериализации
    public void writeObject(ObjectOutputStream out) throws IOException {
        out.writeObject(firstName);
        out.writeObject(lastName);
    }

    public void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        firstName = (String) in.readObject();
        lastName = (String) in.readObject();
    }
}

class Book implements Serializable {
    private String title;
    private List<Author> authors;
    private int publicationYear;
    private int editionNumber;

    public Book(String title, List<Author> authors, int publicationYear, int editionNumber) {
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

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.writeObject(title);
        out.writeObject(authors);
        out.writeInt(publicationYear);
        out.writeInt(editionNumber);
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        title = (String) in.readObject();
        authors = (List<Author>) in.readObject();
        publicationYear = in.readInt();
        editionNumber = in.readInt();
    }
}

class BookStore implements Serializable {
    private String name;
    private List<Book> books;

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
        return "BookStore: " + name + "\nBooks:\n" + bookList;
    }
}

class BookReader extends Human implements Serializable {
    private int readerID;
    private List<Book> borrowedBooks;

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

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.writeObject(firstName);
        out.writeObject(lastName);
        out.writeInt(readerID);
        out.writeObject(borrowedBooks);
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        firstName = (String) in.readObject();
        lastName = (String) in.readObject();
        readerID = in.readInt();
        borrowedBooks = (List<Book>) in.readObject();
    }
}

class Library implements Serializable {
    private String name;
    private List<BookStore> bookStores;
    private List<BookReader> readers;

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

        return "Library: " + name + "\nBookStores:\n" + bookStoresList + "\nReaders:\n" + readersList;
    }
}

public class Main {
    public static void main(String[] args) {

        Author tolkien = new Author("J.R.R.", "Tolkien");
        Author rowling = new Author("J.K.", "Rowling");
        Author martin = new Author("George R.R.", "Martin");
        Author austen = new Author("Jane", "Austen");
        Author dostoevsky = new Author("Fyodor", "Dostoevsky");

        Book book1 = new Book("The Lord of the Rings", List.of(tolkien), 1954, 1);
        Book book2 = new Book("Harry Potter and the Philosopher's Stone", List.of(rowling), 1997, 1);
        Book book3 = new Book("A Game of Thrones", List.of(martin), 1996, 1);
        Book book4 = new Book("Pride and Prejudice", List.of(austen), 1813, 1);
        Book book5 = new Book("Crime and Punishment", List.of(dostoevsky), 1866, 1);

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

        Library library = new Library("My Library");
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

        // Display Deserialized Library
        System.out.println("Deserialized Library:");
        System.out.println(deserializedLibrary.toString());
    }
}


