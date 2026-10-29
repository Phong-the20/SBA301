package fu.edu.sba301.assignment02.entity;

/**
 * Author: Tran The Phong
 * Student ID: CE190157
 * Assignment: 02 - FUNewsManagementSystem
 */

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "SystemAccount")
public class SystemAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer accountId;

    @NotBlank(message = "Account name is required")
    @Column(nullable = false, length = 100, columnDefinition = "NVARCHAR(100)")
    private String accountName;

    @NotBlank(message = "Account email is required")
    @Email(message = "Invalid email format")
    @Column(nullable = false, unique = true, length = 100)
    private String accountEmail;

    @NotNull(message = "Account role is required (1: Admin, 2: Staff)")
    @Column(nullable = false)
    private Integer accountRole; // 1: Admin, 2: Staff

    @NotBlank(message = "Password is required")
    @Column(nullable = false, length = 100)
    private String accountPassword;

    @JsonIgnore
    @OneToMany(mappedBy = "createdBy", fetch = FetchType.LAZY)
    private List<NewsArticle> createdNews = new ArrayList<>();

    public SystemAccount() {
    }

    public SystemAccount(Integer accountId, String accountName, String accountEmail, Integer accountRole,
            String accountPassword) {
        this.accountId = accountId;
        this.accountName = accountName;
        this.accountEmail = accountEmail;
        this.accountRole = accountRole;
        this.accountPassword = accountPassword;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountEmail() {
        return accountEmail;
    }

    public void setAccountEmail(String accountEmail) {
        this.accountEmail = accountEmail;
    }

    public Integer getAccountRole() {
        return accountRole;
    }

    public void setAccountRole(Integer accountRole) {
        this.accountRole = accountRole;
    }

    public String getAccountPassword() {
        return accountPassword;
    }

    public void setAccountPassword(String accountPassword) {
        this.accountPassword = accountPassword;
    }

    public List<NewsArticle> getCreatedNews() {
        return createdNews;
    }

    public void setCreatedNews(List<NewsArticle> createdNews) {
        this.createdNews = createdNews;
    }
}
