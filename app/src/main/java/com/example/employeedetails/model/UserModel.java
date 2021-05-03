package com.example.employeedetails.model;

public class UserModel {

    String Name, Id, Company, Image;

    public UserModel() {
    }

    public UserModel(String Name, String Id, String Company, String Image) {
        this.Name = Name;
        this.Id = Id;
        this.Company = Company;
        this.Image = Image;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getId() {
        return Id;
    }

    public void setId(String Id) {
        this.Id = Id;
    }

    public String getCompany() {
        return Company;
    }

    public void setCompany(String Company) {
        this.Company = Company;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String Image) {
        this.Image = Image;
    }
}
