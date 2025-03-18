package org.company.noxinh.repository;

import org.company.noxinh.dto.admin.response.AccountResposnseDTO;
import org.company.noxinh.entity.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * The interface Account repository.
 */
@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {
    /**
     * Find by phone optional.
     *
     * @param phone the phone
     * @return the optional
     */
    Optional<Account> findByPhone(String phone);

    /**
     * Find first by phone optional.
     *
     * @param phone the phone
     * @return the optional
     */
    Optional<Account> findFirstByPhone(String phone);

    /**
     * Find accounts page.
     *
     * @param name            the name
     * @param phone           the phone
     * @param specificAddress the specific address
     * @param pageable        the pageable
     * @return the page
     */
    @Query(value = "SELECT new  org.company.noxinh.dto.admin.response.AccountResposnseDTO(a.id, a.name, a.phone, a.specificAddress, d.name, p.name, w.name)" +
            "FROM Account a " +
            "JOIN District d ON a.districtId = d.id " +
            "JOIN Province p ON a.provinceId = p.id " +
            "JOIN Ward w ON a.wardId = w.id " +
            "WHERE (:name IS NULL OR a.name LIKE CONCAT('%', :name, '%')) " +
            "AND (:phone IS NULL OR a.phone LIKE CONCAT('%', :phone, '%')) " +
            "AND (:specificAddress IS NULL OR a.specificAddress LIKE CONCAT('%', :specificAddress, '%'))")
    Page<AccountResposnseDTO> findAccounts(@Param("name") String name,
                                           @Param("phone") String phone,
                                           @Param("specificAddress") String specificAddress,
                                           Pageable pageable);

    /**
     * Find by email optional.
     *
     * @param email the email
     * @return the optional
     */
    Optional<Account> findByEmail(String email);

    /**
     * Find first by email optional.
     *
     * @param email the email
     * @return the optional
     */
    Optional<Account> findFirstByEmail(String email);
}
