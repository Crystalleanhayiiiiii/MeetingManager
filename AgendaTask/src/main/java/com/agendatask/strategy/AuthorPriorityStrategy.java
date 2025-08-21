package com.agendatask.strategy;

import java.util.List;

public class AuthorPriorityStrategy implements TaskAssignmentStrategy {
    @Override
    public Long assignAssignee(int actionIndex, List<Long> participantIds, Long minutesAuthorId) {
        return (actionIndex % 2 == 0) ? minutesAuthorId : 
               participantIds.get(actionIndex % participantIds.size());
    }
}