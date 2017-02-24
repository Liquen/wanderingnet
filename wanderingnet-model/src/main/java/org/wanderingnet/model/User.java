package org.wanderingnet.model;

/**
 * Created by guillermoblascojimenez on 07/03/16.
 */
public class User extends AbstractNamedEntity {

    private String email;

    private String passwordHash;

    private String passwordSalt;

    public User() {
    }

    public User(long id) {
        super(id);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getPasswordSalt() {
        return passwordSalt;
    }

    public void setPasswordSalt(String passwordSalt) {
        this.passwordSalt = passwordSalt;
    }
}
