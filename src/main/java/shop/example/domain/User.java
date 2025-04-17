package shop.example.domain;

import jakarta.persistence.*;
import lombok.*;
import shop.example.enums.Gender;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class User extends AbstractEntity {

    @Column(name = "fullname")
    private String fullName;

    @Column(name = "firstname")
    private String firstname;

    @Column(name = "lastname")
    private String lastname;

    @Column(name = "address")
    private String address;

    @Column(name = "email",unique = true,columnDefinition = "VARCHAR(255) COLLATE utf8mb4_unicode_ci")
    private String email;

    @Column(name = "password")
    private String password;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_role",joinColumns = @JoinColumn(name = "user_id",nullable = false),
    inverseJoinColumns = @JoinColumn(name = "role_id",nullable = false))
    private List<Role> roles= new ArrayList<>();

    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY,cascade ={CascadeType.MERGE,CascadeType.PERSIST},orphanRemoval = true)
    private List<Order> orders = new ArrayList<>();

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    private Gender gender;







}
