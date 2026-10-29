package fu.edu.sba301.assignment02.service;

/**
 * Author: Tran The Phong
 * Student ID: CE190157
 * Assignment: 02 - FUNewsManagementSystem
 */

import fu.edu.sba301.assignment02.dto.AccountDto;
import java.util.List;

public interface IAccountService {
    List<AccountDto> getAllAccounts();
    AccountDto getAccountById(Integer id);
    AccountDto createAccount(AccountDto dto);
    AccountDto updateAccount(Integer id, AccountDto dto);
    void deleteAccount(Integer id);
    List<AccountDto> searchAccounts(String keyword);
    AccountDto updateProfile(Integer id, AccountDto dto);
}

