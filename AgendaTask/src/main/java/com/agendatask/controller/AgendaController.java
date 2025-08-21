package com.agendatask.controller;

import com.agendatask.entity.Agenda;
import com.agendatask.service.AgendaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/agendas")
public class AgendaController {
    @Autowired
    private AgendaService service;

    @GetMapping
    public List<Agenda> getAll() {
        return service.getAll();
    }

    @GetMapping("/meeting/{meetingId}")
    public List<Agenda> getByMeeting(@PathVariable String meetingId) {
        return service.getByMeeting(meetingId);
    }

    @PostMapping
    public List<Agenda> create(@RequestBody List<Agenda> agendas) {
        return service.create(agendas);
    }


    @PutMapping("/{id}")
    public Agenda update(@PathVariable Long id, @RequestBody Agenda agenda) {
        return service.update(id, agenda);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
