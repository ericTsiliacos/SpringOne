package io.pivotal.notes.crypto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class EncryptionTool {

    private BCryptPasswordEncoder bcryptEncoder;

    @Autowired
    public EncryptionTool(BCryptPasswordEncoder bcryptEncoder) {
        this.bcryptEncoder = bcryptEncoder;
    }

    public String encrypt(String password) {
        return bcryptEncoder.encode(password);
    }

    public boolean matches(String password, String encryptedPassword) {
        return bcryptEncoder.matches(password, encryptedPassword);
    }
}
