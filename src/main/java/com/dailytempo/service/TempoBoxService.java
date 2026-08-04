package com.dailytempo.service;

import com.dailytempo.domain.TempoBox;
import com.dailytempo.domain.User;
import com.dailytempo.dto.TempoBoxRequest;
import com.dailytempo.repository.TempoBoxRepository;
import com.dailytempo.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TempoBoxService {

    private final TempoBoxRepository tempoBoxRepository;
    private final UserRepository userRepository;

    public TempoBoxService(TempoBoxRepository tempoBoxRepository, UserRepository userRepository) {
        this.tempoBoxRepository = tempoBoxRepository;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public List<TempoBox> findAll(String username) {
        return tempoBoxRepository.findAllByUserUsernameOrderByCreatedAtAsc(username);
    }

    @Transactional(readOnly = true)
    public TempoBox findOwned(Long id, String username) {
        return tempoBoxRepository.findByIdAndUserUsername(id, username)
                .orElseThrow(() -> new IllegalArgumentException("템포 박스를 찾을 수 없습니다."));
    }

    @Transactional
    public TempoBox create(String username, TempoBoxRequest request) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        TempoBox tempoBox = new TempoBox();
        tempoBox.setUser(user);
        tempoBox.setName(request.name().trim());
        tempoBox.setType(request.type());
        tempoBox.setColor(request.color().toLowerCase());
        return tempoBoxRepository.save(tempoBox);
    }
}
