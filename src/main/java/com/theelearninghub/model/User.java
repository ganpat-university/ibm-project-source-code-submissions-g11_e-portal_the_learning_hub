package com.theelearninghub.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

@Entity
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "`user`")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int iduser;
    @NotNull
    @Column(unique=true)
    private String email;
    @NotNull
    private String password;
    private String firstName;
    private String lastName;
    private String nickName;
    private String interest;
    private String description;
    private Integer phoneNumber;
    @Column(length = 1_000_000)
    private byte[] photoBinary;
    @ManyToOne
    @JoinColumn(name = "AdminId", referencedColumnName = "idadmin", nullable = false)
    private Admin admin;
    @OneToMany(mappedBy = "creator")
    private List<Course> createdCourses;
    @OneToMany(mappedBy = "creator")
    private List<Material> materials;
    @ManyToMany
    @JoinTable(name = "user_has_room", joinColumns =
    @JoinColumn(name = "user_iduser", referencedColumnName = "iduser", nullable = false), inverseJoinColumns =
    @JoinColumn(name = "room_idroom", referencedColumnName = "idroom", nullable = false))
    private List<Room> rooms;
    @ManyToMany
    @JoinTable(name = "user_attends_course", joinColumns =
    @JoinColumn(name = "user_iduser", referencedColumnName = "iduser", nullable = false), inverseJoinColumns =
    @JoinColumn(name = "course_idcourse", referencedColumnName = "idcourse", nullable = false))
    private List<Course> attends;
    @ManyToMany
    @JoinTable(name = "user_wishlist_course", joinColumns =
    @JoinColumn(name = "user_iduser", referencedColumnName = "iduser", nullable = false), inverseJoinColumns =
    @JoinColumn(name = "course_idcourse", referencedColumnName = "idcourse", nullable = false))
    private List<Course> wishes;

    @Override
    public String toString() {
        return "User{" +
                "iduser=" + iduser +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", nickName='" + nickName + '\'' +
                ", interest='" + interest + '\'' +
                ", description='" + description + '\'' +
                ", phoneNumber=" + phoneNumber +
                ", photoBinary=" + Arrays.toString(photoBinary) +
                '}';
    }
}
