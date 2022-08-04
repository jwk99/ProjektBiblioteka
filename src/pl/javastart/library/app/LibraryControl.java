package pl.javastart.library.app;

import pl.javastart.library.Exceptions.*;
import pl.javastart.library.io.ConsolePrinter;
import pl.javastart.library.io.DataReader;
import pl.javastart.library.io.file.FileManager;
import pl.javastart.library.io.file.FileManagerBuilder;
import pl.javastart.library.model.*;

import java.util.Comparator;
import java.util.InputMismatchException;
import java.util.Optional;

public class LibraryControl {


    private ConsolePrinter printer = new ConsolePrinter();
    private DataReader dataReader=new DataReader(printer);
    private Library library;
    private FileManager fileManager;

    public LibraryControl() {
        fileManager = new FileManagerBuilder(printer, dataReader).build();
        try {
            library = fileManager.importData();
            printer.printLine("Zaimportowano dane z bazy.s");
        }catch (DataImportException | InvalidDataException e)
        {
            printer.printLine(e.getMessage());
            printer.printLine("Zainicjowano nową bazę.");
            library = new Library();
        }
    }

    public void controlLoop()
    {
        Option option;

        do{
            printOptions();
            option=getOption();
            switch (option)
            {
                case ADD_BOOK:
                    addBook();
                    break;
                case ADD_MAGAZINE:
                    addMagazine();
                    break;
                case PRINT_BOOKS:
                    printBooks();
                    break;
                case PRINT_MAGAZINE:
                    printMagazines();
                    break;
                case DELETE_BOOK:
                    deleteBook();
                    break;
                case DELETE_MAGAZINE:
                    deleteMagazine();
                    break;
                case ADD_USER:
                    addUser();
                    break;
                case PRINT_USERS:
                    printUsers();
                    break;
                case FIND_BOOK:
                    findBook();
                    break;
                case EXIT:
                    exit();
                    break;
                default:
                    printer.printLine("Brak takiej opcji.");
            }
        }while (option!=Option.EXIT);



    }

    private void findBook() {
        printer.printLine("Podaj tytuł publikacji:");
        String title = dataReader.getString();
        String notFoundMessage = "Brak publikacji o podanym tytule.";
        library.findPublicationByTitle(title)
                .map(Publication::toString)
                .ifPresentOrElse(System.out::println, ()-> System.out.println(notFoundMessage));

    }

    private void printUsers() {
        printer.printUsers(library.getSortedUsers(
                //(p1, p2) -> p1.getLastName().compareToIgnoreCase(p2.getLastName())));
                Comparator.comparing(User::getLastName, String.CASE_INSENSITIVE_ORDER)));
    }

    private void addUser() {
        LibraryUser libraryUser = dataReader.createLibraryUser();
        try{
        library.addUser(libraryUser);}
        catch(UserAlreadyExistsException e)
        {
            printer.printLine(e.getMessage());
        }
    }


    private Option getOption() {
        boolean optionOk = false;
        Option option = null;
        while (!optionOk)
        {
            try{
                option = Option.createFromInt(dataReader.getInt());
                optionOk=true;
            } catch (NoSuchOptionException e)
            {
                printer.printLine(e.getMessage());
            }catch (InputMismatchException e)
            {
                printer.printLine("Wprowadzono wartość, która nie jest liczbą, podaj ponownie.");
            }
        }
        return option;
    }

    private void printMagazines() {
        printer.printMagazines(library.getSortedPublications(
             //   (p1, p2) -> p1.getTitle().compareToIgnoreCase(p2.getTitle())
        Comparator.comparing(Publication::getTitle, String.CASE_INSENSITIVE_ORDER)
        ));
    }

    private void addMagazine() {
        try{
        Magazine magazine = dataReader.readAndCreateMagazine();
        library.addPublication(magazine);
        }catch(InputMismatchException e)
        {
            printer.printLine("Nie udało się utworzyć magazynu, niepoprawne dane!");
        }catch (ArrayIndexOutOfBoundsException e)
        {
            printer.printLine("Osiągnięto limit pojemności, nie można dodać kolejnego magazynu!");
        }
    }
    private void deleteMagazine() {
        try {
            Magazine magazine = dataReader.readAndCreateMagazine();
            if (library.removePublication(magazine)) {
                printer.printLine("Usunięto magazyn.");
            } else {
                printer.printLine("Brak wskazanego magazynu.");
            }
        }catch (InputMismatchException e)
        {
            printer.printLine("Nie udao się utworzyć magazynu, niepoprawne dane!");
        }
    }


    private void printOptions(){
        printer.printLine("Wybierz opcję:");
        for (Option option : Option.values()) {
            printer.printLine(option.toString());
        }
    }

    private void addBook(){
        try {
            Book book = dataReader.readAndCreateBook();
            library.addPublication(book);
        }catch(InputMismatchException e)
        {
            printer.printLine("Nie udało się utworzyć książki, niepoprawne dane!");
        }catch (ArrayIndexOutOfBoundsException e)
        {
            printer.printLine("Osiągnięto limit pojemności, nie można dodać kolejnej książki!");
        }
    }
    private void deleteBook() {
        try {
            Book book = dataReader.readAndCreateBook();
            if (library.removePublication(book)) {
                printer.printLine("Usunięto książkę.");
            } else {
                printer.printLine("Brak wskazanej książki.");
            }
        }catch (InputMismatchException e)
        {
            printer.printLine("Nie udało się utworzyć książki, niepoprawne dane!");
        }
    }

    private void printBooks()
    {
        printer.printBooks(library.getSortedPublications(
                //(p1, p2) -> p1.getTitle().compareToIgnoreCase(p2.getTitle())
                Comparator.comparing(Publication::getTitle, String.CASE_INSENSITIVE_ORDER)
        ));
    }

    private void exit()
    {
        try {
            fileManager.exportData(library);
            printer.printLine("Zapis danych do pliku zakończony powodzeniem.");
        }catch (DataExportException e)
        {
            printer.printLine(e.getMessage());
        }
        printer.printLine("Wyłączasz program.");
        dataReader.close();
    }

    public enum Option {
        EXIT(0, "Wyjście z programu"),
        ADD_BOOK(1, "Dodaj książkę"),
        ADD_MAGAZINE(2, "Dodaj magazyn"),
        PRINT_BOOKS(3, "Wyświetl książki"),
        PRINT_MAGAZINE(4, "Wyświetl magazyny"),
        DELETE_BOOK(5, "Usuń książkę"),
        DELETE_MAGAZINE(6, "Usuń magazyn"),
        ADD_USER(7, "Dodaj czytelnika"),
        PRINT_USERS(8, "Wyświetl czytelników"),
        FIND_BOOK(9, "Wyszukaj publikację");

        private final int value;
        private final String description;

        Option(int value, String description) {
            this.value = value;
            this.description = description;
        }

        public String getDescription() {
            return description;
        }

        public int getValue() {
            return value;
        }

        @Override
        public String toString()
        {
            return value+" - "+description;
        }

        static Option createFromInt(int option) throws NoSuchOptionException
        {
            try{
                return Option.values()[option];
            }catch (ArrayIndexOutOfBoundsException e)
            {
                throw new NoSuchOptionException("Brak opcji o id "+option);
            }


        }

    }

}
