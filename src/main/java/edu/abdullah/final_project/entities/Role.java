package edu.abdullah.final_project.entities;

import edu.abdullah.final_project.enums.RoleName;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private RoleName name;

    @ManyToMany(mappedBy = "roles")
    private Set<User> users = new HashSet<>();

    // --- Constructors ---
    public Role() {}

    public Role(RoleName name) {
        this.name = name;
    }

    public Role(Long id, RoleName name) {
        this.id = id;
        this.name = name;
    }

    // --- Getters and Setters ---
    public Long getId() {
        return id;
    }

    public void setId(Long id) { this.id = id; }

    public RoleName getName() {
        return name;
    }

    public void setName(RoleName name) { this.name = name; }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) { this.users = users; }
}
