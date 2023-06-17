package com.application.easycook.RecipesPackage;

import java.util.ArrayList;
import java.util.HashMap;

public class Recipes {
    private String id;
    private HashMap<String,String> ingredients;
    private String title;
    private String main_image;
    private String category;
    private String recipeName;
    private String subCategory;
    private String numberOfDishes;
    private String description;
    private ArrayList<String> preparationTime;
    private String difficultyLevel;
    private String calories;
    private ArrayList<String> PreparationSteps;

    Recipes(){

    }

    public Recipes(String id,HashMap<String, String> ingredients, String title, String main_image, String category, String recipeName, String subCategory, String numberOfDishes, String description, ArrayList<String> preparationTime, String difficultyLevel, String calories, ArrayList<String> preparationSteps) {
        this.id=id;
        this.ingredients = ingredients;
        this.title = title;
        this.main_image = main_image;
        this.category = category;
        this.recipeName = recipeName;
        this.subCategory = subCategory;
        this.numberOfDishes = numberOfDishes;
        this.description = description;
        this.preparationTime = preparationTime;
        this.difficultyLevel = difficultyLevel;
        this.calories = calories;
        PreparationSteps = preparationSteps;
    }

    public HashMap<String, String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(HashMap<String, String> ingredients) {
        this.ingredients = ingredients;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    public String getNumberOfDishes() {
        return numberOfDishes;
    }

    public void setNumberOfDishes(String numberOfDishes) {
        this.numberOfDishes = numberOfDishes;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<String> getPreparationTime() {
        return preparationTime;
    }

    public void setPreparationTime(ArrayList<String> preparationTime) {
        this.preparationTime = preparationTime;
    }

    public String getDifficultyLevel() {
        return difficultyLevel;
    }

    public void setDifficultyLevel(String difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }

    public String getCalories() {
        return calories;
    }

    public void setCalories(String calories) {
        this.calories = calories;
    }

    public ArrayList<String> getPreparationSteps() {
        return PreparationSteps;
    }

    public void setPreparationSteps(ArrayList<String> preparationSteps) {
        PreparationSteps = preparationSteps;
    }
    public String getMain_image() {
        return main_image;
    }

    public void setMain_image(String main_image) {
        this.main_image = main_image;
    }

    @Override
    public String toString() {
        return "\nRecipes{" +
                "\n ID='" + id +
                "\ningredients=" + ingredients +
                "\n title='" + title +
                "\n main_image='" + main_image +
                "\n category='" + category +
                "\n recipeName='" + recipeName +
                "\n subCategory='" + subCategory +
                "\n numberOfDishes='" + numberOfDishes +
                "\n description='" + description +
                "\n preparationTime=" + preparationTime +
                "\n difficultyLevel='" + difficultyLevel +
                "\n calories='" + calories +
                "\n PreparationSteps=" + PreparationSteps +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
