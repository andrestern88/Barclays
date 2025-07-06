package com.example.barclays.mappers;

public interface Mapper<Entity, DTO> {

    Entity mapTo(DTO dto);

    DTO mapFrom(Entity entity);

}
