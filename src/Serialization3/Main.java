package Serialization3;


import java.io.*;
import java.util.ArrayList;
import java.util.List;

class Author implements Externalizable {
    private String firstName;
    private String lastName;

    public Author() {}

    public Author(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(firstName);
        out.writeObject(lastName);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        firstName = (String) in.readObject();
        lastName = (String) in.readObject();
    }

    @Override
    public String toString() {
        return firstName + " " + lastName;
    }
}

class Book implements Externalizable {
    private String title;
    private List<Author> authors;
    private int publicationYear;
    private int editionNumber;

    public Book() {
        authors = new ArrayList<>();
    }

    public Book(String title, List<Author> authors, int publicationYear, int editionNumber) {
        this.title = title;
        this.authors = authors;
        this.publicationYear = publicationYear;
        this.editionNumber = editionNumber;
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(title);
        out.writeInt(authors.size());
        for (Author author : authors) {
            author.writeExternal(out);
        }
        out.writeInt(publicationYear);
        out.writeInt(editionNumber);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        title = (String) in.readObject();
        int numAuthors = in.readInt();
        authors = new ArrayList<>(numAuthors);
        for (int i = 0; i < numAuthors; i++) {
            Author author = new Author();
            author.readExternal(in);
            authors.add(author);
        }
        publicationYear = in.readInt();
        editionNumber = in.readInt();
    }

    @Override
    public String toString() {
        return title + " by " + authors + ", " + publicationYear + ", Edition " + editionNumber;
    }
}

class BookStore implements Externalizable {
    private String name;
    private List<Book> books;

    public BookStore() {
        books = new ArrayList<>();
    }

    public BookStore(String name) {
        this.name = name;
        this.books = new ArrayList<>();
    }

    public void addBook(Book book) {
        books.add(book);
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(name);
        out.writeInt(books.size());
        for (Book book : books) {
            book.writeExternal(out);
        }
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        name = (String) in.readObject();
        int numBooks = in.readInt();
        books = new ArrayList<>(numBooks);
        for (int i = 0; i < numBooks; i++) {
            Book book = new Book();
            book.readExternal(in);
            books.add(book);
        }
    }

    @Override
    public String toString() {
        return name + ": " + books.size() + " books";
    }
}

class BookReader implements Externalizable {
    private String firstName;
    private String lastName;
    private int readerID;
    private List<Book> borrowedBooks;

    public BookReader() {
        borrowedBooks = new ArrayList<>();
    }

    public BookReader(String firstName, String lastName, int readerID) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.readerID = readerID;
        this.borrowedBooks = new ArrayList<>();
    }

    public void borrowBook(Book book) {
        borrowedBooks.add(book);
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(firstName);
        out.writeObject(lastName);
        out.writeInt(readerID);
        out.writeInt(borrowedBooks.size());
        for (Book book : borrowedBooks) {
            book.writeExternal(out);
        }
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        firstName = (String) in.readObject();
        lastName = (String) in.readObject();
        readerID = in.readInt();
        int numBorrowedBooks = in.readInt();
        borrowedBooks = new ArrayList<>(numBorrowedBooks);
        for (int i = 0; i < numBorrowedBooks; i++) {
            Book book = new Book();
            book.readExternal(in);
            borrowedBooks.add(book);
        }
    }

    @Override
    public String toString() {
        return firstName + " " + lastName + " (Reader ID: " + readerID + ")";
    }
}

class Library implements Externalizable {
    private String name;
    private List<BookStore> bookStores;
    private List<BookReader> readers;

    public Library() {
        bookStores = new ArrayList<>();
        readers = new ArrayList<>();
    }

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
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(name);
        out.writeInt(bookStores.size());
        for (BookStore store : bookStores) {
            store.writeExternal(out);
        }
        out.writeInt(readers.size());
        for (BookReader reader : readers) {
            reader.writeExternal(out);
        }
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        name = (String) in.readObject();
        int numBookStores = in.readInt();
        bookStores = new ArrayList<>(numBookStores);
        for (int i = 0; i < numBookStores; i++) {
            BookStore store = new BookStore();
            store.readExternal(in);
            bookStores.add(store);
        }
        int numReaders = in.readInt();
        readers = new ArrayList<>(numReaders);
        for (int i = 0; i < numReaders; i++) {
            BookReader reader = new BookReader();
            reader.readExternal(in);
            readers.add(reader);
        }
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

    public static void serializeLibrary(Library library, String fileName) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(library);
            System.out.println("Serialization successful.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Library deserializeLibrary(String fileName) {
        Library library = null;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
            library = (Library) ois.readObject();
            System.out.println("Deserialization successful.");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return library;
    }

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

        serializeLibrary(library, "library.ser");


        Library deserializedLibrary = deserializeLibrary("library.ser");
        System.out.println("Deserialized Library: " + deserializedLibrary);
    }
}







