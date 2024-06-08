package com.acme.center.platform.learning.domain.model.valueobjects;

import com.acme.center.platform.learning.domain.model.aggregates.Course;
import com.acme.center.platform.learning.domain.model.entities.LearningPathItem;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
@Embeddable

public class LearningPath {



    @OneToMany(mappedBy = "learningPath", cascade = CascadeType.ALL)
    //it is used to specify the relationship between the entities
    private List<LearningPathItem> learningPathItems;

    public LearningPath(){
        this.learningPathItems = new ArrayList<>();
    }

    public void addItem(Course course, Long tutorialId, LearningPathItem nexItem){
        //Add the new item before the next item
        System.out.println("Adding item to learning path");
        LearningPathItem learningPathItem = new LearningPathItem(course, tutorialId, nexItem);
        System.out.println("tutorial Id " + learningPathItem.getTutorialId());
        learningPathItems.add(learningPathItem);
    }

    /**
     * Adds the item at the end of the learning path
     * @param course The course to add
     * @param tutorialId the tutorial to add
     */

    public void addItem(Course course, Long tutorialId){
        LearningPathItem learningPathItem = new LearningPathItem(course, tutorialId, null);
        LearningPathItem originalLastItem = null;
        if(!learningPathItems.isEmpty()){
            originalLastItem = getLastItemInLearningPath();
        }else{
            System.out.println("Learning path is empty");
        }
        learningPathItems.add(learningPathItem);
        System.out.println("tutorial Id " + learningPathItem.getTutorialId());
        System.out.println("Learning path item added");
        if(originalLastItem!=null) originalLastItem.updateNextItem(learningPathItem);
    }

    /**
     * Adds the item at the end of the learning path
     * @param course The course to add
     * @param tutorialId The tutorial to add
     * @param nextTutorialId the id of the tutorial before which the new item should be added
     */
    public void addItem(Course course, Long tutorialId, Long nextTutorialId){
        LearningPathItem nextItem = getLearningPathItemWithId(nextTutorialId);
        addItem(course, tutorialId, nextItem);
    }

    public boolean isEmpty(){
        return learningPathItems.isEmpty();
    }

    public Long getNextTutorialInLearningPath(Long currentTutorialId){
        LearningPathItem item = getLearningPathItemWithTutorialId(currentTutorialId);
        return item!= null ? item.getTutorialId(): null;
    }

    public boolean isLastTutorialInLearningPath(Long currentTutorialId){
        return  getNextTutorialInLearningPath(currentTutorialId) == null;
    }

    public Long getFirstTutorialIdLearningPath(){
        return learningPathItems.get(0).getTutorialId();
    }
    private  LearningPathItem getLearningPathItemWithId(Long itemId) {
        return  learningPathItems.stream().filter(learningPathItem ->
                        learningPathItem.getId().equals(itemId))
                .findFirst().orElse(null);
    }
    public LearningPathItem getLearningPathItemWithTutorialId(Long tutorialId){
        return learningPathItems.stream().filter(item -> item.getTutorialId().equals(tutorialId))
                .findFirst().orElse(null);
    }
    public LearningPathItem getLastItemInLearningPath(){
        return learningPathItems.stream().filter(item -> item.getNextItem() == null)
                .findFirst().orElse(null);
    }
}