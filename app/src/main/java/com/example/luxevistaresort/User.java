package com.example.luxevistaresort;

public class User {
    private String name, email, phone;
    private String base64Image;

    public User(String name, String email, String phone, String base64Image) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.base64Image = base64Image;
    }

    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getBase64Image() { return base64Image; }
}
