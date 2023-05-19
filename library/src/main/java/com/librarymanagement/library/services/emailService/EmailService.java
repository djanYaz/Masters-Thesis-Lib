package com.librarymanagement.library.services.emailService;

import com.librarymanagement.library.entities.OutOfStock;
import com.librarymanagement.library.repositories.BookRepository;
import com.librarymanagement.library.repositories.OutOfStockRepository;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class EmailService {
    private final SendGrid sendGrid;
    private final DateTimeFormatter dateTimeFormatter;
    private String sendingEmail="vaianakiev@tu-sofia.bg";
    @Autowired
    BookRepository bookRepository;
    @Autowired
    OutOfStockRepository outOfStockRepository;

    public EmailService() {
        sendGrid = new SendGrid("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");
        dateTimeFormatter=DateTimeFormatter.ISO_LOCAL_DATE_TIME.withZone(ZoneId.from(ZoneOffset.UTC));
    }

    public void WarnOfApproachingDeadline(String readerEmail, String bookName, Instant deadLineDate, Instant borrowDate) throws IOException {
        Email from = new Email(sendingEmail);
        String subject = "Крайният срок за връщането на книгата ви наближава!";
        Email to = new Email(readerEmail);
        Content content = new Content("text/plain", "Здравейте. Трябва да" +
                " върнете книгата '"+bookName+"' до "+ formatInstant(deadLineDate)+". "
                +"Вие заехте книгата на: "+formatInstant(borrowDate)+".");
        Mail mail = new Mail(from, subject, to, content);
        this.trySendEmail(mail);
    }

    public void WarnOfOverdueBook(String readerEmail, String bookName, Instant deadLineDate, Instant borrowDate) throws IOException{
        Email from = new Email(sendingEmail);
        String subject = "Трябва да върнете книгата си!";
        Email to = new Email(readerEmail);
        Content content = new Content("text/plain", "Здравейте. Просрочихте " +
                "връщането на книгата '"+bookName+"'. Вие заехте книгата на: "+formatInstant(borrowDate)+". " +
                "Крайният срок за връщането й беше:" +
                " "+ formatInstant(deadLineDate)+". Моля върнете " +
                "книгата си!");
        Mail mail = new Mail(from, subject, to, content);
        this.trySendEmail(mail);
    }

    public void WarnOfAdministrativeSanction(String readerEmail, String bookName, Instant deadLineDate, Instant borrowDate) throws IOException {
        Email from = new Email(sendingEmail);
        String subject = "Трябва да върнете книгата си!!";
        Email to = new Email(readerEmail);
        Content content = new Content("text/plain", "Здравейте. Просрочихте " +
                "връщането на книгата '"+bookName+"'. Вие заехте книгата на: "+formatInstant(borrowDate)+". " +
                "Крайният срок за връщането й беше:" +
                " "+ formatInstant(deadLineDate)+". Моля върнете " +
                "книгата си - " +
                "в противен случай ще последват административни наказания!");
        Mail mail = new Mail(from, subject, to, content);
        this.trySendEmail(mail);
    }
    public void InformUserOfBorrowingSuccess(String readerEmail, String bookName, Instant deadLineDate) throws IOException{
        Email from = new Email(sendingEmail);
        String subject = "Наслаждавайте се на книгата си!";
        Email to = new Email(readerEmail);
        Content content = new Content("text/plain", "Здравейте. Заехте книгата " +
                "'"+bookName+"', като крайният срок за връщането й е : "+ formatInstant(deadLineDate)+".");
        Mail mail = new Mail(from, subject, to, content);
        this.trySendEmail(mail);
    }
    public void InformUserThatABookHeRequestedIsAvailable(String readerEmail, String bookName, Instant attemptToBorrowDate) throws IOException{
        Email from = new Email(sendingEmail);
        String subject = "Уведомление за вече-налична книга";
        Email to = new Email(readerEmail);
        Content content = new Content("text/plain", "Здравейте. Книгата '"+bookName+"', която поискахте да " +
                "вземете на "+formatInstant(attemptToBorrowDate)+", вече е налична.");
        Mail mail = new Mail(from, subject, to, content);
        this.trySendEmail(mail);
    }
    public void InformOfAvailabilityAllRequesters(Long bookId) throws Exception {
        try{
            List<OutOfStock> borrowingAttempts=outOfStockRepository.getAllBorrowingAttemptsForSpecificBook(bookId);
            for (OutOfStock outOfStock:borrowingAttempts) {
                this.InformUserThatABookHeRequestedIsAvailable(outOfStock.reader.getEmail(), outOfStock.book.getTitle(),outOfStock.dateCreated);
                outOfStockRepository.deleteById(outOfStock.id);
            }
        }catch(IOException ioException)
        {
            throw new Exception("Failed to notify readers!");
        }
    }
    private void trySendEmail(Mail mail) throws IOException{
        try{
            Request request = new Request();
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = this.sendGrid.api(request);
        }
        catch(IOException ioException){
            throw ioException;
        }
    }
    private String formatInstant(Instant instant){
        return dateTimeFormatter.format(instant)+" GMT";
    }
}
