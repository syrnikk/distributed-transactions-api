package com.syrnik.service;

import java.util.List;
import org.springframework.stereotype.Service;

import com.syrnik.dto.BranchDTO;
import com.syrnik.model.central.Branch;
import com.syrnik.repository.central.BranchRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BranchService {
    private final BranchRepository branchRepository;

    public List<BranchDTO> findAll() {
        List<Branch> branches = branchRepository.findAll();
        return branches
              .stream()
              .map(branch -> new BranchDTO(branch.getId(), branch.getName(), branch.getAddress()))
              .toList();
    }
}
