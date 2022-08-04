package pl.javastart.library.Exceptions;

public class PublicationAlreadyExistsException extends RuntimeException{
    public PublicationAlreadyExistsException(String message) {
        super(message);
    }
}
