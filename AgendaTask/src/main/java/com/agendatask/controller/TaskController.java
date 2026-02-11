package com.agendatask.controller;

import com.agendatask.entity.Task;
import com.agendatask.service.TaskService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    @Autowired
    private TaskService service;

    @GetMapping
    public List<Task> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public Task getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @GetMapping("/meeting/{meetingId}")
    public List<Task> getByMeeting(@PathVariable String meetingId) {
        return service.getByMeeting(meetingId);
    }

    @GetMapping("/assignee/{assigneeId}")
    public List<Task> getByAssignee(@PathVariable Long assigneeId) {
        return service.getByAssignee(assigneeId);
    }

    @PostMapping
    public Task create(@RequestBody Task task) {
        return service.create(task);
    }

    @PutMapping("/{id}")
    public Task update(@PathVariable Long id, @RequestBody Task task) {
        return service.update(id, task);
    }
    @PutMapping("/{id}/status")
    public Task updateStatus(@PathVariable Long id, @RequestBody Map<String, String> payload) {
        String statusStr = payload.get("status");
        return service.updateStatus(id, statusStr);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
