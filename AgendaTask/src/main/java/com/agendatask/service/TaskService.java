package com.agendatask.service;

import com.agendatask.entity.Task;
import com.agendatask.repository.TaskRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {
    @Autowired
    private TaskRepository repo;

    public List<Task> getAll() {
        return repo.findAll();
    }

    public Task getById(Long id) {
        return repo.findById(id).orElseThrow();
    }

    public List<Task> getByMeeting(String meetingId) {
        return repo.findByMeetingId(meetingId);
    }

    public List<Task> getByAssignee(Long assigneeId) {
        return repo.findByAssigneeId(assigneeId);
    }

    public Task create(Task task) {
        return repo.save(task);
    }

    public Task update(Long id, Task newData) {
        Task t = repo.findById(id).orElseThrow();
        t.setTitle(newData.getTitle());
        t.setDescription(newData.getDescription());
        t.setDueDate(newData.getDueDate());
        t.setStatus(newData.getStatus());
        t.setAgendaItemId(newData.getAgendaItemId());
        return repo.save(t);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
    public Task updateStatus(Long id, String status) {
        Task t = repo.findById(id).orElseThrow();
        Task.Status statusEnum = Task.Status.valueOf(status.toUpperCase());

        t.setStatus(statusEnum);
        return repo.save(t);
    }


}
