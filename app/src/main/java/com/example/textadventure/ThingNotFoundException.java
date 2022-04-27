package com.example.textadventure;
public class ThingNotFoundException extends Exception {
    public ThingNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
