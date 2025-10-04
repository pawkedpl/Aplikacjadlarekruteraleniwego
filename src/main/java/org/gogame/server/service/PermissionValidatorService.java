package org.gogame.server.service;

import lombok.RequiredArgsConstructor;
import org.gogame.server.domain.entities.UserEntity;
import org.gogame.server.repositories.UserRepository;
import org.hibernate.JDBCException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PermissionValidatorService {

    private final JwtService jwtService;

    private final UserRepository userRepo;

    public boolean validateUserId(Long userId, String token) {
        var username = jwtService.extractUsername(token.substring(7));  // remove "Bearer " from token

        UserEntity user;
        try {
            user = userRepo.findById(userId).orElseThrow();
        } catch (NullPointerException | JDBCException ex) {
            return false;
        }
        return user.getNickname().equals(username);
    }
}
