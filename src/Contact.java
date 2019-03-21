/*
socketServer Contact Card information
v1.0
(c) 2019 Jacob Bogner
 */
public class Contact {
    public String name;
    public String number;

    public Contact (String name, String number){ //Create a contact with name and number
        this.name = name;
        this.number = number;
    }

    public String getName(){
        return this.name;
    } //Return the name of a contact

    public String getNumber(){
        return this.number;
    } //Return the number of a contact

}