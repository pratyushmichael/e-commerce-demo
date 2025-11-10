package com.example.ecommerce_demo.controller;

import com.example.ecommerce_demo.dto.AdminSummary;
import com.example.ecommerce_demo.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Admin endpoints: summary & future admin operations.
 */
@RestController
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    /**
     * GET /admin/summary
     * Returns counts, totals, discount codes and total discount amount.
     */
    @GetMapping("/summary")
    public ResponseEntity<AdminSummary> getSummary() {
        AdminSummary summary = adminService.getSummary();
        return ResponseEntity.ok(summary);
    }
}
