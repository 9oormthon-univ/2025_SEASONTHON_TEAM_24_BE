package com.qoormthon.empty_wallet.domain.user.repository;


import com.qoormthon.empty_wallet.domain.user.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findBySocialEmail(String socialEmail);

  @Query("select u.character.code from User u where u.id = :userId")
  Optional<String> findCharacterCodeById(Long userId);
}
