package com.syrnik.service;

import java.util.Optional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.syrnik.dto.RegisterDTO;
import com.syrnik.dto.UserClientDTO;
import com.syrnik.enums.Role;
import com.syrnik.exception.EntityNotFoundException;
import com.syrnik.model.branch.Client;
import com.syrnik.model.central.Branch;
import com.syrnik.model.central.User;
import com.syrnik.repository.branch.ClientRepository;
import com.syrnik.repository.central.BranchRepository;
import com.syrnik.repository.central.UserRepository;
import com.syrnik.security.UserDetailsImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
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

    @Transactional("chainedTransactionManager")
    public UserClientDTO getFullUserInfo(Integer id) {
        User user = userRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        Client client = clientRepository.findByUserId(id).orElseThrow(EntityNotFoundException::new);
        return UserClientDTO
              .builder()
              .id(user.getId())
              .login(user.getLogin())
              .role(user.getRole())
              .branchName(user.getBranch().getName())
              .firstName(client.getFirstName())
              .secondName(client.getSecondName())
              .lastName(client.getLastName())
              .email(client.getEmail())
              .dateOfBirth(client.getDateOfBirth())
              .placeOfBirth(client.getPlaceOfBirth())
              .gender(client.getGender())
              .build();
    }

    public Optional<User> findById(Integer id) {
        return userRepository.findById(id);
    }

    public UserDetailsImpl getLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.getPrincipal() instanceof UserDetailsImpl userDetails) {
            return userDetails;
        }
        log.warn("Not authenticated");
        throw new IllegalStateException("Not authenticated");
    }
}
