package com.dynalar.dynalar.controller;

import com.dynalar.dynalar.model.patient.Document;
import com.dynalar.dynalar.model.patient.Patient;
import com.dynalar.dynalar.respository.DocumentRepository;
import com.dynalar.dynalar.respository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/document")
public class DocumentController {

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private PatientRepository patientRepository;

    private final Path rootPath = Paths.get("uploads");

    @PostMapping(value = "/patient/{patientId}/upload", consumes = "multipart/form-data")
    public ResponseEntity<Document> uploadFile(
            @PathVariable Long patientId,
            @RequestParam("file") MultipartFile file,
            @RequestParam("type") String type) {

        Patient patient = patientRepository.findById(patientId).orElse(null);
        
        if (patient == null) {
        	return ResponseEntity.notFound().build();
        }

        try {
            Document doc = new Document();
            doc.setPatient(patient);
            doc.setDocumentType(type);
            doc.setCreationDate(LocalDateTime.now());
            doc = documentRepository.save(doc);

            String relativePath = savePhysicalFile(file, patientId, doc.getId());

            doc.setDocumentUrl(relativePath);
            documentRepository.save(doc);

            return ResponseEntity.ok(doc);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    

    @GetMapping("/{documentId}")
    public ResponseEntity<Resource> getDocument(@PathVariable Long documentId) {
        Document doc = documentRepository.findById(documentId).orElse(null);
        if (doc == null || doc.getDocumentUrl() == null) return ResponseEntity.notFound().build();

        try {
            Path filePath = rootPath.resolve(doc.getDocumentUrl());
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() || resource.isReadable()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_PDF)
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (MalformedURLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<Document>> getPatientDocuments(@PathVariable Long patientId) {
        return ResponseEntity.ok(documentRepository.findByPatientId(patientId));
    }

    
    private String savePhysicalFile(MultipartFile file, Long patientId, Long docId) throws IOException {
        String patientFolder = "patient_" + patientId;
        Path targetFolder = this.rootPath.resolve(patientFolder);

        if (!Files.exists(targetFolder)) {
            Files.createDirectories(targetFolder);
        }

        String originalName = file.getOriginalFilename();
        String extension = "";
        if (originalName != null && originalName.contains(".")) {
            extension = originalName.substring(originalName.lastIndexOf("."));
        }

        String fileName = docId + extension;
        Path targetPath = targetFolder.resolve(fileName);

        Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

        return patientFolder + "/" + fileName;
    }
}