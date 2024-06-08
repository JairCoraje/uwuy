package com.acme.center.platform.learning.domain.model.valueobjects;

import com.acme.center.platform.learning.domain.model.aggregates.Enrollment;
import com.acme.center.platform.learning.domain.model.entities.ProgressRecordItem;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable

public class ProgressRecord {
    @OneToMany(mappedBy = "enrollment", cascade = CascadeType.ALL) //it is used to map the ProgressRecordItem entity
    private List<ProgressRecordItem> progressRecordItems;
    public ProgressRecord(){
        progressRecordItems = new ArrayList<>();
    }
    public void initializeProgressRecord(Enrollment enrollment, LearningPath learningPath){
        if(learningPath.isEmpty()) return;
        Long tutorialId = learningPath.getFirstTutorialIdLearningPath();
        ProgressRecordItem progressRecordItem = new ProgressRecordItem(enrollment, tutorialId);
        progressRecordItems.add(progressRecordItem);
    }

    public void startTutorial(Long tutorialId) {
        if (hasAnItemInProgress()) throw new IllegalStateException("A tutorial is already in progress");
        ProgressRecordItem progressRecordItem = getProgressRecordItemWithTutorialId(tutorialId);
        if (progressRecordItem != null) {
            if (progressRecordItem.isNotStarted()) progressRecordItem.start();
            else throw new IllegalStateException("Tutorial is already started");
        } else throw new IllegalArgumentException("Tutorial not found");
    }

    public void completeTutorial(Long tutorialId, LearningPath learningPath){
        ProgressRecordItem progressRecordItem = getProgressRecordItemWithTutorialId(tutorialId);
        if(progressRecordItem != null) progressRecordItem.complete();
        else throw new IllegalArgumentException("Tutorial not found");
        if(learningPath.isLastTutorialInLearningPath(tutorialId)) return;
        Long nexTutorialId = learningPath.getNextTutorialInLearningPath(tutorialId);
        if(nexTutorialId!= null){
            ProgressRecordItem nextProgressRecordItem = getProgressRecordItemWithTutorialId(nexTutorialId);
            progressRecordItems.add(nextProgressRecordItem);
        }
    }

    private ProgressRecordItem getProgressRecordItemWithTutorialId(long tutorialId){
        return  progressRecordItems.stream().filter(progressRecordItem ->
                progressRecordItem.getTutorialId() == tutorialId).findFirst().orElse(null);
    }

    public boolean hasAnItemInProgress(){
        return progressRecordItems.stream().anyMatch(ProgressRecordItem::isInProgress);
    }

    public long calculateDaysElapsedForEnrollment(Enrollment enrollment){
        return  progressRecordItems.stream().filter(progressRecordItem ->
                        progressRecordItem.getEnrollment().equals(enrollment))
                .mapToLong(ProgressRecordItem::calculateDaysElapsed).sum();
    }
}