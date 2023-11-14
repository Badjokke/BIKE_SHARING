package com.example.bike_sharing.mappers;

import java.util.List;

public interface ModelToDto <F,T>{
    T mapToDto(F modelObject);
    List<T> mapToDto(List<F> modelObjects);
}
