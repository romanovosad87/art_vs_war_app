package com.example.artvswar.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@ToString
@Table(name = "admins")
public class Admin extends User {
    @Column(nullable = false)
    private String fullName;
}
