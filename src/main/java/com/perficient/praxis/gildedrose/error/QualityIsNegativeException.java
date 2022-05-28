package com.perficient.praxis.gildedrose.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class QualityIsNegativeException extends RuntimeException{

    public QualityIsNegativeException(String message){
        super(message);
    }
}

