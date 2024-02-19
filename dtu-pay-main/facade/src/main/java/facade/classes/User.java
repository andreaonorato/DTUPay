package facade.classes;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@EqualsAndHashCode
public abstract class User {
    private String firstName;
    private String lastName;
    private String cpr;
    private String id;
    private String bankAccount;

    @Override
    public String toString() {
        return String.format("User first name %s, last name %s, cpr %s, id %s", firstName, lastName, cpr, id);
    }

}
