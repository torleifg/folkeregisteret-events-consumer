package com.github.torleifg.freg.common.event;

import lombok.*;

@Value
public class Event {
    Long sequence;
    EventDetails eventDetails;
}