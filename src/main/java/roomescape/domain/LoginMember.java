package roomescape.domain;

public class LoginMember {
    private Long id;
    private RoleType roleType;

    public LoginMember(Long id, RoleType roleType) {
        this.id = id;
        this.roleType = roleType;
    }

    public Long getId() {
        return id;
    }

    public RoleType getRoleType() {
        return roleType;
    }
}
