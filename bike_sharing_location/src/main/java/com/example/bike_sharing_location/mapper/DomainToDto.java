package com.example.bike_sharing_location.mapper;

import java.util.List;

public interface DomainToDto <F,T>{
    List<T> mapDomainToDtos(List<F> from);
    T mapDomainToDto(F from);

}
