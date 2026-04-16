package com.dynalar.dynalar.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dynalar.dynalar.model.Material;
import com.dynalar.dynalar.respository.MaterialRepository;

@RestController
@RequestMapping("/material")
public class MaterialController {
	
	@Autowired
	private MaterialRepository materialRepository;
	
	@PutMapping("/{id}/decrease-stock")
	public ResponseEntity<Material> decreaseStock(@PathVariable Long id, @RequestParam Integer quantity) {
		try {
			Optional<Material> materialOpt = materialRepository.findById(id);
			if (materialOpt.isEmpty()) {
				return ResponseEntity.notFound().build();
			}

			Material material = materialOpt.get();
			if (material.getAvailableStock() < quantity) {
				return ResponseEntity.badRequest().build();
			}

			material.setAvailableStock(material.getAvailableStock() - quantity);
			return ResponseEntity.ok(materialRepository.save(material));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@PutMapping("/{id}/increase-stock")
	public ResponseEntity<Material> increaseStock(@PathVariable Long id, @RequestParam Integer quantity) {
		try {
			Optional<Material> materialOpt = materialRepository.findById(id);
			if (materialOpt.isEmpty()) {
				return ResponseEntity.notFound().build();
			}

			Material material = materialOpt.get();
			material.setAvailableStock(material.getAvailableStock() + quantity);
			return ResponseEntity.ok(materialRepository.save(material));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	
}
