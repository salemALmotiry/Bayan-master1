package com.example.bayan.Repostiry;

import com.example.bayan.Model.MyUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface AuthRepository extends JpaRepository<MyUser, Integer> {

    MyUser findMyUserByUsername(String username);
    MyUser findMyUserById(Integer id);

}
