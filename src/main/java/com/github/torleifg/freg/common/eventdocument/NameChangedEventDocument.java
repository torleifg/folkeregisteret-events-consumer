package com.github.torleifg.freg.common.eventdocument;

import lombok.*;

@Value
public class NameChangedEventDocument implements EventDocument {
    String givenName;
    String familyName;
}