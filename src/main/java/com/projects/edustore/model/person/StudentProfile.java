package com.projects.edustore.model.person;


//import com.projects.edustore.model.products.Product;
import jakarta.persistence.*;


@Entity
@Table(name = "student_profiles")
public class StudentProfile {

    @Id
    private Long id;

    @MapsId
    @OneToOne
    @JoinColumn(name = "id")
    private Person person;

    //    special fields for students
    private String schoolPeriod;

//    @OneToMany(mappedBy = "maker")
//    private List<Product> products = new ArrayList<>();

//    constructors

    public StudentProfile() {}

    protected StudentProfile(Person person, String schoolPeriod) { //, List<Product> products
        this.person = person;
        this.schoolPeriod = schoolPeriod;
//        this.products = products;
    }

    public static StudentProfile create(Person person, String schoolPeriod) {
        StudentProfile profile = new StudentProfile(person, schoolPeriod);
        person.setStudentProfile(profile);
        return profile;
    }

    //    getters and setters

//    public List<Product> getProducts() {
//        return products;
//    }
//    public void setProducts(List<Product> products) {
//        this.products = products;
//    }

    public Long getId() {
        return id;
    }

    public String getSchoolPeriod() {
        return schoolPeriod;
    }

    public void setSchoolPeriod(String schoolPeriod) {
        this.schoolPeriod = schoolPeriod;
    }
    public Person getPerson() {
        return person; }

    protected void setPerson(Person person) {
        this.person = person;
    }


}
