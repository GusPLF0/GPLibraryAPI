package br.com.gusapi.services;

import br.com.gusapi.controllers.PersonController;
import br.com.gusapi.data.vo.v1.PersonVO;
import br.com.gusapi.exceptions.ResourceNotFoundException;
import br.com.gusapi.mapper.ApiMapper;
import br.com.gusapi.model.Person;
import br.com.gusapi.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

// Spring encara como objeto runtime
@Service
public class PersonServices {

    private final Logger logger = Logger.getLogger(PersonServices.class.getName());

    @Autowired
    PersonRepository repository;


    public PersonServices(PersonRepository repository) {

        this.repository = repository;
    }

    public PersonVO findById(Long id) {

        logger.info("Finding one person!");

        Person person = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));

        PersonVO vo = ApiMapper.parseObject(person, PersonVO.class);
        vo.add(linkTo(methodOn(PersonController.class).findById(id)).withSelfRel());
        return vo;
    }

    public List<PersonVO> findAll() {

        logger.info("Finding all people!");

        List<PersonVO> personVOS = ApiMapper.parseListObject(repository.findAll(), PersonVO.class);
        personVOS
                .stream()
                .forEach(p -> p.add(linkTo(methodOn(PersonController.class).findById(p.getId())).withSelfRel()));
        return personVOS;
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

    public void delete(Long id) {

        logger.info("Deleting person!");

        Person personFound = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));

        repository.delete(personFound);
    }

}
