package com.hostel.crm.service

import com.hostel.crm.exception.UnauthorizedException
import com.hostel.crm.repository.AppUserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.jose.jws.MacAlgorithm
import org.springframework.security.oauth2.jwt.JwsHeader
import org.springframework.security.oauth2.jwt.JwtClaimsSet
import org.springframework.security.oauth2.jwt.JwtEncoder
import org.springframework.security.oauth2.jwt.JwtEncoderParameters
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant

@Service
@Transactional(readOnly = true)
class AuthService(
    private val appUserRepository: AppUserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtEncoder: JwtEncoder
) {
    fun login(username: String, password: String): String {
        val user = appUserRepository.findByUsername(username)
            ?: throw UnauthorizedException("Invalid credentials")

        if (!passwordEncoder.matches(password, user.passwordHash)) {
            throw UnauthorizedException("Invalid credentials")
        }

        val role = user.role
            ?: throw UnauthorizedException("Invalid credentials")

        val now = Instant.now()
        val claims = JwtClaimsSet.builder()
            .subject(user.username)
            .claim("role", role.name.name)
            .issuedAt(now)
            .expiresAt(now.plusSeconds(3600))
            .build()

        val header = JwsHeader.with(MacAlgorithm.HS256).build()
        return jwtEncoder.encode(JwtEncoderParameters.from(header, claims)).tokenValue
    }
}