package io.pivotal.notes.models;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.Transient;

@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class BaseResponse {

    @Transient
    private Error error;

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }
}
