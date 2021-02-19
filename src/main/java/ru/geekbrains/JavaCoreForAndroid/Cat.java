package ru.geekbrains.JavaCoreForAndroid;

/**
 * Сourse: java core for android
 * Faculty of Geek University Android Development
 *
 * @Author Student Dmitry Veremeenko aka StDimensiy
 * Group 24.12.2020
 * <p>
 * HomeWork for lesson 9
 * Created 11.02.2021
 * v1.0
 */
public class Cat {
    private static int count;
    //переменные объекта
    private int catId;
    private String name;
    private String species;
    private String color;
    private int weight;
    private int yearOfBirth;

    public Cat(int catId, String name, String species, String color, int weight, int yearOfBirth) {
        this.catId = catId;
        this.name = name;
        this.species = species;
        this.color = color;
        this.weight = weight;
        this.yearOfBirth = yearOfBirth;
        count++;
    }

    public Cat() {
        count++;
    }

    public int getCount() {
        return count;
    }

    public int getCatId() {
        return catId;
    }

    public String getName() {
        return name;
    }

    public String getSpecies() {
        return species;
    }

    public String getColor() {
        return color;
    }

    public int getWeight() {
        return weight;
    }

    public int getYearOfBirth() {
        return yearOfBirth;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public void setCatId(int catId) {
        this.catId = catId;
    }

    public void setYearOfBirth(int yearOfBirth) {
        this.yearOfBirth = yearOfBirth;
    }

    @Override
    public String toString() {
        return "\n Кот / Кошка \n" +
                " Уникальный идентификатор в системе: " + catId +
                ",\n Кличка: '" + name + '\'' +
                ",\n Порода: '" + species + '\'' +
                ",\n Окрас: '" + color + '\'' +
                ",\n Вес: " + weight + "(гр.) \'" +
                ",\n Год рождения: " + yearOfBirth +"г.\n -----------------------------------------------------------";
    }
}
