package com.agendatask.strategy;

import java.util.List;

public interface TaskAssignmentStrategy {
    Long assignAssignee(int actionIndex, List<Long> participantIds, Long minutesAuthorId);
}