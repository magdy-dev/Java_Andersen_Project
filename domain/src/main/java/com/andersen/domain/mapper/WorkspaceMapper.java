package com.andersen.domain.mapper;

import com.andersen.domain.dto.workspace.WorkspaceDto;
import com.andersen.domain.entity.workspace.Workspace;

/**
 * Mapper class for converting between Workspace entities and Workspace Data Transfer Objects (DTOs).
 * This class provides methods to translate Workspace data to and from its DTO representation
 * to facilitate data transfer across different layers of the application.
 */
public class WorkspaceMapper {

    /**
     * Converts a Workspace entity to a WorkspaceDto.
     *
     * @param entity the Workspace entity to be converted
     * @return a WorkspaceDto representing the provided Workspace entity, or null if the entity is null
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
     * Converts a WorkspaceDto to a Workspace entity.
     *
     * @param dto the WorkspaceDto to be converted
     * @return a Workspace entity representing the provided WorkspaceDto, or null if the dto is null
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