package com.agendatask.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agendatask.entity.Task;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByMeetingId(String meetingId);
    List<Task> findByAssigneeId(Long assigneeId);
}
