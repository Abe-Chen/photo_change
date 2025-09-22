package com.photochange.model;

/**
 * 关键点
 */
public class Keypoint {
    private String id;
    private float x;
    private float y;
    private float confidence;

    public Keypoint() {
    }

    public Keypoint(String id, float x, float y, float confidence) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.confidence = confidence;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getConfidence() {
        return confidence;
    }

    public void setConfidence(float confidence) {
        this.confidence = confidence;
    }
}