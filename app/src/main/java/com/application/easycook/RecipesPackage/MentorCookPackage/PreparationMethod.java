package com.application.easycook.RecipesPackage.MentorCookPackage;

import android.media.Image;

import java.util.List;

public class PreparationMethod {
    private List<Action> actions;

    public PreparationMethod(List<Action> actions) {
        this.actions = actions;
    }

    public List<Action> getActions() {
        return actions;
    }

    public void setActions(List<Action> actions) {
        this.actions = actions;
    }

    public static class Action {
        private String name;
        private String details;
        private Image image;

        public Action(String name, String details, Image image) {
            this.name = name;
            this.details = details;
            this.image = image;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDetails() {
            return details;
        }

        public void setDetails(String details) {
            this.details = details;
        }

        public Image getImage() {
            return image;
        }

        public void setImage(Image image) {
            this.image = image;
        }
    }
}

