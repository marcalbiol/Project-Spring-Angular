package com.example.demo.models.entity;


import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "usuarios")
public class Usuario implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // id auto incrementable
    private Long id;

    @Column(unique = true, length = 20)
    private String username;
    @Column(length = 60)
    private String password;
    private Boolean enabled;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    // cuando se borra algun usuario asociado a roles tambien se elimina estos
    @JoinTable(name = "usuarios_roles", joinColumns = @JoinColumn(name = "usuarios_id"), inverseJoinColumns = @JoinColumn(name = "roles_id"), // talba intermedia
            uniqueConstraints = {@UniqueConstraint(columnNames = {"usuarios_id", "roles_id"})}) // da un error
    private List<Rol> roles;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enables) {
        this.enabled = enables;
    }

    public List<Rol> getRoles() {
        return roles;
    }

    public void setRoles(List<Rol> roles) {
        this.roles = roles;
    }
}
