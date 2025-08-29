package com.qoormthon.empty_wallet.domain.user.repositroy;


import com.qoormthon.empty_wallet.domain.user.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findBySocialEmail(String socialEmail);
}
