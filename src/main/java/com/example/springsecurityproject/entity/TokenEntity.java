package com.example.springsecurityproject.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * The token entity table is a very important one, it'll save the encrypted secret key for that specified user for the
 * JWT of that user.
 *
 * The column id is auto incremented for every new insert
 * The column token will contain the secret key in base64
 * The column user_id will have the user id linked to that key
 *
 * That table cannot have 2 key for the same user, because at a new login the secret key will be deleted from the db and
 * replaced with a new one.
 *
 */
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class TokenEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false)
    private String token;

    @OneToOne
    @JoinColumn(name="user_id", referencedColumnName="id")
    private UserEntity user;

}
