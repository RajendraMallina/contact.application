package itm.codingmaxima.contact.application.model;

import jakarta.persistence.*;

import java.util.Objects;


@Entity
public class AppRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, unique = true)
    private String role;

    public AppRole() {}

    public AppRole(String role) {
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public String getRoleName() {
        return role;
    }

    public void setRoleName(String role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        AppRole appRole = (AppRole) o;
        return id == appRole.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

}
