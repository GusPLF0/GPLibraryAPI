package br.com.gusapi.controllers;

import br.com.gusapi.data.vo.v1.UploadFileResponseVO;
import br.com.gusapi.services.FileStorageService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Tag(name = "File Endpoint")
@RestController
@RequestMapping("/api/file/v1")
public class FileController {

	@Autowired
	private FileStorageService service;

	@PostMapping("/uploadFile")
	public UploadFileResponseVO uploadFile(@RequestParam("file") MultipartFile file) {

		String fileName = service.storeFile(file);

		String fileDowloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
			.path("/api/file/v1/dowloadFile/")
			.path(fileName)
			.toUriString();

		return new UploadFileResponseVO(fileName, fileDowloadUri, file.getContentType(), file.getSize());
	}

}
