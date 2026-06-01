package Models;

public interface Human {
    enum Gender {
        MALE, FEMALE
    }

    int getId();
    void setId(int id);

    String getName();
    void setName(String name);

    Gender getGender();
    void setGender(Gender gender);

    double getAge();
    void setAge(double age);
}
