package com.example.dotabot1.repository;

import com.example.dotabot1.entity.users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerRepository extends JpaRepository<User, Long> {

    User findByChatId(Long telegramId);


}