package fu.edu.sba301.assignment02.service;

/**
 * Author: Tran The Phong
 * Student ID: CE190157
 * Assignment: 02 - FUNewsManagementSystem
 */

import fu.edu.sba301.assignment02.dto.AccountDto;
import fu.edu.sba301.assignment02.entity.SystemAccount;
import fu.edu.sba301.assignment02.exception.BusinessException;
import fu.edu.sba301.assignment02.exception.ResourceNotFoundException;
import fu.edu.sba301.assignment02.repository.NewsArticleRepository;
import fu.edu.sba301.assignment02.repository.SystemAccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class AccountService implements IAccountService {

    private final SystemAccountRepository accountRepository;
    private final NewsArticleRepository newsArticleRepository;

    public AccountService(SystemAccountRepository accountRepository, NewsArticleRepository newsArticleRepository) {
        this.accountRepository = accountRepository;
        this.newsArticleRepository = newsArticleRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<AccountDto> getAllAccounts() {
        return accountRepository.findAll().stream()
            .map(this::toDto)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public AccountDto getAccountById(Integer id) {
        SystemAccount account = accountRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Account not found with ID: " + id));
        return toDto(account);
    }

    @Override
    public AccountDto createAccount(AccountDto dto) {
        if (dto == null) {
            throw new BusinessException("Account payload cannot be empty");
        }
        String email = dto.getAccountEmail().trim().toLowerCase();
        if (accountRepository.existsByAccountEmail(email)) {
            throw new BusinessException("Email '" + email + "' is already registered in the system.");
        }
        if (dto.getAccountRole() == null || (dto.getAccountRole() != 1 && dto.getAccountRole() != 2)) {
            throw new BusinessException("Account role must be 1 (Admin) or 2 (Staff).");
        }
        if (dto.getAccountPassword() == null || dto.getAccountPassword().trim().isEmpty()) {
            throw new BusinessException("Password cannot be blank.");
        }

        SystemAccount account = new SystemAccount();
        account.setAccountName(dto.getAccountName().trim());
        account.setAccountEmail(email);
        account.setAccountRole(dto.getAccountRole());
        account.setAccountPassword(dto.getAccountPassword().trim());

        SystemAccount saved = accountRepository.save(account);
        return toDto(saved);
    }

    @Override
    public AccountDto updateAccount(Integer id, AccountDto dto) {
        SystemAccount existing = accountRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Account not found with ID: " + id));

        String newEmail = dto.getAccountEmail().trim().toLowerCase();
        if (!existing.getAccountEmail().equalsIgnoreCase(newEmail) && accountRepository.existsByAccountEmail(newEmail)) {
            throw new BusinessException("Email '" + newEmail + "' is already in use by another account.");
        }

        existing.setAccountName(dto.getAccountName().trim());
        existing.setAccountEmail(newEmail);
        if (dto.getAccountRole() != null) {
            existing.setAccountRole(dto.getAccountRole());
        }
        if (dto.getAccountPassword() != null && !dto.getAccountPassword().trim().isEmpty()) {
            existing.setAccountPassword(dto.getAccountPassword().trim());
        }

        SystemAccount saved = accountRepository.save(existing);
        return toDto(saved);
    }

    @Override
    public void deleteAccount(Integer id) {
        SystemAccount existing = accountRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Account not found with ID: " + id));

        // Critical Business Requirement:
        // "the delete action will delete an account in the case this account is not belong to any news articles (created),
        // if the account is already created any news article, cannot delete."
        boolean hasArticles = newsArticleRepository.existsByCreatedBy_AccountId(id);
        if (hasArticles) {
            throw new BusinessException("Cannot delete account '" + existing.getAccountName() +
                "' (ID: " + id + ") because it has created news articles.");
        }

        accountRepository.delete(existing);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AccountDto> searchAccounts(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllAccounts();
        }
        String cleanKeyword = keyword.trim();
        return accountRepository.findByAccountNameContainingIgnoreCaseOrAccountEmailContainingIgnoreCase(cleanKeyword, cleanKeyword)
            .stream()
            .map(this::toDto)
            .collect(Collectors.toList());
    }

    @Override
    public AccountDto updateProfile(Integer id, AccountDto dto) {
        SystemAccount existing = accountRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Account not found with ID: " + id));

        existing.setAccountName(dto.getAccountName().trim());
        if (dto.getAccountPassword() != null && !dto.getAccountPassword().trim().isEmpty()) {
            existing.setAccountPassword(dto.getAccountPassword().trim());
        }
        SystemAccount saved = accountRepository.save(existing);
        return toDto(saved);
    }

    private AccountDto toDto(SystemAccount entity) {
        return new AccountDto(
            entity.getAccountId(),
            entity.getAccountName(),
            entity.getAccountEmail(),
            entity.getAccountRole(),
            entity.getAccountPassword()
        );
    }
}

