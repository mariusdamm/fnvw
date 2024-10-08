package de.marius.fnvw.dto;

public class AppUserDto {

    private long id;
    private String name;
    private String username;

    public AppUserDto(long id, String name, String username) {
        this.id = id;
        this.name = name;
        this.username = username;
    }

    public AppUserDto() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
