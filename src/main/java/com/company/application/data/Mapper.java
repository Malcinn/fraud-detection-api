package com.company.application.data;

public interface Mapper<S, T> {

    T map(S source);
}
