package com.example.bike_sharing_location.mapper;

import java.util.List;

/**
 * Interface for converting one class to another
 * Used to convert objects on server to objects for http responses.
 * @param <F> class from which conversion is being performed
 * @param <T> class to which conversion is being performed
 */
public interface DomainToDto <F,T>{
    List<T> mapDomainToDtos(List<F> from);
    T mapDomainToDto(F from);

}
