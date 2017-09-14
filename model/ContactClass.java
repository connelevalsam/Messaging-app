package sample.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by connelblaze on 5/11/17.
 */
public class ContactClass {
    //Declare Student Table Columns
    private IntegerProperty ID;
    private StringProperty  FIRSTNAME;
    private StringProperty  LASTNAME;
    private StringProperty  CLASSGROUP;
    private StringProperty  LEVELGROUP;
    private StringProperty  GENDERGROUP;
    private StringProperty  HEALTHGROUP;
    private IntegerProperty AGEGROUP;

    //Constructor
    public ContactClass() {
        this.ID          = new SimpleIntegerProperty();
        this.FIRSTNAME   = new SimpleStringProperty();
        this.LASTNAME    = new SimpleStringProperty();
        this.CLASSGROUP  = new SimpleStringProperty();
        this.LEVELGROUP  = new SimpleStringProperty();
        this.GENDERGROUP = new SimpleStringProperty();
        this.HEALTHGROUP = new SimpleStringProperty();
        this.AGEGROUP    = new SimpleIntegerProperty();
    }


    //Contact_id
    public int getContactId() {
        return ID.get();
    }

    public void setContactId(int contactId){
        this.ID.set(contactId);
    }

    public IntegerProperty contactIdProperty(){
        return ID;
    }


    //first name
    public String getFirstName () {
        return FIRSTNAME.get();
    }

    public void setFirstName(String firstName){
        this.FIRSTNAME.set(firstName);
    }

    public StringProperty FirstNameProperty() {
        return FIRSTNAME;
    }


    //last name
    public String getLastName () {
        return LASTNAME.get();
    }

    public void setLastName(String lastName){
        this.LASTNAME.set(lastName);
    }

    public StringProperty LastNameProperty() {
        return LASTNAME;
    }


    //class group
    public String getClassGroup () {
        return CLASSGROUP.get();
    }

    public void setClassGroup(String classGroup){
        this.CLASSGROUP.set(classGroup);
    }

    public StringProperty ClassGroupProperty() {
        return CLASSGROUP;
    }


    //level group
    public String getLevelGroup () {
        return LEVELGROUP.get();
    }

    public void setLevelGroup(String levelGroup){
        this.LEVELGROUP.set(levelGroup);
    }

    public StringProperty LevelGroupProperty() {
        return LEVELGROUP;
    }


    //gender group
    public String getGenderGroup () {
        return GENDERGROUP.get();
    }

    public void setGenderGroup(String genderGroup){
        this.GENDERGROUP.set(genderGroup);
    }

    public StringProperty GenderGroupProperty() {
        return GENDERGROUP;
    }


    //health group
    public String getHealthGroup () {
        return HEALTHGROUP.get();
    }

    public void setHealthGroup(String healthGroup){
        this.HEALTHGROUP.set(healthGroup);
    }

    public StringProperty HealthGroupProperty() {
        return HEALTHGROUP;
    }


    //age group
    public int getAgeGroup () {
        return AGEGROUP.get();
    }

    public void setAgeGroup (int ageGroup){
        this.AGEGROUP.set(ageGroup);
    }

    public IntegerProperty AgeGroupProperty() {
        return AGEGROUP;
    }


}
