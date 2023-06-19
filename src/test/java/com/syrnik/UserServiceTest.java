package com.syrnik;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.syrnik.dto.RegisterDTO;
import com.syrnik.model.branch.Client;
import com.syrnik.model.central.Branch;
import com.syrnik.model.central.User;
import com.syrnik.repository.branch.ClientRepository;
import com.syrnik.repository.central.BranchRepository;
import com.syrnik.repository.central.UserRepository;
import com.syrnik.service.UserService;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private BranchRepository branchRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void testRegisterUser() {
        // Mock branch repository
        String branchName = "Branch 1";
        Branch branch = new Branch();
        branch.setName(branchName);
        when(branchRepository.findByName(branchName)).thenReturn(Optional.of(branch));

        // Mock password encoder
        String encodedPassword = "encodedPassword";
        when(passwordEncoder.encode(anyString())).thenReturn(encodedPassword);

        // Create a RegisterDTO object with test data
        RegisterDTO registerDTO = new RegisterDTO();
        registerDTO.setBranchName(branchName);
        registerDTO.setLogin("testuser");
        registerDTO.setPassword("password");
        registerDTO.setFirstName("John");
        registerDTO.setLastName("Doe");
        // Set other properties...

        // Call the registerUser method
        userService.registerUser(registerDTO);

        // Verify that the save method was called on userRepository and clientRepository
        verify(userRepository, times(1)).save(any(User.class));
        verify(clientRepository, times(1)).save(any(Client.class));
    }

    @Test
    void testRegisterUser_InvalidBranch() {
        // Mock branch repository to return Optional.empty()
        when(branchRepository.findByName(anyString())).thenReturn(Optional.empty());

        // Create a RegisterDTO object with test data
        RegisterDTO registerDTO = new RegisterDTO();
        registerDTO.setBranchName("Non-existent Branch");
        registerDTO.setLogin("testuser");
        registerDTO.setPassword("password");
        registerDTO.setFirstName("John");
        registerDTO.setLastName("Doe");
        // Set other properties...

        // Call the registerUser method and assert that it throws an IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> userService.registerUser(registerDTO));

        // Verify that the save method was not called on userRepository and clientRepository
        verify(userRepository, never()).save(any(User.class));
        verify(clientRepository, never()).save(any(Client.class));
    }
}