package com.dev.template_crud.repository;

// import org.hibernate.annotations.Parent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.*;

import com.dev.template_crud.model.Token;

@Repository
public interface TokenRepository extends JpaRepository<Token,Integer> {
    Token findByid(int id);
    
    @Query(value = "select token.* from token join users on token.user_id = users.id where token.token = :token", nativeQuery = true)
    public Token findTokenByTokenName(@Param("token") String tokenName);
}
