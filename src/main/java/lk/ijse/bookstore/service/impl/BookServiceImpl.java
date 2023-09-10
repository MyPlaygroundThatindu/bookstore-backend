package lk.ijse.bookstore.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lk.ijse.bookstore.dto.BookCreateDTO;
import lk.ijse.bookstore.entity.Book;
import lk.ijse.bookstore.repository.BookRepository;
import lk.ijse.bookstore.service.BookService;



@Service
public class BookServiceImpl implements BookService {
    private BookRepository bookRepository;

    @Autowired
    public BookServiceImpl (BookRepository bookRepository){
        this.bookRepository = bookRepository;
    }

    @Value("${upload.directory}")
    private String uploadDirectory;

    @Override
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @Override
    public Book getBookById(Long id) {
        return bookRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Book ID " + id + " not found"));
    }

    @Override
    public Book createBook(Book book) {
        Book newBook = new Book(0, book.getTitle(), book.getAuthor(), book.getISBN10(), book.getDescription(), book.getPrice(), book.getQuantity(), "TEST", book.isFeatured(), book.getSubCategory(), null);

        return bookRepository.save(newBook);        
    }

    @Override
    public Book updateBook(Long id, Book book) {
        Book existingBook= getBookById(id);

        existingBook.setTitle(book.getTitle());
        existingBook.setAuthor(book.getAuthor());
        existingBook.setISBN10(book.getISBN10());
        existingBook.setDescription(book.getDescription());
        existingBook.setPrice(book.getPrice());
        existingBook.setQuantity(book.getQuantity());
        existingBook.setSubCategory(book.getSubCategory());

        return bookRepository.save(existingBook);
    }

    @Override
    public Book updateBookCoverImage(Long id, BookCreateDTO bookCreateDTO) {
        Book existingBook= getBookById(id);

        if(existingBook != null) {
            
            MultipartFile file = bookCreateDTO.getCoverImage();
            String filename = file.getOriginalFilename();
            String filePath = uploadDirectory + File.separator + filename;

            try {
                file.transferTo(new File(filePath));
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (IOException e) {            
                e.printStackTrace();
            }

            existingBook.setCoverImage(filename);
            return bookRepository.save(existingBook);
        }
        return null;
    }

    @Override
    public void deleteBook(Long id) {
        if(bookRepository.existsById(id)){
            bookRepository.deleteById(id);
        }else{
            throw new NoSuchElementException("Book ID " + id + " not found");
        } 
    }

    @Override
    public List<Book> getBooksByCategoryId(Long categoryId) {
        return bookRepository.findBooksByCategoryId(categoryId);
    }
    
    @Override
    public List<Book> getBooksBySubCategoryId(Long subCategoryId) {
        return bookRepository.findBooksBySubCategoryId(subCategoryId);
    }
}
