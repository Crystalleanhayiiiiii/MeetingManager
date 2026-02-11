package com.agendatask.service;

import com.agendatask.entity.Agenda;
import com.agendatask.repository.AgendaRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgendaService {
    @Autowired
    private AgendaRepository repo;

    public List<Agenda> getAll() {
        return repo.findAll();
    }

    public List<Agenda> getByMeeting(String meetingId) {
        return repo.findByMeetingId(meetingId);
    }

    public List<Agenda> create(List<Agenda> agendas) {
        return repo.saveAll(agendas);
    }


    public Agenda update(Long id, Agenda newData) {
        Agenda a = repo.findById(id).orElseThrow();
        a.setTitle(newData.getTitle());
        a.setDescription(newData.getDescription());
        a.setDuration(newData.getDuration());
        a.setOrderIndex(newData.getOrderIndex());
        return repo.save(a);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
}
