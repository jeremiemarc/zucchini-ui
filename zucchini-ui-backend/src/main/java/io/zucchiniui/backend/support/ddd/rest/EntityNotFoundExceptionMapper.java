package io.zucchiniui.backend.support.ddd.rest;

import io.zucchiniui.backend.support.ddd.EntityNotFoundException;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Component
@Provider
public class EntityNotFoundExceptionMapper implements ExceptionMapper<EntityNotFoundException> {

    @Override
    public Response toResponse(final EntityNotFoundException exception) {
        final Response.Status status = Response.Status.NOT_FOUND;
        //final ErrorMessage errorMessage = new ErrorMessage(status.getStatusCode(), exception.getMessage());
        //return Response.status(status).entity(errorMessage).build();
        // FIXME Rest message to return
        return Response.status(status)
            .type(MediaType.TEXT_PLAIN_TYPE)
            .entity(exception.getMessage())
            .build();
    }

}
