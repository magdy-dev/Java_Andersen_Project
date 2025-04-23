package com.andersen.ui.mapper;


import com.andersen.domain.entity.workspace.Workspace;
import com.andersen.ui.dto.workspace.WorkspaceDto;

/**
 * Mapper for converting between Workspace entities and Workspace DTOs.
 * <p>
 * This class contains static methods for converting between
 * {@link Workspace} entities and {@link WorkspaceDto} data transfer
 * objects. The methods ensure that the mapping is clear and
 * encapsulated, promoting separation of concerns between the
 * database layer and the application's data representation layer.
 * </p>
 */

public class WorkspaceMapper {

    /**
     * Converts a {@link Workspace} entity to a {@link WorkspaceDto}.
     *
     * @param entity the Workspace entity to convert
     * @return the converted WorkspaceDto, or null if the provided entity is null
     */
    public static WorkspaceDto toDto(Workspace entity) {
        if (entity == null) return null;

        return new WorkspaceDto(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getType(),
                entity.getPricePerHour(),
                entity.getCapacity(),
                entity.isActive()
        );
    }

    /**
     * Converts a {@link WorkspaceDto} to a {@link Workspace} entity.
     *
     * @param dto the WorkspaceDto to convert
     * @return the converted Workspace entity, or null if the provided dto is null
     */
    public static Workspace toEntity(WorkspaceDto dto) {
        if (dto == null) return null;

        return new Workspace(
                dto.getId(),
                dto.getName(),
                dto.getDescription(),
                dto.getType(),
                dto.getPricePerHour(),
                dto.getCapacity(),
                dto.isActive()
        );
    }
}