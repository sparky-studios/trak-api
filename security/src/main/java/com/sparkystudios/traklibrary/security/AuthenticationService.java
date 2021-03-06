package com.sparkystudios.traklibrary.security;

import org.springframework.security.core.Authentication;

public interface AuthenticationService {

    /**
     * Retrieves the current {@link Authentication} information from Spring Security for the user
     * that is currently accessing the Trak Library API.
     *
     * @return The {@link Authentication} information of the current user.
     */
    Authentication getAuthentication();

    /**
     * Retrieves the current JWT that has been used to invoke the current request. When retrieving the
     * token, it is assumed that the method has already been accessed by the authenticated user.
     *
     * @return The current JWT of the authenticated user.
     */
    String getToken();

    /**
     * Checks to see if the user that is currently interacting with the API matches the user ID provided.
     * The primary purpose of this method is to ensure that the user is only changing information that
     * is directly associated with their user credentials, therefore not allowing them to change information
     * that may be directly relate to other users, which may result in unwanted results for them.
     *
     * The exemption to this rule is if the credentials provided have an admin role, if the user is an admin,
     * validation based on the user ID is ignored.
     *
     * @param userId The user ID to validate matches the current user accessing the API.
     *
     * @return <code>true</code> if the current user is either an admin or matches the information provided.
     */
    boolean isCurrentAuthenticatedUser(long userId);
}
