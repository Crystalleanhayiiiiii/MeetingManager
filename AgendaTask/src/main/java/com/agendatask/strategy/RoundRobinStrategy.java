package com.agendatask.strategy;

import java.util.List;

public class RoundRobinStrategy implements TaskAssignmentStrategy {
    @Override
    public Long assignAssignee(int actionIndex, List<Long> participantIds, Long minutesAuthorId) {
        return participantIds.get(actionIndex % participantIds.size());
    }
}