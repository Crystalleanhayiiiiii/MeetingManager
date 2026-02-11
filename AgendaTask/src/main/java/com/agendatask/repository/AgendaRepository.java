package com.agendatask.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agendatask.entity.Agenda;

import java.util.List;

public interface AgendaRepository extends JpaRepository<Agenda, Long> {
    List<Agenda> findByMeetingId(String meetingId);
}
