package com.company.application;

public interface Mapper<S, T> {

    T map(S source);
}
