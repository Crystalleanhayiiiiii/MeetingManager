package com.agendatask.service;

import org.springframework.stereotype.Service;
import java.util.regex.*;
import java.util.*;

@Service
public class NLPService {
    public List<String> extractActionItems(String content) {
        // Triển khai regex để trích xuất action items
        Pattern pattern = Pattern.compile("(?i)(action item|task|nhiệm vụ):?\\s*(.*?)(?=\\n|$)");
        Matcher matcher = pattern.matcher(content);
        
        List<String> actions = new ArrayList<>();
        while (matcher.find()) {
            actions.add(matcher.group(2).trim());
        }
        return actions;
    }
}