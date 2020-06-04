package com.sparky.trak.authentication.service.scheduler;

import com.sparky.trak.authentication.domain.User;
import com.sparky.trak.authentication.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Collection;

/**
 * The {@link AuthTaskScheduler} is a simple component class that is used to contain any methods that
 * are scheduled for execution at particular points during the life-time of the auth server.
 *
 * @since 1.0.0
 * @author Sparky Studios
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class AuthTaskScheduler {

    private final UserRepository userRepository;

    /**
     * Scheduled task that is executed once an hour, on the hour. Its purpose is to remove any verification codes
     * that have expired for users that have not yet completed the verification process. The expiration time for a
     * verification code is around 24 hours, depending on when the verification code requested. Once a code has expired,
     * the user will have to request a new one.
     */
    @Scheduled(cron = "0 0 * * * *")
    public void removeExpiredVerificationCodesScheduledTask() {
        // Get the name of the current thread that this scheduled task is running on, for debug purposes.
        String currentThread = Thread.currentThread().getName();
        log.info("---------- Running scheduled task: Remove expired verification codes on thread: " + currentThread + " ----------");

        // Retrieve all of the accounts that aren't verified and verification code is older than 24 hours.
        Collection<User> users = userRepository
                .findByVerifiedIsFalseAndVerificationExpiryDateBefore(LocalDateTime.now().minusDays(1));

        // Loop through each user that matches the criteria and remove both the expiry date and verification code.
        // A new verification code won't be automatically generated, the user will have to request a new one.
        users.forEach(user -> {
            user.setVerificationExpiryDate(null);
            user.setVerificationCode(null);

            // Save the updated entity, but leave them as not verified.
            userRepository.save(user);
        });

        log.info("---------- Finished scheduled task: Remove expired verification codes on thread: " + currentThread + " ----------");
    }
}