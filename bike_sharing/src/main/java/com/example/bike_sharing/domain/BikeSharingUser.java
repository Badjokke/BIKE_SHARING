package com.example.bike_sharing.domain;

import jakarta.persistence.*;

import java.util.Objects;

/**
 * Represents a single user of the service.
 */
@Entity
@Table(name="Users")
public class BikeSharingUser {

    /**
     * Unique key of table
     */
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    /**
     * Full name of the user.
     */
    private String name;

    private String password;
    /**
     * Email address of the user, used for log in.
     */
    private String emailAddress;
    /**
     * Role of the user.
     */
    private Role role;

    public BikeSharingUser(Long id, String name, String emailAddress, Role role, String password) {
        this.name = name;
        this.emailAddress = emailAddress;
        this.role = role;
        this.id = id;
        this.password = password;
    }
    public BikeSharingUser(String name, String emailAddress, Role role, String password) {
        this.name = name;
        this.emailAddress = emailAddress;
        this.role = role;
        this.password = password;
    }
    public BikeSharingUser(String name, String emailAddress){
        this.name = name;
        this.emailAddress = emailAddress;
    }

    public BikeSharingUser() {
    }

    public String getPassword(){return this.password;}
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public Role getRole() {
        return role;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BikeSharingUser user)) return false;
        return Objects.equals(id, user.id) && Objects.equals(name, user.name) && Objects.equals(emailAddress, user.emailAddress) && role == user.role;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, emailAddress, role);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", emailAddress='" + emailAddress + '\'' +
                ", role=" + role +
                '}';
    }

    public enum Role {
        /**
         * Regular user
         */
        REGULAR("REGULAR"),
        /**
         * Serviceman, can do everything that regular users but also maintains bikes
         */
        SERVICEMAN("SERVICEMAN");
        private final String value;

        Role(String value){
            this.value = value;
        }
        public String getValue(){
            return this.value;
        }
    }
}