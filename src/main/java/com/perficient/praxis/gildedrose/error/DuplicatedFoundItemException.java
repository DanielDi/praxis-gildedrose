package com.perficient.praxis.gildedrose.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class DuplicatedFoundItemException extends RuntimeException{

    public DuplicatedFoundItemException(String message){
        super(message);
    }
}
