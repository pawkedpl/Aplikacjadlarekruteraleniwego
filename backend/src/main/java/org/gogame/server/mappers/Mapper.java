package org.gogame.server.mappers;

// R for Result
// I for Input
public interface Mapper<I, R> {

    R mapTo(I i);

    I mapFrom(R r);
}
