package hu.bme.aut.freelancer_spring.service;

import hu.bme.aut.freelancer_spring.model.Package;
import hu.bme.aut.freelancer_spring.model.Transfer;
import hu.bme.aut.freelancer_spring.repository.TransferRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransferServiceImpTest {

    @InjectMocks
    TransferServiceImp transferService;

    @Mock
    TransferRepository transferRepository;

    @Test
    void getPackagesTest() {
        // given
        Transfer transfer = mock(Transfer.class);
        List<Package> expectedPackages = Arrays.asList(new Package(), new Package());

        when(transfer.getPackages()).thenReturn(expectedPackages);
        when(transferRepository.findById(anyLong())).thenReturn(Optional.of(transfer));

        // when
        List<Package> result = transferService.getPackages(5L);

        // then
        assertEquals(2, result.size());
        assertEquals(expectedPackages, result);
        verify(transferRepository, times(1)).findById(anyLong());
    }

    @Test
    void getPackagesNotFoundTest() {
        // given
        when(transferRepository.findById(anyLong())).thenReturn(Optional.empty());

        // then
        assertThrows(ResponseStatusException.class, () -> {
            // when
            transferService.getPackages(5L);
        });
    }
}

