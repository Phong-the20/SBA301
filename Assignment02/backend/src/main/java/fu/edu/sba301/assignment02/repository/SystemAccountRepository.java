package fu.edu.sba301.assignment02.repository;

/**
 * Author: Tran The Phong
 * Student ID: CE190157
 * Assignment: 02 - FUNewsManagementSystem
 */

import fu.edu.sba301.assignment02.entity.SystemAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface SystemAccountRepository extends JpaRepository<SystemAccount, Integer> {
    Optional<SystemAccount> findByAccountEmail(String email);
    Optional<SystemAccount> findByAccountEmailAndAccountPassword(String email, String password);
    boolean existsByAccountEmail(String email);
    List<SystemAccount> findByAccountNameContainingIgnoreCaseOrAccountEmailContainingIgnoreCase(String name, String email);

    @Query(value = "SELECT * FROM system_account WHERE account_name COLLATE SQL_Latin1_General_CP1_CI_AI LIKE CONCAT('%', :kw, '%') OR account_email LIKE CONCAT('%', :kw, '%')", nativeQuery = true)
    List<SystemAccount> searchByNameOrEmail(@Param("kw") String keyword);
}

