package br.com.gusapi.controllers;

import br.com.gusapi.data.vo.v1.PersonVO;
import br.com.gusapi.services.PersonServices;
import br.com.gusapi.util.MediaType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/person/v1")
@Tag(name = "People", description = "Endpoints for Manage People")
public class PersonController {

	@Autowired
	private PersonServices service;

	public PersonController(PersonServices service) {
		this.service = service;
	}

	@CrossOrigin(origins = "http://localhost:8080")
	@GetMapping(value = "/{id}",
		produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Operation(summary = "Finds a Person", description = "Finds a Person",
		tags = {"People"},
		responses = {
			@ApiResponse(description = "Success", responseCode = "200",
				content = {
					@Content(
						mediaType = "application/json",
						array = @ArraySchema(schema = @Schema(implementation = PersonVO.class))
					)
				}),
			@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
			@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
			@ApiResponse(description = "Not found", responseCode = "404", content = @Content),
			@ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
		}
	)
	public PersonVO findById(@PathVariable(value = "id") Long id) {

		return service.findById(id);
	}

	@GetMapping(produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Operation(summary = "Finds all People", description = "Finds all People",
		tags = {"People"},
		responses = {
			@ApiResponse(description = "Success", responseCode = "200",
				content = @Content(schema = @Schema(implementation = PersonVO.class))
			),
			@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
			@ApiResponse(description = "No content", responseCode = "204", content = @Content),
			@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
			@ApiResponse(description = "Not found", responseCode = "404", content = @Content),
			@ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
		}
	)
	public ResponseEntity<PagedModel<EntityModel<PersonVO>>> findAll(
		@RequestParam(value = "page", defaultValue = "0") Integer page,
		@RequestParam(value = "limit", defaultValue = "12") Integer limit,
		@RequestParam(value = "direction", defaultValue = "asc") String direction
	) {
		Sort.Direction sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;

		Pageable pageable = PageRequest.of(page, limit, Sort.by(sortDirection, "firstName"));

		return ResponseEntity.ok(service.findAll(pageable));
	}

	@GetMapping(value = "/findByName/{firstName}", produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Operation(summary = "Finds people by name", description = "Finds people by name",
		tags = {"People"},
		responses = {
			@ApiResponse(description = "Success", responseCode = "200",
				content = @Content(schema = @Schema(implementation = PersonVO.class))
			),
			@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
			@ApiResponse(description = "No content", responseCode = "204", content = @Content),
			@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
			@ApiResponse(description = "Not found", responseCode = "404", content = @Content),
			@ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
		}
	)
	public ResponseEntity<PagedModel<EntityModel<PersonVO>>> findPersonsByName(
		@PathVariable(value = "firstName") String firstName,
		@RequestParam(value = "page", defaultValue = "0") Integer page,
		@RequestParam(value = "limit", defaultValue = "12") Integer limit,
		@RequestParam(value = "direction", defaultValue = "asc") String direction
	) {
		Sort.Direction sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;

		Pageable pageable = PageRequest.of(page, limit, Sort.by(sortDirection, "firstName"));

		return ResponseEntity.ok(service.findByName(firstName, pageable));
	}

	@PostMapping(
		consumes = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML},
		produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Operation(summary = "Adds a new Person", description = "Adds a new Person",
		tags = {"People"},
		responses = {
			@ApiResponse(description = "Created", responseCode = "200",
				content = @Content(schema = @Schema(implementation = PersonVO.class))
			),
			@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
			@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
			@ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
		}
	)

	@CrossOrigin(origins = {"http://localhost:8080"})
	public PersonVO create(@RequestBody PersonVO person) {

		return service.create(person);
	}

	@PutMapping(
		consumes = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML},
		produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Operation(summary = "Updates a Person", description = "Updates a Person",
		tags = {"People"},
		responses = {
			@ApiResponse(description = "Updated", responseCode = "200",
				content = @Content(schema = @Schema(implementation = PersonVO.class))
			),
			@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
			@ApiResponse(description = "No content", responseCode = "204", content = @Content),
			@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
			@ApiResponse(description = "Not found", responseCode = "404", content = @Content),
			@ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
		}
	)
	public PersonVO update(@RequestBody PersonVO person) {

		return service.update(person);
	}

	@PatchMapping(value = "/{id}",
		produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Operation(summary = "Disable a Person", description = "Disable a Person using ID",
		tags = {"People"},
		responses = {
			@ApiResponse(description = "Success", responseCode = "200",
				content = {
					@Content(
						mediaType = "application/json",
						array = @ArraySchema(schema = @Schema(implementation = PersonVO.class))
					)
				}),
			@ApiResponse(description = "No Content", responseCode = "204", content = @Content),
			@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
			@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
			@ApiResponse(description = "Not found", responseCode = "404", content = @Content),
			@ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
		}
	)
	public PersonVO disablePerson(@PathVariable(value = "id") Long id) {

		return service.disablePerson(id);
	}

	@DeleteMapping(value = "/{id}")
	@Operation(summary = "Deletes a People", description = "Deletes a People",
		tags = {"People"},
		responses = {
			@ApiResponse(description = "No content", responseCode = "204",
				content = @Content
			),
			@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
			@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
			@ApiResponse(description = "Not found", responseCode = "404", content = @Content),
			@ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
		}
	)
	public ResponseEntity<?> delete(@PathVariable(value = "id") Long id) {

		service.delete(id);

		return ResponseEntity.noContent().build();
	}

}
