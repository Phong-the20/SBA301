package fu.edu.sba301.assignment02;

/**
 * Author: Tran The Phong
 * Student ID: CE190157
 * Assignment: 02 - FUNewsManagementSystem
 */

import fu.edu.sba301.assignment02.dto.AccountDto;
import fu.edu.sba301.assignment02.entity.SystemAccount;
import fu.edu.sba301.assignment02.exception.BusinessException;
import fu.edu.sba301.assignment02.repository.NewsArticleRepository;
import fu.edu.sba301.assignment02.repository.SystemAccountRepository;
import fu.edu.sba301.assignment02.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTests {

    @Mock
    private SystemAccountRepository accountRepository;

    @Mock
    private NewsArticleRepository newsArticleRepository;

    @InjectMocks
    private AccountService accountService;

    private SystemAccount sampleAccount;

    @BeforeEach
    void setUp() {
        sampleAccount = new SystemAccount(10, "Test Staff", "teststaff@news.com", 2, "secret123");
    }

    @Test
    @DisplayName("Create account throws exception when email already exists")
    void testCreateAccountDuplicateEmail() {
        AccountDto dto = new AccountDto(null, "Test Staff", "teststaff@news.com", 2, "secret123");
        when(accountRepository.existsByAccountEmail("teststaff@news.com")).thenReturn(true);

        BusinessException ex = assertThrows(BusinessException.class, () -> accountService.createAccount(dto));
        assertTrue(ex.getMessage().contains("already registered"));
        verify(accountRepository, never()).save(any());
    }

    @Test
    @DisplayName("Create account successfully when email is unique")
    void testCreateAccountSuccess() {
        AccountDto dto = new AccountDto(null, "New Staff", "new@news.com", 2, "pass123");
        when(accountRepository.existsByAccountEmail("new@news.com")).thenReturn(false);
        when(accountRepository.save(any(SystemAccount.class))).thenAnswer(invocation -> {
            SystemAccount acc = invocation.getArgument(0);
            acc.setAccountId(11);
            return acc;
        });

        AccountDto created = accountService.createAccount(dto);
        assertNotNull(created);
        assertEquals(11, created.getAccountId());
        assertEquals("new@news.com", created.getAccountEmail());
    }

    @Test
    @DisplayName("Delete account throws BusinessException when account has created news articles")
    void testDeleteAccountBlockedByNewsArticles() {
        when(accountRepository.findById(10)).thenReturn(Optional.of(sampleAccount));
        // Critical requirement: account already created news articles -> cannot delete
        when(newsArticleRepository.existsByCreatedBy_AccountId(10)).thenReturn(true);

        BusinessException ex = assertThrows(BusinessException.class, () -> accountService.deleteAccount(10));
        assertTrue(ex.getMessage().contains("has created news articles"));
        verify(accountRepository, never()).delete(any());
    }

    @Test
    @DisplayName("Delete account succeeds when account has no news articles")
    void testDeleteAccountSuccessWhenNoNewsArticles() {
        when(accountRepository.findById(10)).thenReturn(Optional.of(sampleAccount));
        when(newsArticleRepository.existsByCreatedBy_AccountId(10)).thenReturn(false);

        assertDoesNotThrow(() -> accountService.deleteAccount(10));
        verify(accountRepository, times(1)).delete(sampleAccount);
    }
}
