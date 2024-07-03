package roomescape.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Role {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Enumerated(EnumType.STRING)
    private RoleType name;

    public Role() {
    }

    public Role(Long id, RoleType name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public RoleType getName() {
        return name;
    }
}
