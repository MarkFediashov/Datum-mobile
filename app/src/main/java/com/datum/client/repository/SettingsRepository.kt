package com.datum.client.repository

import com.datum.client.dto.LoginCredentialsDto
import com.datum.client.dto.RefreshTokenDto

interface SettingsRepository {
    var refreshToken: RefreshTokenDto?
    var credentials: LoginCredentialsDto?
    var endpoint: String?
}