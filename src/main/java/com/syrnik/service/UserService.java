package com.syrnik.service;

import java.util.Optional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.syrnik.dto.RegisterDTO;
import com.syrnik.enums.Role;
import com.syrnik.model.branch.Client;
import com.syrnik.model.central.Branch;
import com.syrnik.model.central.User;
import com.syrnik.repository.branch.ClientRepository;
import com.syrnik.repository.central.BranchRepository;
import com.syrnik.repository.central.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final BranchRepository branchRepository;
    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional("chainedTransactionManager")
    public void registerUser(RegisterDTO registerDTO) {
        String branchName = registerDTO.getBranchName();
        Optional<Branch> optionalBranch = branchRepository.findByName(branchName);
        Branch branch = optionalBranch.orElseThrow(() -> new IllegalArgumentException("Branch not found"));

        User user = new User();
        user.setLogin(registerDTO.getLogin());
        user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        user.setRole(Role.USER);
        user.setBranch(branch);

        userRepository.save(user);

        Client client = new Client();
        client.setFirstName(registerDTO.getFirstName());
        client.setSecondName(registerDTO.getSecondName());
        client.setLastName(registerDTO.getLastName());
        client.setEmail(registerDTO.getEmail());
        client.setDateOfBirth(registerDTO.getDateOfBirth());
        client.setPlaceOfBirth(registerDTO.getPlaceOfBirth());
        client.setGender(registerDTO.getGender());
        client.setUserId(user.getId());

        clientRepository.save(client);
    }
}
