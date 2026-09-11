package com.hospital.hms.controller.api;

import com.hospital.hms.dto.PrescriptionDto;
import com.hospital.hms.service.PrescriptionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prescriptions")
public class PrescriptionApiController {

    private final PrescriptionService prescriptionService;

    @Autowired
    public PrescriptionApiController(PrescriptionService prescriptionService) {
        this.prescriptionService = prescriptionService;
    }

    @GetMapping
    public ResponseEntity<List<PrescriptionDto>> getAllPrescriptions(@RequestParam(value = "sortBy", required = false) String sortBy) {
        return ResponseEntity.ok(prescriptionService.getAllPrescriptions(sortBy));
    }

    @GetMapping("/search")
    public ResponseEntity<List<PrescriptionDto>> searchPrescriptions(@RequestParam("query") String query) {
        return ResponseEntity.ok(prescriptionService.searchPrescriptions(query));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PrescriptionDto> getPrescriptionById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(prescriptionService.getPrescriptionById(id));
    }

    @PostMapping
    public ResponseEntity<PrescriptionDto> createPrescription(@Valid @RequestBody PrescriptionDto prescriptionDto) {
        return new ResponseEntity<>(prescriptionService.createPrescription(prescriptionDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PrescriptionDto> updatePrescription(@PathVariable("id") Long id, @Valid @RequestBody PrescriptionDto prescriptionDto) {
        return ResponseEntity.ok(prescriptionService.updatePrescription(id, prescriptionDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePrescription(@PathVariable("id") Long id) {
        prescriptionService.deletePrescription(id);
        return ResponseEntity.noContent().build();
    }
}
