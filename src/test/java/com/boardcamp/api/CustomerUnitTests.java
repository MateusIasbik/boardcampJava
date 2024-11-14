package com.boardcamp.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.boardcamp.api.dtos.CustomersDTO;
import com.boardcamp.api.exceptions.ConflictException;
import com.boardcamp.api.models.CustomersModel;
import com.boardcamp.api.repositories.CustomersRepository;
import com.boardcamp.api.services.CustomersService;

@SpringBootTest
class CustomerUnitTests {
    
    @InjectMocks
    private CustomersService customersService;

    @Mock
    private CustomersRepository customersRepository;


    @Test
    void givenRepeatedCpf_whenCreatingGame_thenThrowsError() {
		
		CustomersDTO customersDto = new CustomersDTO("test", "test", "test");
		CustomersModel existingCustomer = new CustomersModel(1L, "test", "test", "test");
		doReturn(Optional.of(existingCustomer)).when(customersRepository).findByCpf(any());

		ConflictException exception = assertThrows(
			ConflictException.class, 
			() -> customersService.createCustomer(customersDto));

		verify(customersRepository, times(1)).findByCpf(any());
		verify(customersRepository, times(0)).save(any());
		assertNotNull(exception);
		assertEquals("O cpf já existe. Tente outro.", exception.getMessage());
	}

    @Test
    void givenUniqueCustomer_whenCreatingCustomer_thenCreateCustomerSuccessfully() {
		
		CustomersDTO customerDto = new CustomersDTO("test", "test", "test");
		CustomersModel customerCreated = new CustomersModel(1L, "test", "test", "test");
		doReturn(Optional.empty()).when(customersRepository).findByCpf(any());
		doReturn(customerCreated).when(customersRepository).save(any());

		CustomersModel createdCustomer = customersService.createCustomer(customerDto);

		verify(customersRepository, times(1)).findByCpf(any());
		verify(customersRepository, times(1)).save(any());
		verify(customersRepository, times(1)).save(any());
		
		assertNotNull(createdCustomer);
		assertEquals("test", createdCustomer.getName());
		assertEquals("test", createdCustomer.getPhone());
		assertEquals("test", createdCustomer.getCpf());
	}

    @Test
    void givenCpfWithDifferentData_whenCreatingCustomer_thenThrowsError() {

        CustomersDTO customersDTO = new CustomersDTO("testError", "testError", "test");
        CustomersModel existingCustomer = new CustomersModel(1L, "test", "test", "test");

        doReturn(Optional.of(existingCustomer)).when(customersRepository).findByCpf("test");

        ConflictException exception = assertThrows(
            ConflictException.class, 
            () -> customersService.createCustomer(customersDTO));

        assertNotNull(exception);
        assertEquals("O cpf já existe. Tente outro.", exception.getMessage());
    }

}
