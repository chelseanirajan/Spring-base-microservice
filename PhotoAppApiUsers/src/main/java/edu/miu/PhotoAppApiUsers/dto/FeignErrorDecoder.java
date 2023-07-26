package edu.miu.PhotoAppApiUsers.dto;

import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.server.ResponseStatusException;

@Component
public class FeignErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String s, Response response) {
        switch (response.status()){
            case 400:
                // return new BadRequestException();
                break;
            case 404:
                if(s.contains("getAlbums")){
                return new ResponseStatusException(HttpStatus.valueOf(response.status()), "Endpoint is not correct");
                }
                break;
            default:
                return new Exception(response.reason());
        }
        return null;
    }
}
