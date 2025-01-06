package com.example.bayan.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Border {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 100)
    @NotEmpty(message = "Border name is required")
    @Size(min = 2, max = 100, message = "Border name must be between 2 and 100 characters")
    private String name;

    @Column(length = 50)
    private String type;

    @Column(length = 250)
    private String location;


    @OneToMany(mappedBy = "border", cascade = CascadeType.ALL)
    private Set<Post> posts;


    @ManyToMany(mappedBy = "borders")
    private Set<CustomsBroker> customsBrokers;

}
