package pl.javastart.library.app;

import pl.javastart.library.app.LibraryControl;
import pl.javastart.library.model.Library;
import pl.javastart.library.io.ConsolePrinter;

public class LibraryApp {

    private static final String APP_NAME="Biblioteka v1.10";

    public static void main(String[] args) {
        ConsolePrinter printer = new ConsolePrinter();
        printer.printLine(APP_NAME.toString());
        LibraryControl libcon=new LibraryControl();
        libcon.controlLoop();
    }
}
