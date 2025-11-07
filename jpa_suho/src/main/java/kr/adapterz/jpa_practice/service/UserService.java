package kr.adapterz.jpa_practice.service;

import kr.adapterz.jpa_practice.dto.user.UserInfo;
import kr.adapterz.jpa_practice.entity.User;
import kr.adapterz.jpa_practice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private User getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자ID 입니다."));

        if (user.isDeleted()) {
            throw new IllegalArgumentException("탈퇴한 사용자입니다.");
        }

        return user;
    }

    @Transactional
    public Long create(String email, String password, String nickname, String profileImageUrl) {

        // 이메일 중복 체크
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }

        // 닉네임 중복 체크
        if (userRepository.existsByNickname(nickname)) {
            throw new IllegalArgumentException("이미 사용 중인 닉네임입니다.");
        }

        String encodedPassword = passwordEncoder.encode(password);

        User user = new User(email, encodedPassword, nickname, profileImageUrl);

        return userRepository.save(user).getId();

    }


    public UserInfo findById(Long id) {

        User user = getUserById(id);

        return new UserInfo(user.getEmail(), user.getNickname(), user.getProfileImageUrl());

    }

    @Transactional
    public void deleteById(Long id) {

        User user = getUserById(id);

        user.delete();

    }

    @Transactional
    public void changeNickname(Long id, String newNickname) {

        User user = getUserById(id);

        // 본인의 현재 닉네임과 다를 때만 중복 체크
        if (!user.getNickname().equals(newNickname)) {
            if (userRepository.existsByNickname(newNickname)) {
                throw new IllegalArgumentException("이미 사용 중인 닉네임입니다.");
            }
        }

        user.changeNickname(newNickname);

    }

    @Transactional
    public void changePassword(Long id, String newPassword) {

        User user = getUserById(id);

        user.changePassword(passwordEncoder.encode(newPassword));

    }

    public Long login(String email, String password) {

        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new IllegalArgumentException("이메일 또는 비밀번호가 일치하지 않습니다.");
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("이메일 또는 비밀번호가 일치하지 않습니다.");
        }

        if (user.isDeleted()) {
            throw new IllegalArgumentException("탈퇴한 사용자입니다.");
        }

        return user.getId();

    }

}
