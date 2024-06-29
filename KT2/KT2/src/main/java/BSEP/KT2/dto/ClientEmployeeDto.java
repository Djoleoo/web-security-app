package BSEP.KT2.dto;

import BSEP.KT2.model.enums.Role;

public class ClientEmployeeDto {
    private String username;
    private String firstName;
    private String lastName;
    private Role role;
    
    public ClientEmployeeDto(String username,String firstName,String lastName, Role role) {
        this.username = username;
        this.firstName=firstName;
        this.lastName=lastName;
        this.role=role;
    }
    
    public String getUsername(){
        return username;
    }

    public String getFirstName(){
        return firstName;
    }

    public String getLastName(){
        return lastName;
    }

    public Role getRole(){
        return role;
    }

    
    // Getters and setters
}