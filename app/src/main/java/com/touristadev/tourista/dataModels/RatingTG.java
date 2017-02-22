package com.touristadev.tourista.dataModels;

/**
 * Created by Christian on 1/20/2017.
 */

public class RatingTG {
    private String guideId;
    private float knowledge;
    private float personality;
    private float professional;

    public RatingTG(String guideId, float knowledge, float personality, float professional) {
        this.guideId = guideId;
        this.knowledge = knowledge;
        this.personality = personality;
        this.professional = professional;
    }

    public String getGuideId() {
        return guideId;
    }

    public void setGuideId(String guideId) {
        this.guideId = guideId;
    }

    public float getKnowledge() {
        return knowledge;
    }

    public void setKnowledge(float knowledge) {
        this.knowledge = knowledge;
    }

    public float getPersonality() {
        return personality;
    }

    public void setPersonality(float personality) {
        this.personality = personality;
    }

    public float getProfessional() {
        return professional;
    }

    public void setProfessional(float professional) {
        this.professional = professional;
    }
}
