package fu.edu.sba301.assignment03.service;

/**
 * Author: Tran The Phong
 * Student ID: CE190157
 * Assignment: 03 - FUMiniHotelSystem
 */

import fu.edu.sba301.assignment03.dto.AuthRequest;
import fu.edu.sba301.assignment03.dto.AuthResponse;
import fu.edu.sba301.assignment03.dto.RegisterRequest;

public interface IAuthService {
    AuthResponse login(AuthRequest request);
    AuthResponse register(RegisterRequest request);
}
