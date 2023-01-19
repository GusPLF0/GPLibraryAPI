package br.com.gusapi.services;

import br.com.gusapi.controllers.BookController;
import br.com.gusapi.data.vo.v1.BookVO;
import br.com.gusapi.exceptions.ResourceNotFoundException;
import br.com.gusapi.mapper.DozerMapper;
import br.com.gusapi.model.Book;
import br.com.gusapi.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class BookServices {

    private final Logger logger = Logger.getLogger(BookServices.class.getName());

    @Autowired
    BookRepository repository;


    public BookServices(BookRepository repository) {

        this.repository = repository;
    }

    public BookVO findById(Long id) {

        logger.info("Finding one book!");

        Book book = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));

        BookVO vo = DozerMapper.parseObject(book, BookVO.class);
        vo.add(linkTo(methodOn(BookController.class).findById(id)).withSelfRel());
        return vo;
    }

    public List<BookVO> findAll() {

        logger.info("Finding all books!");

        List<BookVO> bookVOS = DozerMapper.parseListObject(repository.findAll(), BookVO.class);

        bookVOS
                .stream()
                .forEach(p -> p.add(linkTo(methodOn(BookController.class).findById(p.getKey())).withSelfRel()));
        return bookVOS;
    }

    public BookVO create(BookVO book) {

        logger.info("Creating one book!");

        Book bookConverted = DozerMapper.parseObject(book, Book.class);

        BookVO bookVO = DozerMapper.parseObject(repository.save(bookConverted), BookVO.class);
        bookVO.add(linkTo(methodOn(BookController.class).findById(bookVO.getKey())).withSelfRel());
        return bookVO;
    }

    public BookVO update(BookVO book) {

        logger.info("Updating book!");

        Book bookFound = repository.findById(book.getKey())
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));


        bookFound.setAuthor(book.getAuthor());
        bookFound.setPrice(book.getPrice());
        bookFound.setLaunchDate(book.getLaunchDate());
        bookFound.setTitle(book.getTitle());

        BookVO bookVO = DozerMapper.parseObject(repository.save(bookFound), BookVO.class);
        bookVO.add(linkTo(methodOn(BookController.class).findById(bookVO.getKey())).withSelfRel());
        return bookVO;
    }

    public void delete(Long id) {

        logger.info("Deleting book!");

        Book bookFound = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));

        repository.delete(bookFound);
    }

}
