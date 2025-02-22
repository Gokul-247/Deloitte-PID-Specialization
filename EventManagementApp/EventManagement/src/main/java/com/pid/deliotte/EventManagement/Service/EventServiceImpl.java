package com.pid.deliotte.EventManagement.Service;

import com.pid.deliotte.EventManagement.Entity.Category;
import com.pid.deliotte.EventManagement.Entity.Event;
import com.pid.deliotte.EventManagement.Exception.CategoryNotFoundException;
import com.pid.deliotte.EventManagement.Model.EventRequest;
import com.pid.deliotte.EventManagement.Model.EventResponse;
import com.pid.deliotte.EventManagement.Repository.CategoryRepository;
import com.pid.deliotte.EventManagement.Repository.EventRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;

    public EventServiceImpl(EventRepository eventRepository, CategoryRepository categoryRepository) {
        this.eventRepository = eventRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public EventResponse createEvent(EventRequest eventRequest) {

        Category category = categoryRepository.findById(eventRequest.getCategoryId())
                .orElseThrow(() -> new CategoryNotFoundException("Category with ID " + eventRequest.getCategoryId() + " not found"));


        Event event = new Event();
        event.setName(eventRequest.getName());
        event.setDescription(eventRequest.getDescription());
        event.setDate(eventRequest.getDate());
        event.setTime(eventRequest.getTime());
        event.setBudget(eventRequest.getBudget());
        event.setCategory(category);

        event = eventRepository.save(event);


        return mapToEventResponse(event);
    }
    @Override
    public List<EventResponse> getEventsByCategory(String categoryName) {
        List<Event> events = eventRepository.findByCategoryName(categoryName);

        return events.stream()
                .map(this::mapToEventResponse)
                .collect(Collectors.toList());
    }


    //Helper method
    private EventResponse mapToEventResponse(Event event) {
        return new EventResponse(
                event.getId(),
                event.getName(),
                event.getDescription(),
                event.getDate(),
                event.getTime(),
                event.getBudget(),
                event.getCategory().getName()
        );
    }
}
