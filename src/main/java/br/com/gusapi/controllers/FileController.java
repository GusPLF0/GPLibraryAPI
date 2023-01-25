package br.com.gusapi.controllers;

import br.com.gusapi.data.vo.v1.UploadFileResponseVO;
import br.com.gusapi.services.FileStorageService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

	@GetMapping("/dowloadFile/{filename:.+}")
	public ResponseEntity<Resource> dowloadFile(@PathVariable String filename, HttpServletRequest request) {

		Resource resource = service.loadFileAsResource(filename);

		String contentType = "";

		try {
			contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (contentType.isBlank()) contentType = "application/octet-stream";

		return ResponseEntity.ok()
			.contentType(MediaType.parseMediaType(contentType))
			.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
			.body(resource);
	}

}
