package pl.javastart.library.io;

import pl.javastart.library.model.Book;
import pl.javastart.library.model.LibraryUser;
import pl.javastart.library.model.Magazine;
import pl.javastart.library.io.ConsolePrinter;

import java.util.Scanner;

public class DataReader {
    private Scanner input = new Scanner(System.in);
    private ConsolePrinter printer;

    public DataReader(ConsolePrinter printer) {
        this.printer = printer;
    }

    public Book readAndCreateBook(){
        printer.printLine("Tytuł:");
        String title = input.nextLine();
        printer.printLine("Autor:");
        String author = input.nextLine();
        printer.printLine("Rok wydania:");
        int release_date=input.nextInt();
        input.nextLine();
        printer.printLine("Ilość stron:");
        int pages=input.nextInt();
        input.nextLine();
        printer.printLine("Wydawnictwo:");
        String publisher = input.nextLine();
        printer.printLine("ISBN:");
        String isbn = input.nextLine();
        return new Book(title, author,release_date, pages, publisher, isbn);
    }

    public Magazine readAndCreateMagazine() {
        printer.printLine("Tytuł:");
        String title = input.nextLine();
        printer.printLine("Język:");
        String language = input.nextLine();
        printer.printLine("Wydawnictwo:");
        String publisher = input.nextLine();
        printer.printLine("Rok wydania:");
        int year = input.nextInt();
        printer.printLine("Miesiąc:");
        int month = input.nextInt();
        printer.printLine("Dzień:");
        int day = input.nextInt();
        return new Magazine(title, publisher, language, year, month, day);
    }

    public LibraryUser createLibraryUser()
    {
        printer.printLine("Imię:");
        String firstName = input.nextLine();
        printer.printLine("Nazwisko:");
        String lastName = input.nextLine();
        printer.printLine("Pesel:");
        String pesel = input.nextLine();
        return new LibraryUser(firstName, lastName, pesel);
    }

    public int getInt()
    {
        try {
            return input.nextInt();
        }finally {
            input.nextLine();
        }
    }

    public String getString()
    {
        return input.nextLine();
    }

    public void close(){
        input.close();
    }
}
