package edu.miu.PhotoAppApiUsers.entity;

import edu.miu.PhotoAppApiUsers.model.AlbumResponseModel;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.retry.annotation.CircuitBreaker;

import java.util.ArrayList;
import java.util.List;

public class AlbumServiceClientFallBack implements AlbumsServiceClient{

    Logger logger = LoggerFactory.getLogger(this.getClass());
    Throwable throwable;

    public AlbumServiceClientFallBack() {
    }

    public AlbumServiceClientFallBack(Throwable throwable) {
        this.throwable = throwable;
    }

//    @CircuitBreaker("album-ws")
    @Override
    public List<AlbumResponseModel> getAlbums(String id) {

        if(throwable instanceof FeignException && ((FeignException)throwable).status() == 404){
            logger.error("404 error took place with getAlbums was called with userId: "+id + " error" +throwable.getMessage());
        }else {
            logger.error("Other error took place "+throwable.getMessage() );
        }

        return new ArrayList<>();
    }
}
