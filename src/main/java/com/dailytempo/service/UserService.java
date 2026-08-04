package com.dailytempo.service;

import com.dailytempo.domain.AuthProvider;
import com.dailytempo.domain.User;
import com.dailytempo.dto.RegistrationRequest;
import com.dailytempo.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Service
public class UserService {

    private static final DateTimeFormatter BIRTH_DATE_FORMAT =
            DateTimeFormatter.ofPattern("yyyy.MM.dd");

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
    }

    @Transactional
    public void register(RegistrationRequest request) {
        String username = normalized(request.username());
        String password = normalized(request.password());
        String name = normalized(request.name());

        if (username.isBlank() || password.isBlank() || name.isBlank()) {
            throw new IllegalArgumentException("아이디, 비밀번호, 이름은 필수입니다.");
        }
        if (!password.equals(normalized(request.passwordConfirm()))) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("이미 사용 중인 아이디입니다.");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setName(name);
        user.setPhone(normalized(request.phone()));
        user.setProvider(AuthProvider.LOCAL);

        String birthDate = normalized(request.birthDate());
        if (!birthDate.isBlank()) {
            try {
                user.setBirthDate(LocalDate.parse(birthDate, BIRTH_DATE_FORMAT));
            } catch (DateTimeParseException exception) {
                throw new IllegalArgumentException("생년월일은 yyyy.MM.dd 형식으로 입력해 주세요.");
            }
        }

        userRepository.save(user);
    }

    private String normalized(String value) {
        return value == null ? "" : value.trim();
    }
}
