package com.hospital.hms.controller.api;

import com.hospital.hms.dto.BillDto;
import com.hospital.hms.service.BillService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bills")
public class BillApiController {

    private final BillService billService;

    @Autowired
    public BillApiController(BillService billService) {
        this.billService = billService;
    }

    @GetMapping
    public ResponseEntity<List<BillDto>> getAllBills(@RequestParam(value = "sortBy", required = false) String sortBy) {
        return ResponseEntity.ok(billService.getAllBills(sortBy));
    }

    @GetMapping("/search")
    public ResponseEntity<List<BillDto>> searchBills(@RequestParam("query") String query) {
        return ResponseEntity.ok(billService.searchBills(query));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BillDto> getBillById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(billService.getBillById(id));
    }

    @PostMapping
    public ResponseEntity<BillDto> createBill(@Valid @RequestBody BillDto billDto) {
        return new ResponseEntity<>(billService.createBill(billDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BillDto> updateBill(@PathVariable("id") Long id, @Valid @RequestBody BillDto billDto) {
        return ResponseEntity.ok(billService.updateBill(id, billDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBill(@PathVariable("id") Long id) {
        billService.deleteBill(id);
        return ResponseEntity.noContent().build();
    }
}
