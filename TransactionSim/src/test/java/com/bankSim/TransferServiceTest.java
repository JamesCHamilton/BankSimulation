package com.bankSim;

import com.bankSim.dto.requests.TransferRequest;
import com.bankSim.dto.responses.TransferResponse;
import com.bankSim.dto.tasks.TransferTask;
import com.bankSim.exceptions.InsufficientFundsException;
import com.bankSim.exceptions.ResourceNotFoundException;
import com.bankSim.exceptions.UnauthorizedAccessException;
import com.bankSim.model.Account;
import com.bankSim.model.Transfer;
import com.bankSim.queue.TransferQueue;
import com.bankSim.repos.AccountRepository;
import com.bankSim.repos.TransferRepository;
import com.bankSim.repos.UserRepository;
import com.bankSim.service.TransferService;
import com.bankSim.utils.Status;
import com.bankSim.utils.TransferTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.context.ApplicationEventPublisher;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransferServiceTest {

    @Mock
    private TransferRepository transferRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @Mock
    private TransferQueue transferQueue;

    @InjectMocks
    private TransferService transferService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testInitializeTransfer_Success() throws Exception {
        TransferRequest request = new TransferRequest(
                1L, 1L, 2L, BigDecimal.valueOf(100.0), TransferTypes.ACCOUNTtoACCOUNTTRANSFER
        );

        Account fromAccount = new Account(1L, "ACT1", "Holder1", "RTN1", "CHECKING", "Bank1");
        fromAccount.setBalance(BigDecimal.valueOf(500.0));

        when(accountRepository.findById(1L)).thenReturn(Optional.of(fromAccount));
        
        // Mock save and queue
        doAnswer(invocation -> {
            Transfer t = invocation.getArgument(0);
            // set ID
            java.lang.reflect.Field idField = Transfer.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(t, 100L);
            return t;
        }).when(transferRepository).save(any(Transfer.class));

        TransferResponse response = transferService.initializeTransfer(1L, request);

        assertNotNull(response);
        assertEquals(Status.QUEUED, response.getStatus());
        assertEquals(100L, response.getTransactionId());
        verify(transferQueue, times(1)).enqueue(any(TransferTask.class));
    }

    @Test
    void testInitializeTransfer_Unauthorized() {
        TransferRequest request = new TransferRequest(
                1L, 1L, 2L, BigDecimal.valueOf(100.0), TransferTypes.ACCOUNTtoACCOUNTTRANSFER
        );

        // Account belongs to user 99L, not 1L
        Account fromAccount = new Account(99L, "ACT1", "Holder1", "RTN1", "CHECKING", "Bank1");

        when(accountRepository.findById(1L)).thenReturn(Optional.of(fromAccount));

        assertThrows(UnauthorizedAccessException.class, () -> {
            transferService.initializeTransfer(1L, request);
        });
    }

    @Test
    void testProcessTransferTask_InsufficientFunds() {
        TransferTask task = new TransferTask(100L, 1L, 2L, BigDecimal.valueOf(1000.0), TransferTypes.ACCOUNTtoACCOUNTTRANSFER);

        Account fromAccount = new Account(1L, "ACT1", "Holder1", "RTN1", "CHECKING", "Bank1");
        fromAccount.setBalance(BigDecimal.valueOf(100.0)); // low balance
        Account toAccount = new Account(2L, "ACT2", "Holder2", "RTN2", "SAVINGS", "Bank1");

        Transfer transfer = new Transfer(1L, 2L, BigDecimal.valueOf(1000.0), TransferTypes.ACCOUNTtoACCOUNTTRANSFER);

        when(accountRepository.findById(1L)).thenReturn(Optional.of(fromAccount));
        when(accountRepository.findById(2L)).thenReturn(Optional.of(toAccount));
        when(transferRepository.findById(100L)).thenReturn(Optional.of(transfer));

        assertThrows(InsufficientFundsException.class, () -> {
            transferService.processTransferTask(task);
        });

        assertEquals(Status.FAILED, transfer.getStatus());
        verify(transferRepository, times(1)).save(transfer);
    }
}
