package edu.jm.rest.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.Set;

@Entity
@Table(name = "users")
public class User implements UserDetails {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   private String password;
   private String firstName;
   private String lastName;
   private int age;
   @Column(name = "email", nullable = false, unique = true)
   private String email;
   private boolean isEnabled;
   private boolean isAccountNonExpired;
   private boolean isAccountNonLocked;
   private boolean isCredentialsNonExpired;

   @ManyToMany(cascade = {CascadeType.ALL})
   @JoinTable(
           name = "user_roles",
           joinColumns = { @JoinColumn(name = "user_id")},
           inverseJoinColumns = { @JoinColumn(name = "role_id")}
   )
   private Set<Role> roles;

   public User() {}

   public User(String firstName, String lastName, String email) {
      this.firstName = firstName;
      this.lastName = lastName;
      this.email = email;
   }

   public User(UserDto userDto){
      this.firstName = userDto.getFirstName();
      this.lastName = userDto.getLastName();
      this.age = userDto.getAge();
      this.email = userDto.getEmail();
      this.password = userDto.getPassword();
   }

   public Long getId() {
      return id;
   }

   public void setId(Long id) {
      this.id = id;
   }

   public String getFirstName() {
      return firstName;
   }

   public void setFirstName(String firstName) {
      this.firstName = firstName;
   }

   public String getLastName() {
      return lastName;
   }

   public void setLastName(String lastName) {
      this.lastName = lastName;
   }

   public int getAge() {
      return age;
   }

   public void setAge(int age) {
      this.age = age;
   }

   public String getEmail() {
      return email;
   }

   public void setEmail(String email) {
      this.email = email;
   }

   @Override
   public String getPassword() {
      return this.password;
   }

   public void setPassword(String password) {
      this.password = password;
   }

   public Set<Role> getRoles() {
      return roles;
   }

   public void setRoles(Set<Role> roles) {
      this.roles = roles;
   }

   @Override
   public Collection<? extends GrantedAuthority> getAuthorities() {
      return roles;
   }

   @Override
   public String getUsername() {
      return email;
   }

   public void setAccountNonExpired(boolean accountNonExpired) {
      isAccountNonExpired = accountNonExpired;
   }

   @Override
   public boolean isAccountNonExpired() {
      return isAccountNonExpired;
   }

   public void setIsAccountNonLocked(boolean accountNonLocked) {
      isAccountNonLocked = accountNonLocked;
   }

   @Override
   public boolean isAccountNonLocked() {
      System.out.println("Called. AccountNonLocked? " + this.isAccountNonLocked);
      System.out.println(this);
      return this.isAccountNonLocked;
   }

   public void setIsCredentialsNonExpired(boolean credentialsNonExpired) {
      isCredentialsNonExpired = credentialsNonExpired;
   }

   @Override
   public boolean isCredentialsNonExpired() {
      return isCredentialsNonExpired;
   }

   @Override
   public boolean isEnabled() {
      return isEnabled;
   }

   public void setEnabled(boolean enabled) {
      isEnabled = enabled;
   }

   @Override
   public String toString() {
      return "User{" +
              "id=" + id +
              ", password='" + password + '\'' +
              ", firstName='" + firstName + '\'' +
              ", lastName='" + lastName + '\'' +
              ", age='" + age + '\'' +
              ", email='" + email + '\'' +
              ", isEnabled=" + isEnabled +
              ", isAccountNonExpired=" + isAccountNonExpired +
              ", isAccountNonLocked=" + isAccountNonLocked +
              ", isCredentialsNonExpired=" + isCredentialsNonExpired +
              ", roles=" + roles +
              '}';
   }
}
