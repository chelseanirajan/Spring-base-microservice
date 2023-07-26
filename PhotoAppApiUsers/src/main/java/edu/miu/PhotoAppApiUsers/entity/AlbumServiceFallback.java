package edu.miu.PhotoAppApiUsers.entity;

import edu.miu.PhotoAppApiUsers.model.AlbumResponseModel;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

//@Component
public class AlbumServiceFallback implements FallbackFactory<AlbumsServiceClient> {

    @Override
    public AlbumsServiceClient create(Throwable cause) {
        return new AlbumServiceClientFallBack(cause);
    }
}
