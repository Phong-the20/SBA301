package fu.edu.sba301.assignment02.service;

/**
 * Author: Tran The Phong
 * Student ID: CE190157
 * Assignment: 02 - FUNewsManagementSystem
 */

import fu.edu.sba301.assignment02.dto.LoginRequest;
import fu.edu.sba301.assignment02.dto.LoginResponse;
import fu.edu.sba301.assignment02.entity.SystemAccount;
import fu.edu.sba301.assignment02.exception.BusinessException;
import fu.edu.sba301.assignment02.repository.SystemAccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AuthService implements IAuthService {

    private final SystemAccountRepository accountRepository;

    public AuthService(SystemAccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        if (request == null || request.getAccountEmail() == null || request.getAccountPassword() == null) {
            throw new BusinessException("Email and password cannot be empty");
        }

        SystemAccount account = accountRepository.findByAccountEmail(request.getAccountEmail().trim().toLowerCase())
            .orElseThrow(() -> new BusinessException("Invalid email or password"));

        if (!account.getAccountPassword().equals(request.getAccountPassword().trim())) {
            throw new BusinessException("Invalid email or password");
        }

        return new LoginResponse(
            account.getAccountId(),
            account.getAccountName(),
            account.getAccountEmail(),
            account.getAccountRole()
        );
    }
}
