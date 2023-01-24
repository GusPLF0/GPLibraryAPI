package br.com.gusapi.services;

import br.com.gusapi.controllers.BookController;
import br.com.gusapi.controllers.PersonController;
import br.com.gusapi.data.vo.v1.BookVO;
import br.com.gusapi.exceptions.ResourceNotFoundException;
import br.com.gusapi.mapper.ApiMapper;
import br.com.gusapi.model.Book;
import br.com.gusapi.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class BookServices {

	private final Logger logger = Logger.getLogger(BookServices.class.getName());

	@Autowired
	BookRepository repository;

	PagedResourcesAssembler<BookVO> assembler;

	public BookServices(BookRepository repository, PagedResourcesAssembler<BookVO> assembler) {
		this.repository = repository;
		this.assembler = assembler;
	}

	public BookVO findById(Long id) {

		logger.info("Finding one book!");

		Book book = repository.findById(id)
			.orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));

		BookVO vo = ApiMapper.parseObject(book, BookVO.class);
		vo.add(linkTo(methodOn(BookController.class).findById(id)).withSelfRel());
		return vo;
	}

	public PagedModel<EntityModel<BookVO>> findAll(Pageable pageable) {

		logger.info("Finding all books!");

		Page<Book> bookPage = repository.findAll(pageable);

		Page<BookVO> bookVoPage = bookPage.map(p -> ApiMapper.parseObject(p, BookVO.class));

		bookVoPage.map(b -> b.add(linkTo(methodOn(BookController.class).findById(b.getId())).withSelfRel()));

		Link link = linkTo(methodOn(PersonController.class).findAll(pageable.getPageNumber(), pageable.getPageSize(), "asc")).withSelfRel();

		return assembler.toModel(bookVoPage, link);
	}

	public BookVO create(BookVO book) {

		logger.info("Creating one book!");

		Book bookConverted = ApiMapper.parseObject(book, Book.class);

		BookVO bookVO = ApiMapper.parseObject(repository.save(bookConverted), BookVO.class);
		bookVO.add(linkTo(methodOn(BookController.class).findById(bookVO.getId())).withSelfRel());
		return bookVO;
	}

	public BookVO update(BookVO book) {

		logger.info("Updating book!");

		Book bookFound = repository.findById(book.getId())
			.orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));


		bookFound.setAuthor(book.getAuthor());
		bookFound.setPrice(book.getPrice());
		bookFound.setLaunchDate(book.getLaunchDate());
		bookFound.setTitle(book.getTitle());

		BookVO bookVO = ApiMapper.parseObject(repository.save(bookFound), BookVO.class);
		bookVO.add(linkTo(methodOn(BookController.class).findById(bookVO.getId())).withSelfRel());
		return bookVO;
	}

	public void delete(Long id) {

		logger.info("Deleting book!");

		Book bookFound = repository.findById(id)
			.orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));

		repository.delete(bookFound);
	}

}
