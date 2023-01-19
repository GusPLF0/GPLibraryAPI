package br.com.gusapi.controllers;

import br.com.gusapi.data.vo.v1.security.AccountCredentialsVO;
import br.com.gusapi.services.AuthServices;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication EndPoint", description = "Endpoints for Authenticate")
public class AuthController {

	@Autowired
	AuthServices authServices;

	@Operation(summary = "Authenticates a user and return a token")
	@PostMapping(value = "/signin")
	public ResponseEntity signIn(@RequestBody AccountCredentialsVO data) {
		if (data == null || data.getUsername() == null || data.getUsername().isBlank() ||
			data.getPassword().isBlank() || data.getPassword() == null){
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid client request");
		}

		ResponseEntity token = authServices.signIn(data);

		if(token == null){
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid client request");
		}

		return token;


	}
}
