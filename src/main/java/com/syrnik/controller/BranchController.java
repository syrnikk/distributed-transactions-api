package com.syrnik.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.syrnik.dto.BranchDTO;
import com.syrnik.dto.RegisterDTO;
import com.syrnik.service.BranchService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")

public class BranchController {
    private final BranchService branchService;
    @GetMapping("/branch")
    public ResponseEntity<List<BranchDTO>> register() {
        log.info("REST Get branches");
        List<BranchDTO> branches = branchService.findAll();
        return ResponseEntity.ok(branches);
    }
}
