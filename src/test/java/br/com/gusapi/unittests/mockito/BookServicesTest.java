package br.com.gusapi.unittests.mockito;

import br.com.gusapi.data.vo.v1.BookVO;
import br.com.gusapi.model.Book;
import br.com.gusapi.repositories.BookRepository;
import br.com.gusapi.services.BookServices;
import br.com.gusapi.unittests.mapper.mocks.MockBook;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class BookServicesTest {

    MockBook input;

    @Mock
    BookRepository repository;

    @InjectMocks
    private BookServices service;

    @BeforeEach
    void setUpMocks() {
        input = new MockBook();
        MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void findById() {
        Book entity = input.mockEntity(1);
        entity.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(entity));

        BookVO result = service.findById(1L);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getLinks());
        assertTrue(result.toString().contains("links: [</api/book/v1/1>;rel=\"self\"]"));
        assertEquals("Title Test1", result.getTitle());
        assertEquals("Author Test1", result.getAuthor());
        assertNotNull(result.getLaunchDate());
        assertEquals(1, result.getPrice());

    }

    @Test
    void findAll() {
    }

    @Test
    void create() {
        Book entity = input.mockEntity(1);

        Book persisted = entity;
        persisted.setId(1L);

        BookVO vo = input.mockVO(1);
        vo.setId(1L);

        when(repository.save(entity)).thenReturn(persisted);

        BookVO result = service.create(vo);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getLinks());
        assertTrue(result.toString().contains("links: [</api/book/v1/1>;rel=\"self\"]"));
        assertEquals("Title Test1", result.getTitle());
        assertEquals("Author Test1", result.getAuthor());
        assertNotNull(result.getLaunchDate());
        assertEquals(1, result.getPrice());
    }

    @Test
    void update() {
        Book entity = input.mockEntity(1);
        entity.setId(1L);

        Book persisted = entity;

        BookVO vo = input.mockVO(1);
        vo.setId(1L);

        when(repository.findById(1L)).thenReturn(Optional.of(entity));
        when(repository.save(entity)).thenReturn(persisted);

        BookVO result = service.update(vo);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getLinks());
        assertTrue(result.toString().contains("links: [</api/book/v1/1>;rel=\"self\"]"));
        assertEquals("Title Test1", result.getTitle());
        assertEquals("Author Test1", result.getAuthor());
        assertNotNull(result.getLaunchDate());
        assertEquals(1, result.getPrice());
    }

    @Test
    void delete() {
        Book entity = input.mockEntity(1);
        entity.setId(1L);

        when(repository.findById(1L)).thenReturn(Optional.of(entity));

        service.delete(1L);
    }
}