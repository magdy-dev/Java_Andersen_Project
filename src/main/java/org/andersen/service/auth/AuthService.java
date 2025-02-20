package org.andersen.service.auth;

import org.andersen.entity.users.Admin;
import org.andersen.entity.users.Customer;

public interface AuthService {
    Customer loginCustomer(String username, String password);
    Admin loginAdmin(String username, String password);
    void registerUser(String username, String password);
}
