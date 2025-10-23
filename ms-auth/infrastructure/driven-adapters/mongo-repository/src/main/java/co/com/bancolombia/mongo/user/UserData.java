package co.com.bancolombia.mongo.user;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder()
public class UserData {

    @Id
    private String id;

    @Indexed(unique = true)
    private String username;

    private String password;

    private boolean active;

    private List<String> roles;
}
