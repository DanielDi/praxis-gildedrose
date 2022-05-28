package com.perficient.praxis.gildedrose.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class QualityUnsuitableException extends RuntimeException{

    public QualityUnsuitableException(String message){
        super(message);
    }
}

