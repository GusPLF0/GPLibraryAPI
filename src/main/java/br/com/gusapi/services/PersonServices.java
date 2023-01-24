package br.com.gusapi.services;

import br.com.gusapi.controllers.PersonController;
import br.com.gusapi.data.vo.v1.PersonVO;
import br.com.gusapi.exceptions.ResourceNotFoundException;
import br.com.gusapi.mapper.ApiMapper;
import br.com.gusapi.model.Person;
import br.com.gusapi.repositories.PersonRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

// Spring encara como objeto runtime
@Service
public class PersonServices {

    private final Logger logger = Logger.getLogger(PersonServices.class.getName());

    PersonRepository repository;

    PagedResourcesAssembler<PersonVO> assembler;

	@Autowired
	public PersonServices(PersonRepository repository, PagedResourcesAssembler<PersonVO> assembler) {
		this.repository = repository;
		this.assembler = assembler;
	}

	public PersonVO findById(Long id) {

        logger.info("Finding one person!");

        Person person = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));

        PersonVO vo = ApiMapper.parseObject(person, PersonVO.class);
        vo.add(linkTo(methodOn(PersonController.class).findById(id)).withSelfRel());
        return vo;
    }

    public PagedModel<EntityModel<PersonVO>> findAll(Pageable pageable) {

        logger.info("Finding all people!");

		Page<Person> personPage = repository.findAll(pageable);

		Page<PersonVO> personVoPage = personPage.map(p -> ApiMapper.parseObject(p, PersonVO.class));

		personVoPage.map(p -> p.add(linkTo(methodOn(PersonController.class).findById(p.getId())).withSelfRel()));

		Link link = linkTo(methodOn(PersonController.class).findAll(pageable.getPageNumber(), pageable.getPageSize(), "asc")).withSelfRel();

		return assembler.toModel(personVoPage, link);
    }

	public PagedModel<EntityModel<PersonVO>> findByName(String firstname, Pageable pageable) {

		logger.info("Finding all people!");

		Page<Person> personPage = repository.findPersonsByName(firstname, pageable);

		Page<PersonVO> personVoPage = personPage.map(p -> ApiMapper.parseObject(p, PersonVO.class));

		personVoPage.map(p -> p.add(linkTo(methodOn(PersonController.class).findById(p.getId())).withSelfRel()));

		Link link = linkTo(methodOn(PersonController.class).findAll(pageable.getPageNumber(), pageable.getPageSize(), "asc")).withSelfRel();

		return assembler.toModel(personVoPage, link);
	}

    public PersonVO create(PersonVO person) {

        logger.info("Creating one person!");

        Person personConverted = ApiMapper.parseObject(person, Person.class);

        PersonVO personVO = ApiMapper.parseObject(repository.save(personConverted), PersonVO.class);
        personVO.add(linkTo(methodOn(PersonController.class).findById(personVO.getId())).withSelfRel());
        return personVO;
    }

    public PersonVO update(PersonVO person) {

        logger.info("Updating person!");

        Person personFound = repository.findById(person.getId())
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));


        personFound.setFirstName(person.getFirstName());
        personFound.setLastName(person.getLastName());
        personFound.setAddress(person.getAddress());
        personFound.setGender(person.getGender());

        PersonVO personVO = ApiMapper.parseObject(repository.save(personFound), PersonVO.class);
        personVO.add(linkTo(methodOn(PersonController.class).findById(personVO.getId())).withSelfRel());
        return personVO;
    }

	@Transactional
	public PersonVO disablePerson(Long id) {

		logger.info("Disabling one person!");

		repository.disablePerson(id);

		Person person = repository.findById(id)
			.orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));

		PersonVO vo = ApiMapper.parseObject(person, PersonVO.class);
		vo.add(linkTo(methodOn(PersonController.class).findById(id)).withSelfRel());
		return vo;
	}

    public void delete(Long id) {

        logger.info("Deleting person!");

        Person personFound = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));

        repository.delete(personFound);
    }

}
