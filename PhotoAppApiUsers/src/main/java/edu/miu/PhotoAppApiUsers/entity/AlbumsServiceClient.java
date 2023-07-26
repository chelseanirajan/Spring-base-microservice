package edu.miu.PhotoAppApiUsers.entity;

import edu.miu.PhotoAppApiUsers.model.AlbumResponseModel;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;

@FeignClient(name = "album-ws")
//@FeignClient(name = "album-ws",fallbackFactory = AlbumServiceFallback.class)
public interface AlbumsServiceClient {

    @GetMapping("/users/{id}/albums")
    @Retry(name = "album-ws")
    @CircuitBreaker(name = "album-ws", fallbackMethod = "getAlbumsFallback")
    List<AlbumResponseModel> getAlbums(@PathVariable String id);

    default List<AlbumResponseModel> getAlbumsFallback(String id, Throwable ex){
        System.out.println("Param = "+id);
        System.out.println("Exception took place: "+ex.getMessage());
        return new ArrayList<>();
    }

}
