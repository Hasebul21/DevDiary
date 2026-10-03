package com.example.techblogapi.entity;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.Date;
import java.util.Objects;

@Entity
public class Users {


    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;

    @NotBlank
    @NotEmpty
    @NotNull
    @Column(unique = true)
    @Email(regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}",flags = Pattern.Flag.CASE_INSENSITIVE, message = "Email have to be unique")
    private String email;

    @NotBlank
    @NotEmpty
    @NotNull
    private String password;

    @NotBlank
    @NotEmpty
    @NotNull
    @Pattern(regexp = "^[A-Za-z\\s]+$"  , message = "Invalid name. name must have length atleast 3 character UpperCase or LowerCase")
    private String name;

    @NotBlank
    @NotEmpty
    @NotNull
    @Pattern(regexp = "[0-9]{11}" ,message = "Invalid Phone. Number must have exactly 11 digit")
    private String phone;

    // USER or ADMIN
    private String role="USER";

    @Temporal(TemporalType.TIMESTAMP)
    private Date CreatedDate=new Date(System.currentTimeMillis());

    public Users() {

    }

    public Users(int id, String email, String password, String name, String phone) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.phone = phone;
    }

    public String getRole() {

        return role;
    }

    public void setRole(String role) {

        this.role = role;
    }

    public void setId(int id) {

        this.id = id;
    }

    public void setEmail(String email) {

        this.email = email;
    }

    public void setPassword(String password) {

        this.password = password;
    }
    public void setCreatedDate(Date createdDate) {

        CreatedDate = createdDate;
    }

    public int getId() {

        return id;
    }
    public String getEmail() {

        return email;
    }

    public String getPassword() {

        return password;
    }
    public Date getCreatedDate() {

        return CreatedDate;
    }
    public String getName() {

        return name;
    }

    public void setName(String name) {

        this.name = name;
    }

    public String getPhone() {

        return phone;
    }

    public void setPhone(String phone) {

        this.phone = phone;
    }

    @Override
    public int hashCode() {
        int hash=7;
        return id*hash + email.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        Users other = (Users) obj;
        if (other.id == this.id&& Objects.equals(other.email, this.email)) return true;
        return false;
    }

}
