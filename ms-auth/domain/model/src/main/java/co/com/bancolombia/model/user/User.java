package co.com.bancolombia.model.user;

import co.com.bancolombia.model.user.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class User {

    private String id;

    private String username;

    private String password;

    private boolean active;

    private Set<Role> roles;
}
