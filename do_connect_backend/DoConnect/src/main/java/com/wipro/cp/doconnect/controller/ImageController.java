package com.wipro.cp.doconnect.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.Clock;
import java.util.Arrays;
import java.util.Optional;
import java.util.Random;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1")
public class ImageController {

	@Value("${image-storage-directory}")
	private Path imageStorageDirectory;

	@Value("${valid-image-extensions}")
	private String[] validImageExtensions;
	
	@PostConstruct
	public void ensureDirectoryExists() throws IOException {
		if (!Files.exists(this.imageStorageDirectory)) {
			Files.createDirectories(this.imageStorageDirectory);
		}
	}
	
	private static Optional<String> getFileExtension(String fileName) {
		final int indexOfLastDot = fileName.lastIndexOf('.');
		if (indexOfLastDot == -1) {
			return Optional.empty();
		} else {
			return Optional.of(fileName.substring(indexOfLastDot + 1));
		}
	}
	
	private boolean isValidFileExtension(String fileExtension) {
		if (fileExtension.isBlank()) {
			return false;
		}
		return Arrays.stream(validImageExtensions).anyMatch(fileExtension::equals);
	}
	
	private static String generateFileName() {
		Random rnd = new Random();
	    int number = rnd.nextInt(99999);
		Clock clock = Clock.systemDefaultZone();
		long milliseconds = clock.millis();
		return String.format("%d%05d", milliseconds, number);
	}

	@PostMapping(value = "/images", produces = MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity<String> uploadImage(@RequestPart(name = "image", required = true) MultipartFile imageFile) throws IOException {
		final String fileExtension = Optional.ofNullable(imageFile.getOriginalFilename()).flatMap(ImageController::getFileExtension).orElse("");
		if (!isValidFileExtension(fileExtension)) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File extension is invalid. Should be either 'png' or 'jpeg'.");
		}
		final String targetFileName = generateFileName() + "." + fileExtension;
		final Path targetPath = this.imageStorageDirectory.resolve(targetFileName);
		try (InputStream in = imageFile.getInputStream()) {
			try (OutputStream out = Files.newOutputStream(targetPath, StandardOpenOption.CREATE)) {
				in.transferTo(out);
			}
		}
		return ResponseEntity.ok(targetFileName);
	}
	
	@GetMapping("/images/{fileName}")
	public ResponseEntity<?> downloadImage(@PathVariable("fileName") String fileName) throws IOException {
		String fileExtension = Optional.ofNullable(fileName).flatMap(ImageController::getFileExtension).orElse("");
		if (!isValidFileExtension(fileExtension)) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File extension is invalid. Should be either 'png' or 'jpeg'.");
		}
		MediaType contentType = (fileExtension.equalsIgnoreCase("jpeg")) ? MediaType.IMAGE_JPEG : MediaType.IMAGE_PNG;
		final Path targetPath = this.imageStorageDirectory.resolve(fileName);
		if (!Files.exists(targetPath)) {
			return ResponseEntity.notFound().build();
		}
		InputStream inputStream = Files.newInputStream(targetPath);
		return ResponseEntity.ok().contentType(contentType).body(new InputStreamResource(inputStream));
	}

}
