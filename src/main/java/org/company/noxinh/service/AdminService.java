package org.company.noxinh.service;

import org.company.noxinh.dto.ResponseData;
import org.company.noxinh.dto.admin.response.AccountResposnseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * The interface Admin service.
 */
public interface AdminService {
    /**
     * Gets accounts.
     *
     * @param name            the name
     * @param phone           the phone
     * @param specificAddress the specific address
     * @param pageable        the pageable
     * @return the accounts
     */
    ResponseData<Page<AccountResposnseDTO>> getAccounts(String name, String phone, String specificAddress, Pageable pageable);

    /**
     * Change default password response data.
     *
     * @param accountId the account id
     * @return the response data
     */
    ResponseData<String> changeDefaultPassword(Integer accountId);
}
