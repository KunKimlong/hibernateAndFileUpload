package edu.etec.hibernateemployee.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "employees")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "name", nullable = false,length = 30)
    private String name;
    @Column(name = "gender")
    private String gender;
    @Column(name = "salary")
    private float salary;
    @Column(name = "profile")
    private String profile;
}
