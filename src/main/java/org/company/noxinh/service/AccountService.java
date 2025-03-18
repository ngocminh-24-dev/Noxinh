package org.company.noxinh.service;

import org.company.noxinh.dto.ResponseData;
import org.company.noxinh.dto.authen.request.ChangePasswordRequestDTO;
import org.company.noxinh.dto.authen.request.UpdateAccountResponseDTO;
import org.company.noxinh.dto.authen.response.AccountProfileResponseDTO;
import org.company.noxinh.entity.Account;

import java.security.Principal;

/**
 * The interface User service.
 */
public interface AccountService {
    /**
     * Gets user by id.
     *
     * @return the user by id
     */
    ResponseData<AccountProfileResponseDTO> getAccount();

    /**
     * Change password response data.
     *
     * @param changePasswordRequestDTO the change password request dto
     * @param principal                the principal
     * @return the response data
     */
    ResponseData<?> changePassword(ChangePasswordRequestDTO changePasswordRequestDTO, Principal principal);

    /**
     * Update account response data.
     *
     * @param request the request
     * @return the response data
     */
    ResponseData<UpdateAccountResponseDTO> updateAccount(UpdateAccountResponseDTO request);

    Account findById(Integer id);
    ResponseData<Integer> countAllOrders();
}
