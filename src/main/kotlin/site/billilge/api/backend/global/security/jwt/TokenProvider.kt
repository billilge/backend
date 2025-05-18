package site.billilge.api.backend.global.security.jwt

import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Header
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import jakarta.xml.bind.DatatypeConverter
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import site.billilge.api.backend.domain.member.entity.Member
import site.billilge.api.backend.domain.member.enums.Role
import site.billilge.api.backend.global.exception.ApiException
import site.billilge.api.backend.global.exception.GlobalErrorCode
import site.billilge.api.backend.global.security.UserAuthInfoService
import java.security.Key
import java.time.Duration
import java.util.*
import javax.crypto.spec.SecretKeySpec

@Service
class TokenProvider(
    @Value("\${jwt.issuer}")
    private val issuer: String,
    @Value("\${jwt.secret_key}")
    private val secretKey: String,
    private val userAuthInfoService: UserAuthInfoService
) {
    val signatureAlgorithm = SignatureAlgorithm.HS256

    private fun createSecretKey(): Key {
        val apiKeySecretBytes = DatatypeConverter.parseBase64Binary(secretKey)
        return SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.jcaName)
    }

    fun generateToken(member: Member, expiredAt: Duration): String {
        val now = Date()
        return makeToken(Date(now.time + expiredAt.toMillis()), member.studentId, member.role, member.name)
    }

    private fun makeToken(expiry: Date, studentId: String, role: Role, name: String): String {
        val now = Date()

        return Jwts.builder()
            .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
            .setIssuer(issuer)
            .setIssuedAt(now)
            .setExpiration(expiry)
            .setSubject(studentId)
            .claim("role", role.name)
            .claim("name", name)
            .signWith(createSecretKey(), signatureAlgorithm)
            .compact()
    }

    fun validToken(token: String?): Boolean {
        try {
            if (token == null) return false

            Jwts.parserBuilder()
                .setSigningKey(createSecretKey())
                .build()
                .parseClaimsJws(token)

            return true
        } catch (e: ExpiredJwtException) {
            throw ApiException(GlobalErrorCode.EXPIRED_ACCESS_TOKEN)
        } catch (e1: Exception) {
            return false
        }
    }

    fun getAuthentication(token: String): Authentication {
        val claims = getClaims(token)
        val studentId = claims.subject
        val userDetails = userAuthInfoService.loadUserByUsername(studentId)

        return UsernamePasswordAuthenticationToken(userDetails, "", userDetails.authorities)
    }

    fun getClaims(token: String): Claims {
        return Jwts.parserBuilder()
            .setSigningKey(createSecretKey())
            .build()
            .parseClaimsJws(token)
            .body
    }
}